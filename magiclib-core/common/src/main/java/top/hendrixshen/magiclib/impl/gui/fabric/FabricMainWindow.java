/*
 * Copyright 2016 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package top.hendrixshen.magiclib.impl.gui.fabric;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.impl.gui.fabric.FabricStatusTree.*;
import top.hendrixshen.magiclib.util.fabric.StringUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.List;
import java.util.*;
import java.util.concurrent.CountDownLatch;

class FabricMainWindow {
    static Icon missingIcon = null;

    static void open(FabricStatusTree tree, boolean shouldWait) throws Exception {
        if (GraphicsEnvironment.isHeadless()) {
            throw new HeadlessException();
        }

        // Set MacOS specific system props
        System.setProperty("apple.awt.application.appearance", "system");
        System.setProperty("apple.awt.application.name", tree.title);
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        FabricMainWindow.open0(tree, shouldWait);
    }

    private static void open0(FabricStatusTree tree, boolean shouldWait) throws Exception {
        CountDownLatch guiTerminatedLatch = new CountDownLatch(1);
        SwingUtilities.invokeAndWait(() -> FabricMainWindow.createUi(guiTerminatedLatch, tree));

        if (shouldWait) {
            guiTerminatedLatch.await();
        }
    }

    private static void createUi(CountDownLatch onCloseLatch, @NotNull FabricStatusTree tree) {
        JFrame window = new JFrame();
        window.setVisible(false);
        window.setTitle(tree.title);

        try {
            Image image = FabricMainWindow.loadImage("/assets/magiclib-core/icon.png");
            window.setIconImage(image);
            FabricMainWindow.setTaskBarImage(image);
        } catch (IOException e) {
            e.printStackTrace();
        }

        window.setMinimumSize(new Dimension(640, 480));
        window.setPreferredSize(new Dimension(800, 480));
        window.setLocationByPlatform(true);
        window.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                onCloseLatch.countDown();
            }
        });

        Container contentPane = window.getContentPane();

        if (tree.mainText != null && !tree.mainText.isEmpty()) {
            JLabel errorLabel = new JLabel(tree.mainText);
            errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
            Font font = errorLabel.getFont();
            errorLabel.setFont(font.deriveFont(font.getSize() * 2.0f));
            contentPane.add(errorLabel, BorderLayout.NORTH);
        }

        IconSet icons = new IconSet();

        if (tree.tabs.isEmpty()) {
            FabricStatusTab tab = new FabricStatusTab("Opening Errors");
            tab.addChild("No tabs provided! (Something is very broken)").setError();
            contentPane.add(createTreePanel(tab.node, tab.filterLevel, icons), BorderLayout.CENTER);
        } else if (tree.tabs.size() == 1) {
            FabricStatusTab tab = tree.tabs.get(0);
            contentPane.add(createTreePanel(tab.node, tab.filterLevel, icons), BorderLayout.CENTER);
        } else {
            JTabbedPane tabs = new JTabbedPane();
            contentPane.add(tabs, BorderLayout.CENTER);

            for (FabricStatusTab tab : tree.tabs) {
                tabs.addTab(tab.node.name, createTreePanel(tab.node, tab.filterLevel, icons));
            }
        }

        if (!tree.buttons.isEmpty()) {
            JPanel buttons = new JPanel();
            contentPane.add(buttons, BorderLayout.SOUTH);
            buttons.setLayout(new FlowLayout(FlowLayout.TRAILING));

            for (FabricStatusButton button : tree.buttons) {
                JButton btn = new JButton(button.text);
                buttons.add(btn);
                btn.addActionListener(event -> {
                    if (button.type == FabricBasicButtonType.CLICK_ONCE) {
                        btn.setEnabled(false);
                    }

                    if (button.clipboard != null) {
                        try {
                            StringSelection clipboard = new StringSelection(button.clipboard);
                            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(clipboard, clipboard);
                        } catch (IllegalStateException e) {
                            //Clipboard unavailable?
                        }
                    }

                    if (button.shouldClose) {
                        window.dispose();
                    }

                    if (button.shouldContinue) {
                        onCloseLatch.countDown();
                    }
                });
            }
        }

        window.pack();
        window.setVisible(true);
        window.requestFocus();
    }

    private static @NotNull JPanel createTreePanel(FabricStatusNode rootNode, FabricTreeWarningLevel minimumWarningLevel,
                                                   IconSet iconSet) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        TreeNode treeNode = new CustomTreeNode(null, rootNode, minimumWarningLevel);
        DefaultTreeModel model = new DefaultTreeModel(treeNode);
        JTree tree = new JTree(model);
        tree.setRootVisible(false);
        tree.setRowHeight(0); // Allow rows to be multiple lines tall

        for (int row = 0; row < tree.getRowCount(); row++) {
            if (!tree.isVisible(tree.getPathForRow(row))) {
                continue;
            }

            CustomTreeNode node = ((CustomTreeNode) tree.getPathForRow(row).getLastPathComponent());

            if (node.node.expandByDefault) {
                tree.expandRow(row);
            }
        }

        ToolTipManager.sharedInstance().registerComponent(tree);
        tree.setCellRenderer(new CustomTreeCellRenderer(iconSet));
        JScrollPane scrollPane = new JScrollPane(tree);
        panel.add(scrollPane);
        return panel;
    }

    private static BufferedImage loadImage(String str) throws IOException {
        return ImageIO.read(loadStream(str));
    }

    private static @NotNull InputStream loadStream(String str) throws FileNotFoundException {
        InputStream stream = FabricMainWindow.class.getResourceAsStream(str);

        if (stream == null) {
            throw new FileNotFoundException(str);
        }

        return stream;
    }

    private static void setTaskBarImage(Image image) {
        try {
            // TODO Remove reflection when updating past Java 8
            Class<?> taskbarClass = Class.forName("java.awt.Taskbar");
            Method getTaskbar = taskbarClass.getDeclaredMethod("getTaskbar");
            Method setIconImage = taskbarClass.getDeclaredMethod("setIconImage", Image.class);
            Object taskbar = getTaskbar.invoke(null);
            setIconImage.invoke(taskbar, image);
        } catch (Exception e) {
            // Ignored
        }
    }

    static final class IconSet {
        /**
         * Map of IconInfo -> Integer Size -> Real Icon.
         */
        private final Map<IconInfo, Map<Integer, Icon>> icons = new HashMap<>();

        public Icon get(IconInfo info) {
            // TODO: HDPI
            int scale = 16;
            Map<Integer, Icon> map = icons.get(info);

            if (map == null) {
                icons.put(info, map = new HashMap<>());
            }

            Icon icon = map.get(scale);

            if (icon == null) {
                try {
                    icon = loadIcon(info, scale);
                } catch (IOException e) {
                    e.printStackTrace();
                    icon = missingIcon();
                }

                map.put(scale, icon);
            }

            return icon;
        }
    }

    private static Icon missingIcon() {
        if (missingIcon == null) {
            BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB);

            for (int y = 0; y < 16; y++) {
                for (int x = 0; x < 16; x++) {
                    img.setRGB(x, y, 0xff_ff_f2);
                }
            }

            for (int i = 0; i < 16; i++) {
                img.setRGB(0, i, 0x22_22_22);
                img.setRGB(15, i, 0x22_22_22);
                img.setRGB(i, 0, 0x22_22_22);
                img.setRGB(i, 15, 0x22_22_22);
            }

            for (int i = 3; i < 13; i++) {
                img.setRGB(i, i, 0x9b_00_00);
                img.setRGB(i, 16 - i, 0x9b_00_00);
            }

            missingIcon = new ImageIcon(img);
        }

        return missingIcon;
    }

    @Contract("_, _ -> new")
    private static @NotNull Icon loadIcon(@NotNull IconInfo info, int scale) throws IOException {
        BufferedImage img = new BufferedImage(scale, scale, BufferedImage.TYPE_INT_ARGB);
        Graphics2D imgG2d = img.createGraphics();
        BufferedImage main = FabricMainWindow.loadImage("/assets/magiclib-core/ui/icon/" + info.mainPath + "_x" + scale + ".png");
        assert main.getWidth() == scale;
        assert main.getHeight() == scale;
        imgG2d.drawImage(main, null, 0, 0);
        final int[][] coords = {{0, 8}, {8, 8}, {8, 0}};

        for (int i = 0; i < info.decor.length; i++) {
            String decor = info.decor[i];

            if (decor == null) {
                continue;
            }

            BufferedImage decorImg = FabricMainWindow.loadImage("/assets/magiclib-core/ui/icon/decoration/" + decor + "_x" + (scale / 2) + ".png");
            assert decorImg.getWidth() == scale / 2;
            assert decorImg.getHeight() == scale / 2;
            imgG2d.drawImage(decorImg, null, coords[i][0], coords[i][1]);
        }

        return new ImageIcon(img);
    }

    static final class IconInfo {
        public final String mainPath;
        public final String[] decor;
        private final int hash;

        @Contract(pure = true)
        IconInfo(@NotNull String mainPath) {
            this.mainPath = mainPath;
            this.decor = new String[0];
            this.hash = mainPath.hashCode();
        }

        IconInfo(String mainPath, String @NotNull [] decor) {
            this.mainPath = mainPath;
            this.decor = decor;
            assert decor.length < 4 : "Cannot fit more than 3 decorations into an image (and leave space for the background)";

            if (decor.length == 0) {
                // To mirror the no-decor constructor
                this.hash = mainPath.hashCode();
            } else {
                this.hash = mainPath.hashCode() * 31 + Arrays.hashCode(decor);
            }
        }

        @Contract("_ -> new")
        public static @NotNull IconInfo fromNode(@NotNull FabricStatusNode node) {
            String[] split = node.iconType.split("\\+");

            if (split.length == 1 && split[0].isEmpty()) {
                split = new String[0];
            }

            final String main;
            List<String> decors = new ArrayList<>();
            FabricTreeWarningLevel warnLevel = node.getMaximumWarningLevel();

            if (split.length == 0) {
                // Empty string, but we might replace it with a warning
                if (warnLevel == FabricTreeWarningLevel.NONE) {
                    main = "missing";
                } else {
                    main = "level_" + warnLevel.lowerCaseName;
                }
            } else {
                main = split[0];

                if (warnLevel == FabricTreeWarningLevel.NONE) {
                    // Just to add a gap
                    decors.add(null);
                } else {
                    decors.add("level_" + warnLevel.lowerCaseName);
                }

                for (int i = 1; i < split.length && i < 3; i++) {
                    decors.add(split[i]);
                }
            }

            return new IconInfo(main, decors.toArray(new String[0]));
        }

        @Override
        public int hashCode() {
            return this.hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }

            if (obj == null || obj.getClass() != getClass()) {
                return false;
            }

            IconInfo other = (IconInfo) obj;
            return this.mainPath.equals(other.mainPath) && Arrays.equals(this.decor, other.decor);
        }
    }

    private static final class CustomTreeCellRenderer extends DefaultTreeCellRenderer {
        private static final long serialVersionUID = -5621219150752332739L;

        private final IconSet iconSet;

        private CustomTreeCellRenderer(IconSet icons) {
            this.iconSet = icons;
            //setVerticalTextPosition(TOP); // Move icons to top rather than centre
        }

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
                                                      boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            this.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));

            if (value instanceof CustomTreeNode) {
                CustomTreeNode c = (CustomTreeNode) value;
                this.setIcon(this.iconSet.get(c.getIconInfo()));

                if (c.node.details == null || c.node.details.isEmpty()) {
                    this.setToolTipText(null);
                } else {
                    this.setToolTipText(FabricMainWindow.applyWrapping(c.node.details));
                }
            }

            return this;
        }
    }

    private static @NotNull String applyWrapping(@NotNull String str) {
        if (str.indexOf('\n') < 0) {
            return str;
        }

        str = str.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\n", "<br>");

        return "<html>" + str + "</html>";
    }

    static class CustomTreeNode implements TreeNode {
        public final TreeNode parent;
        public final FabricStatusNode node;
        public final List<CustomTreeNode> displayedChildren = new ArrayList<>();
        private IconInfo iconInfo;

        CustomTreeNode(TreeNode parent, @NotNull FabricStatusNode node, FabricTreeWarningLevel minimumWarningLevel) {
            this.parent = parent;
            this.node = node;

            for (FabricStatusNode c : node.children) {
                if (minimumWarningLevel.isHigherThan(c.getMaximumWarningLevel())) {
                    continue;
                }

                this.displayedChildren.add(new CustomTreeNode(this, c, minimumWarningLevel));
            }
        }

        public IconInfo getIconInfo() {
            if (this.iconInfo == null) {
                this.iconInfo = IconInfo.fromNode(this.node);
            }

            return this.iconInfo;
        }

        @Override
        public String toString() {
            return FabricMainWindow.applyWrapping(StringUtil.wrapLines(node.name, 120));
        }

        @Override
        public TreeNode getChildAt(int childIndex) {
            return this.displayedChildren.get(childIndex);
        }

        @Override
        public int getChildCount() {
            return this.displayedChildren.size();
        }

        @Override
        public TreeNode getParent() {
            return this.parent;
        }

        @Override
        public int getIndex(TreeNode node) {
            return this.displayedChildren.indexOf(this.node);
        }

        @Override
        public boolean getAllowsChildren() {
            return !this.isLeaf();
        }

        @Override
        public boolean isLeaf() {
            return this.displayedChildren.isEmpty();
        }

        @Override
        public Enumeration<CustomTreeNode> children() {
            return new Enumeration<CustomTreeNode>() {
                Iterator<CustomTreeNode> it = CustomTreeNode.this.displayedChildren.iterator();

                @Override
                public boolean hasMoreElements() {
                    return this.it.hasNext();
                }

                @Override
                public CustomTreeNode nextElement() {
                    return this.it.next();
                }
            };
        }
    }
}
