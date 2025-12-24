package top.hendrixshen.magiclib.impl.malilib.config.gui.widget;

import fi.dy.masa.malilib.gui.GuiTextFieldGeneric;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import fi.dy.masa.malilib.gui.widgets.WidgetConfigOptionBase;
import fi.dy.masa.malilib.render.RenderUtils;
import lombok.AllArgsConstructor;

// CHECKSTYLE.OFF: ImportOrder
//#if MC >= 12111
//$$ import fi.dy.masa.malilib.render.GuiContext;
//#endif
// CHECKSTYLE.ON: ImportOrder

import net.minecraft.core.Vec3i;

// CHECKSTYLE.OFF: ImportOrder
//#if MC >= 12109
//$$ import net.minecraft.client.input.CharacterEvent;
//$$ import net.minecraft.client.input.KeyEvent;
//$$ import net.minecraft.client.input.MouseButtonEvent;
//#endif

//#if MC >= 12111
//#elseif MC > 11904
//$$ import net.minecraft.client.gui.GuiGraphics;
//#elseif MC > 11502
import com.mojang.blaze3d.vertex.PoseStack;
//#endif
// CHECKSTYLE.ON: ImportOrder

import top.hendrixshen.magiclib.api.malilib.config.option.ConfigVec3iTupleList;
import top.hendrixshen.magiclib.impl.malilib.config.gui.button.ListEditEntryButtonType;

import java.util.Collections;
import java.util.List;

public class WidgetVec3iTupleListEditEntry extends WidgetConfigOptionBase<ConfigVec3iTupleList.Entry> {
    protected final WidgetListVec3iTupleListEdit parent;
    protected final ConfigVec3iTupleList.Entry defaultValue;
    protected ConfigVec3iTupleList.Entry initialValue;
    protected ConfigVec3iTupleList.Entry lastAppliedValue;
    protected final int listIndex;
    protected final boolean isOdd;
    protected final WidgetVec3iEntry firstVec3iEntry;
    protected final WidgetVec3iEntry secondVec3iEntry;

    public WidgetVec3iTupleListEditEntry(int x, int y, int width, int height, int listIndex, boolean isOdd,
                                         ConfigVec3iTupleList.Entry initialValue, ConfigVec3iTupleList.Entry defaultValue,
                                         WidgetListVec3iTupleListEdit parent) {
        super(x, y, width, height, parent, initialValue, listIndex);
        this.listIndex = listIndex;
        this.isOdd = isOdd;
        this.defaultValue = defaultValue;
        this.initialValue = initialValue;
        this.lastAppliedValue = initialValue;
        this.parent = parent;
        y += 1;
        int iy = y + 3;

        if (!this.isDummy()) {
            this.addLabel(x + 2, y, 20, 20, 0xC0C0C0C0, String.format("%3d:", listIndex + 1));
            x += 20;
            int vec3iWidth = width - 120;
            this.firstVec3iEntry = new WidgetVec3iEntry(x, y, vec3iWidth, height, initialValue.getFirstVec3i(), defaultValue.getFirstVec3i(), 1);
            this.firstVec3iEntry.getTextFields().forEach(this.parent::addTextField);
            this.secondVec3iEntry = new WidgetVec3iEntry(x, y + 22, vec3iWidth, height, initialValue.getSecondVec3i(), defaultValue.getSecondVec3i(), 2);
            this.secondVec3iEntry.getTextFields().forEach(this.parent::addTextField);
            x += vec3iWidth + 2;
            this.addListActionButton(x, iy, ListEditEntryButtonType.ADD);
            x += 18;
            this.addListActionButton(x, iy, ListEditEntryButtonType.REMOVE);
            x += 18;

            if (this.canBeMoved(true)) {
                this.addListActionButton(x, iy, ListEditEntryButtonType.MOVE_DOWN);
            }

            x += 18;

            if (this.canBeMoved(false)) {
                this.addListActionButton(x, iy, ListEditEntryButtonType.MOVE_UP);
            }
        } else {
            this.firstVec3iEntry = null;
            this.secondVec3iEntry = null;
            this.addListActionButton(x, y + 3, ListEditEntryButtonType.ADD);
        }
    }

    protected boolean isDummy() {
        return this.listIndex < 0;
    }

    protected void addListActionButton(int x, int y, ListEditEntryButtonType type) {
        ButtonGeneric button = new ButtonGeneric(x, y, type.getIcon(), type.getDisplayName());
        ListenerListActions listener = new ListenerListActions(type, this);
        this.addButton(button, listener);
    }

    @Override
    protected GuiTextFieldGeneric createTextField(int x, int y, int width, int height) {
        return null;
    }

    @Override
    public boolean wasConfigModified() {
        if (this.isDummy()) {
            return false;
        }

        return this.firstVec3iEntry.wasConfigModified() || this.secondVec3iEntry.wasConfigModified();
    }

    // We don't use the textField of the super, so we need to override it here.
    @Override
    public boolean hasPendingModifications() {
        if (this.isDummy()) {
            return false;
        }

        return !this.firstVec3iEntry.getVec3iValue().equals(this.firstVec3iEntry.lastAppliedValue)
                || !this.secondVec3iEntry.getVec3iValue().equals(this.secondVec3iEntry.lastAppliedValue);
    }

    @Override
    public void applyNewValueToConfig() {
        if (!this.isDummy()) {
            ConfigVec3iTupleList config = this.parent.getParent().getConfig();
            List<ConfigVec3iTupleList.Entry> list = config.getVec3iTupleList();
            Vec3i newFirstValue = this.firstVec3iEntry.getVec3iValue();
            Vec3i newSecondValue = this.secondVec3iEntry.getVec3iValue();
            ConfigVec3iTupleList.Entry newValue = new ConfigVec3iTupleList.Entry(newFirstValue, newSecondValue);

            if (list.size() > this.listIndex) {
                list.set(this.listIndex, newValue);
                this.firstVec3iEntry.lastAppliedValue = newFirstValue;
                this.secondVec3iEntry.lastAppliedValue = newSecondValue;
            }
        }
    }

    private void insertEntryBefore() {
        List<ConfigVec3iTupleList.Entry> list = this.parent.getParent().getConfig().getVec3iTupleList();
        int size = list.size();
        int index = this.listIndex < 0 ? size : (Math.min(this.listIndex, size));
        list.add(index, ConfigVec3iTupleList.Entry.ZERO);
        this.parent.refreshEntries();
        this.parent.markConfigsModified();
    }

    private void removeEntry() {
        List<ConfigVec3iTupleList.Entry> list = this.parent.getParent().getConfig().getVec3iTupleList();
        int size = list.size();

        if (this.listIndex >= 0 && this.listIndex < size) {
            list.remove(this.listIndex);
            this.parent.refreshEntries();
            this.parent.markConfigsModified();
        }
    }

    private void moveEntry(boolean down) {
        List<ConfigVec3iTupleList.Entry> list = this.parent.getParent().getConfig().getVec3iTupleList();
        int size = list.size();

        if (this.listIndex >= 0 && this.listIndex < size) {
            int index1 = this.listIndex;
            int index2 = -1;

            if (down && this.listIndex < size - 1) {
                index2 = index1 + 1;
            } else if (!down && this.listIndex > 0) {
                index2 = index1 - 1;
            }

            if (index2 >= 0) {
                this.parent.markConfigsModified();
                this.parent.applyPendingModifications();
                Collections.swap(list, index1, index2);
                this.parent.refreshEntries();
            }
        }
    }

    private boolean canBeMoved(boolean down) {
        int size = this.parent.getParent().getConfig().getVec3iTupleList().size();
        return this.listIndex >= 0 && this.listIndex < size && (down && this.listIndex < size - 1 || !down && this.listIndex > 0);
    }

    @Override
    //#if MC >= 12106
    //$$ public void render(
    //$$         //#if MC >= 12111
    //$$         //$$ GuiContext guiGraphics,
    //$$         //#else
    //$$         GuiGraphics guiGraphics,
    //$$         //#endif
    //$$         int mouseX,
    //$$         int mouseY,
    //$$         boolean selected
    //$$ ) {
    //$$     if (this.isOdd) {
    //$$         RenderUtils.drawRect(guiGraphics, this.x, this.y, this.width, this.height, 0x20FFFFFF);
    //$$     } else {
    //$$         RenderUtils.drawRect(guiGraphics, this.x, this.y, this.width, this.height, 0x30FFFFFF);
    //$$     }
    //$$
    //$$     this.drawSubWidgets(guiGraphics, mouseX, mouseY);
    //$$
    //$$     if (this.firstVec3iEntry != null && this.secondVec3iEntry != null) {
    //$$         this.firstVec3iEntry.render(guiGraphics, mouseX, mouseY, selected);
    //$$         this.secondVec3iEntry.render(guiGraphics, mouseX, mouseY, selected);
    //$$     }
    //$$
    //$$     super.render(guiGraphics, mouseX, mouseY, selected);
    //$$ }
    //#else
    public void render(
            // CHECKSTYLE.OFF: NoWhitespaceBefore
            // CHECKSTYLE.OFF: SeparatorWrap
            int mouseX,
            int mouseY,
            boolean selected
            //#if MC > 11904
            //$$ , GuiGraphics poseStackOrGuiGraphics
            //#elseif MC > 11502
            , PoseStack poseStackOrGuiGraphics
            //#endif
            // CHECKSTYLE.ON: SeparatorWrap
            // CHECKSTYLE.ON: NoWhitespaceBefore
    ) {
        RenderUtils.color(1.0F, 1.0F, 1.0F, 1.0F);

        if (this.isOdd) {
            RenderUtils.drawRect(this.x, this.y, this.width, this.height, 0x20FFFFFF);
        } else {
            RenderUtils.drawRect(this.x, this.y, this.width, this.height, 0x30FFFFFF);
        }

        this.drawSubWidgets(
                // CHECKSTYLE.OFF: NoWhitespaceBefore
                // CHECKSTYLE.OFF: SeparatorWrap
                mouseX,
                mouseY
                //#if MC > 11502
                , poseStackOrGuiGraphics
                //#endif
                // CHECKSTYLE.ON: SeparatorWrap
                // CHECKSTYLE.ON: NoWhitespaceBefore
        );

        if (this.firstVec3iEntry != null && this.secondVec3iEntry != null) {
            this.firstVec3iEntry.render(
                    // CHECKSTYLE.OFF: NoWhitespaceBefore
                    // CHECKSTYLE.OFF: SeparatorWrap
                    mouseX,
                    mouseY,
                    selected
                    //#if MC > 11502
                    , poseStackOrGuiGraphics
                    //#endif
                    // CHECKSTYLE.ON: SeparatorWrap
                    // CHECKSTYLE.ON: NoWhitespaceBefore
            );
            this.secondVec3iEntry.render(
                    // CHECKSTYLE.OFF: NoWhitespaceBefore
                    // CHECKSTYLE.OFF: SeparatorWrap
                    mouseX,
                    mouseY,
                    selected
                    //#if MC > 11502
                    , poseStackOrGuiGraphics
                    //#endif
                    // CHECKSTYLE.ON: SeparatorWrap
                    // CHECKSTYLE.ON: NoWhitespaceBefore
            );
        }

        super.render(
                // CHECKSTYLE.OFF: NoWhitespaceBefore
                // CHECKSTYLE.OFF: SeparatorWrap
                mouseX,
                mouseY,
                selected
                //#if MC > 11502
                , poseStackOrGuiGraphics
                //#endif
                // CHECKSTYLE.ON: SeparatorWrap
                // CHECKSTYLE.ON: NoWhitespaceBefore
        );
    }
    //#endif

    //#if MC >= 12109
    //$$ @Override
    //$$ protected boolean onMouseClickedImpl(MouseButtonEvent input, boolean doubleClick) {
    //$$     if (super.onMouseClickedImpl(input, doubleClick)) {
    //$$         return true;
    //$$     }
    //$$
    //$$     if (this.firstVec3iEntry == null || this.secondVec3iEntry == null) {
    //$$         return false;
    //$$     }
    //$$
    //$$     return this.firstVec3iEntry.onMouseClickedImpl(input, doubleClick)
    //$$             || this.secondVec3iEntry.onMouseClickedImpl(input, doubleClick);
    //$$ }
    //$$
    //$$ @Override
    //$$ public boolean onKeyTypedImpl(KeyEvent input) {
    //$$     if (this.firstVec3iEntry == null || this.secondVec3iEntry == null) {
    //$$         return false;
    //$$     }
    //$$
    //$$     return this.firstVec3iEntry.onKeyTypedImpl(input)
    //$$             || this.secondVec3iEntry.onKeyTypedImpl(input);
    //$$ }
    //$$
    //$$ @Override
    //$$ protected boolean onCharTypedImpl(CharacterEvent input) {
    //$$     if (this.firstVec3iEntry == null || this.secondVec3iEntry == null) {
    //$$         return false;
    //$$     }
    //$$
    //$$     return this.firstVec3iEntry.onCharTypedImpl(input)
    //$$             || this.secondVec3iEntry.onCharTypedImpl(input);
    //$$ }
    //#else
    @Override
    protected boolean onMouseClickedImpl(int mouseX, int mouseY, int mouseButton) {
        if (super.onMouseClickedImpl(mouseX, mouseY, mouseButton)) {
            return true;
        }

        if (this.firstVec3iEntry == null || this.secondVec3iEntry == null) {
            return false;
        }

        return this.firstVec3iEntry.onMouseClickedImpl(mouseX, mouseY, mouseButton)
                || this.secondVec3iEntry.onMouseClickedImpl(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean onKeyTypedImpl(int keyCode, int scanCode, int modifiers) {
        if (this.firstVec3iEntry == null || this.secondVec3iEntry == null) {
            return false;
        }

        return this.firstVec3iEntry.onKeyTypedImpl(keyCode, scanCode, modifiers)
                || this.secondVec3iEntry.onKeyTypedImpl(keyCode, scanCode, modifiers);
    }

    @Override
    protected boolean onCharTypedImpl(char charIn, int modifiers) {
        if (this.firstVec3iEntry == null || this.secondVec3iEntry == null) {
            return false;
        }

        return this.firstVec3iEntry.onCharTypedImpl(charIn, modifiers)
                || this.secondVec3iEntry.onCharTypedImpl(charIn, modifiers);
    }
    //#endif

    @AllArgsConstructor
    private static class ListenerListActions implements IButtonActionListener {
        private final ListEditEntryButtonType type;
        private final WidgetVec3iTupleListEditEntry parent;

        public void actionPerformedWithButton(ButtonBase button, int mouseButton) {
            if (this.type == ListEditEntryButtonType.ADD) {
                this.parent.insertEntryBefore();
            } else if (this.type == ListEditEntryButtonType.REMOVE) {
                this.parent.removeEntry();
            } else {
                this.parent.moveEntry(this.type == ListEditEntryButtonType.MOVE_DOWN);
            }
        }
    }

    protected static class WidgetVec3iEntry extends WidgetVec3iEdit {
        public WidgetVec3iEntry(int x, int y, int width, int height, Vec3i initialValue, Vec3i defaultValue, int num) {
            super(x, y, width, height, initialValue, defaultValue, null,
                    "x" + num + ":", "y" + num + ":", "z" + num + ":");
        }
    }
}
