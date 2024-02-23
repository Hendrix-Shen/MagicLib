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

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.*;
import java.util.function.UnaryOperator;

public final class FabricStatusTree {
    public enum FabricTreeWarningLevel {
        ERROR,
        WARN,
        INFO,
        NONE;

        public final String lowerCaseName = name().toLowerCase(Locale.ROOT);

        public boolean isHigherThan(@NotNull FabricTreeWarningLevel other) {
            return this.ordinal() < other.ordinal();
        }

        public boolean isAtLeast(@NotNull FabricTreeWarningLevel other) {
            return this.ordinal() <= other.ordinal();
        }

        public static FabricTreeWarningLevel getHighest(@NotNull FabricTreeWarningLevel a, FabricTreeWarningLevel b) {
            return a.isHigherThan(b) ? a : b;
        }
    }

    public enum FabricBasicButtonType {
        /**
         * Sends the status message to the main application, then disables itself.
         */
        CLICK_ONCE,
        /**
         * Sends the status message to the main application, remains enabled.
         */
        CLICK_MANY;
    }

    /**
     * No icon is displayed.
     */
    public static final String ICON_TYPE_DEFAULT = "";
    /**
     * Generic folder.
     */
    public static final String ICON_TYPE_FOLDER = "folder";
    /**
     * Generic (unknown contents) file.
     */
    public static final String ICON_TYPE_UNKNOWN_FILE = "file";
    /**
     * Generic non-Fabric jar file.
     */
    public static final String ICON_TYPE_JAR_FILE = "jar";
    /**
     * Generic Fabric-related jar file.
     */
    public static final String ICON_TYPE_FABRIC_JAR_FILE = "jar+fabric";
    /**
     * Something related to Fabric (It's not defined what exactly this is for, but it uses the main Fabric logo).
     */
    public static final String ICON_TYPE_FABRIC = "fabric";
    /**
     * Generic JSON file.
     */
    public static final String ICON_TYPE_JSON = "json";
    /**
     * A file called "fabric.mod.json".
     */
    public static final String ICON_TYPE_FABRIC_JSON = "json+fabric";
    /**
     * Java bytecode class file.
     */
    public static final String ICON_TYPE_JAVA_CLASS = "java_class";
    /**
     * A folder inside of a Java JAR.
     */
    public static final String ICON_TYPE_PACKAGE = "package";
    /**
     * A folder that contains Java class files.
     */
    public static final String ICON_TYPE_JAVA_PACKAGE = "java_package";
    /**
     * A tick symbol, used to indicate that something matched.
     */
    public static final String ICON_TYPE_TICK = "tick";
    /**
     * A cross symbol, used to indicate that something didn't match (although it's not an error). Used as the opposite
     * of {@link #ICON_TYPE_TICK}
     */
    public static final String ICON_TYPE_LESSER_CROSS = "lesser_cross";

    public final String title;
    public final String mainText;
    public final List<FabricStatusTab> tabs = new ArrayList<>();
    public final List<FabricStatusButton> buttons = new ArrayList<>();

    public FabricStatusTree(String title, String mainText) {
        Objects.requireNonNull(title, "null title");
        Objects.requireNonNull(mainText, "null mainText");

        this.title = title;
        this.mainText = mainText;
    }

    public FabricStatusTree(@NotNull DataInputStream is) throws IOException {
        this.title = is.readUTF();
        this.mainText = is.readUTF();

        for (int i = is.readInt(); i > 0; i--) {
            this.tabs.add(new FabricStatusTab(is));
        }

        for (int i = is.readInt(); i > 0; i--) {
            this.buttons.add(new FabricStatusButton(is));
        }
    }

    public void writeTo(@NotNull DataOutputStream os) throws IOException {
        os.writeUTF(this.title);
        os.writeUTF(this.mainText);
        os.writeInt(this.tabs.size());

        for (FabricStatusTab tab : this.tabs) {
            tab.writeTo(os);
        }

        os.writeInt(this.buttons.size());

        for (FabricStatusButton button : this.buttons) {
            button.writeTo(os);
        }
    }

    public @NotNull FabricStatusTab addTab(String name) {
        FabricStatusTab tab = new FabricStatusTab(name);
        this.tabs.add(tab);
        return tab;
    }

    public @NotNull FabricStatusButton addButton(String text, FabricBasicButtonType type) {
        FabricStatusButton button = new FabricStatusButton(text, type);
        this.buttons.add(button);
        return button;
    }

    public static final class FabricStatusButton {
        public final String text;
        public final FabricBasicButtonType type;
        public String clipboard;
        public boolean shouldClose, shouldContinue;

        public FabricStatusButton(String text, FabricBasicButtonType type) {
            Objects.requireNonNull(text, "null text");

            this.text = text;
            this.type = type;
        }

        public FabricStatusButton(@NotNull DataInputStream is) throws IOException {
            this.text = is.readUTF();
            this.type = FabricBasicButtonType.valueOf(is.readUTF());
            this.shouldClose = is.readBoolean();
            this.shouldContinue = is.readBoolean();

            if (is.readBoolean()) {
                this.clipboard = is.readUTF();
            }
        }

        public void writeTo(@NotNull DataOutputStream os) throws IOException {
            os.writeUTF(this.text);
            os.writeUTF(this.type.name());
            os.writeBoolean(this.shouldClose);
            os.writeBoolean(this.shouldContinue);

            if (this.clipboard != null) {
                os.writeBoolean(true);
                os.writeUTF(this.clipboard);
            } else {
                os.writeBoolean(false);
            }
        }

        public FabricStatusButton makeClose() {
            this.shouldClose = true;
            return this;
        }

        public FabricStatusButton makeContinue() {
            this.shouldContinue = true;
            return this;
        }

        public FabricStatusButton withClipboard(String clipboard) {
            this.clipboard = clipboard;
            return this;
        }
    }

    public static final class FabricStatusTab {
        public final FabricStatusNode node;

        /**
         * The minimum warning level to display for this tab.
         */
        public FabricTreeWarningLevel filterLevel = FabricTreeWarningLevel.NONE;

        public FabricStatusTab(String name) {
            this.node = new FabricStatusNode(null, name);
        }

        public FabricStatusTab(DataInputStream is) throws IOException {
            this.node = new FabricStatusNode(null, is);
            this.filterLevel = FabricTreeWarningLevel.valueOf(is.readUTF());
        }

        public void writeTo(DataOutputStream os) throws IOException {
            this.node.writeTo(os);
            os.writeUTF(this.filterLevel.name());
        }

        public @NotNull FabricStatusNode addChild(String name) {
            return this.node.addChild(name);
        }
    }

    public static final class FabricStatusNode {
        private FabricStatusNode parent;
        public String name;
        /**
         * The icon type. There can be a maximum of 2 decorations (added with "+" symbols), or 3 if the
         * {@link #setWarningLevel(FabricTreeWarningLevel) warning level} is set to
         * {@link FabricTreeWarningLevel#NONE }
         */
        public String iconType = ICON_TYPE_DEFAULT;
        private FabricTreeWarningLevel warningLevel = FabricTreeWarningLevel.NONE;
        public boolean expandByDefault = false;
        /**
         * Extra text for more information. Lines should be separated by "\n".
         */
        public String details;
        public final List<FabricStatusNode> children = new ArrayList<>();

        private FabricStatusNode(FabricStatusNode parent, String name) {
            Objects.requireNonNull(name, "null name");

            this.parent = parent;
            this.name = name;
        }

        public FabricStatusNode(FabricStatusNode parent, @NotNull DataInputStream is) throws IOException {
            this.parent = parent;

            this.name = is.readUTF();
            this.iconType = is.readUTF();
            this.warningLevel = FabricTreeWarningLevel.valueOf(is.readUTF());
            this.expandByDefault = is.readBoolean();
            if (is.readBoolean()) this.details = is.readUTF();

            for (int i = is.readInt(); i > 0; i--) {
                this.children.add(new FabricStatusNode(this, is));
            }
        }

        public void writeTo(@NotNull DataOutputStream os) throws IOException {
            os.writeUTF(this.name);
            os.writeUTF(this.iconType);
            os.writeUTF(this.warningLevel.name());
            os.writeBoolean(this.expandByDefault);
            os.writeBoolean(this.details != null);

            if (this.details != null) {
                os.writeUTF(details);
            }

            os.writeInt(this.children.size());

            for (FabricStatusNode child : this.children) {
                child.writeTo(os);
            }
        }

        public void moveTo(@NotNull FabricStatusNode newParent) {
            this.parent.children.remove(this);
            this.parent = newParent;
            newParent.children.add(this);
        }

        public FabricTreeWarningLevel getMaximumWarningLevel() {
            return this.warningLevel;
        }

        public void setWarningLevel(FabricTreeWarningLevel level) {
            if (this.warningLevel == level) {
                return;
            }

            if (this.warningLevel.isHigherThan(level)) {
                // Just because I haven't written the back-fill revalidation for this
                throw new Error("Why would you set the warning level multiple times?");
            } else {
                if (this.parent != null && level.isHigherThan(this.parent.warningLevel)) {
                    this.parent.setWarningLevel(level);
                }

                this.warningLevel = level;
                this.expandByDefault |= level.isAtLeast(FabricTreeWarningLevel.WARN);
            }
        }

        public void setError() {
            setWarningLevel(FabricTreeWarningLevel.ERROR);
        }

        public void setWarning() {
            setWarningLevel(FabricTreeWarningLevel.WARN);
        }

        public void setInfo() {
            setWarningLevel(FabricTreeWarningLevel.INFO);
        }

        private @NotNull FabricStatusNode addChild(@NotNull String string) {
            if (string.startsWith("\t")) {
                if (this.children.isEmpty()) {
                    FabricStatusNode rootChild = new FabricStatusNode(this, "");
                    this.children.add(rootChild);
                }

                FabricStatusNode lastChild = this.children.get(this.children.size() - 1);
                lastChild.addChild(string.substring(1));
                lastChild.expandByDefault = true;
                return lastChild;
            } else {
                FabricStatusNode child = new FabricStatusNode(this, cleanForNode(string));
                this.children.add(child);
                return child;
            }
        }

        private @NotNull String cleanForNode(String string) {
            string = string.trim();

            if (string.length() > 1) {
                if (string.startsWith("-")) {
                    string = string.substring(1);
                    string = string.trim();
                }
            }

            return string;
        }

        public @NotNull FabricStatusNode addMessage(@NotNull String message, FabricTreeWarningLevel warningLevel) {
            String[] lines = message.split("\n");

            FabricStatusNode sub = new FabricStatusNode(this, lines[0]);
            this.children.add(sub);
            sub.setWarningLevel(warningLevel);

            for (int i = 1; i < lines.length; i++) {
                sub.addChild(lines[i]);
            }

            return sub;
        }

        public FabricStatusNode addException(Throwable exception) {
            return FabricStatusNode.addException(this, Collections.newSetFromMap(new IdentityHashMap<>()), exception, UnaryOperator.identity(), new StackTraceElement[0]);
        }

        public FabricStatusNode addCleanedException(Throwable exception) {
            return FabricStatusNode.addException(this, Collections.newSetFromMap(new IdentityHashMap<>()),
                    exception, e -> {
                // Remove some self-repeating exception traces from the tree
                // (for example the RuntimeException that is is created unnecessarily by ForkJoinTask)
                Throwable cause;

                while ((cause = e.getCause()) != null) {
                    if (e.getSuppressed().length > 0) {
                        break;
                    }

                    String msg = e.getMessage();

                    if (msg == null) {
                        msg = e.getClass().getName();
                    }

                    if (!msg.equals(cause.getMessage()) && !msg.equals(cause.toString())) {
                        break;
                    }

                    e = cause;
                }

                return e;
            }, new StackTraceElement[0]);
        }

        private static FabricStatusNode addException(FabricStatusNode node, @NotNull Set<Throwable> seen, Throwable exception, UnaryOperator<Throwable> filter, StackTraceElement[] parentTrace) {
            if (!seen.add(exception)) {
                return node;
            }

            exception = filter.apply(exception);
            FabricStatusNode sub = node.addException(exception, parentTrace);
            StackTraceElement[] trace = exception.getStackTrace();

            for (Throwable t : exception.getSuppressed()) {
                FabricStatusNode suppressed = FabricStatusNode.addException(sub, seen, t, filter, trace);
                suppressed.name += " (suppressed)";
                suppressed.expandByDefault = false;
            }

            if (exception.getCause() != null) {
                FabricStatusNode.addException(sub, seen, exception.getCause(), filter, trace);
            }

            return sub;
        }

        private @NotNull FabricStatusNode addException(@NotNull Throwable exception, StackTraceElement[] parentTrace) {
            boolean showTrace = exception.getCause() != null;
            String msg;

            if (exception.getMessage() == null || exception.getMessage().isEmpty()) {
                msg = exception.toString();
            } else {
                msg = String.format("%s: %s", exception.getClass().getSimpleName(), exception.getMessage());
            }

            FabricStatusNode sub = addMessage(msg, FabricTreeWarningLevel.ERROR);

            if (!showTrace) {
                return sub;
            }

            StackTraceElement[] trace = exception.getStackTrace();
            int uniqueFrames = trace.length - 1;

            for (int i = parentTrace.length - 1; uniqueFrames >= 0 && i >= 0 && trace[uniqueFrames].equals(parentTrace[i]); i--) {
                uniqueFrames--;
            }

            StringJoiner frames = new StringJoiner("\n");
            int inheritedFrames = trace.length - 1 - uniqueFrames;

            for (int i = 0; i <= uniqueFrames; i++) {
                frames.add("at " + trace[i]);
            }

            if (inheritedFrames > 0) {
                frames.add("... " + inheritedFrames + " more");
            }

            sub.addChild(frames.toString()).iconType = ICON_TYPE_JAVA_CLASS;

            StringWriter sw = new StringWriter();
            exception.printStackTrace(new PrintWriter(sw));
            sub.details = sw.toString();

            return sub;
        }

        /**
         * If this node has one child then it merges the child node into this one.
         */
        public void mergeWithSingleChild(String join) {
            if (this.children.size() != 1) {
                return;
            }

            FabricStatusNode child = this.children.remove(0);
            this.name += join + child.name;

            for (FabricStatusNode cc : child.children) {
                cc.parent = this;
                this.children.add(cc);
            }

            child.children.clear();
        }

        public void mergeSingleChildFilePath(String folderType) {
            if (!this.iconType.equals(folderType)) {
                return;
            }

            while (this.children.size() == 1 && this.children.get(0).iconType.equals(folderType)) {
                mergeWithSingleChild("/");
            }

            this.children.sort(Comparator.comparing(a -> a.name));
            mergeChildFilePaths(folderType);
        }

        public void mergeChildFilePaths(String folderType) {
            for (FabricStatusNode node : this.children) {
                node.mergeSingleChildFilePath(folderType);
            }
        }

        public @NotNull FabricStatusNode getFileNode(String file, String folderType, String fileType) {
            FabricStatusNode fileNode = this;

            pathIteration:
            for (String s : file.split("/")) {
                if (s.isEmpty()) {
                    continue;
                }

                for (FabricStatusNode c : fileNode.children) {
                    if (c.name.equals(s)) {
                        fileNode = c;
                        continue pathIteration;
                    }
                }

                if (fileNode.iconType.equals(FabricStatusTree.ICON_TYPE_DEFAULT)) {
                    fileNode.iconType = folderType;
                }

                fileNode = fileNode.addChild(s);
            }

            fileNode.iconType = fileType;
            return fileNode;
        }
    }
}
