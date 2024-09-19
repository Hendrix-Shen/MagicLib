package top.hendrixshen.magiclib.impl.malilib.config.gui.widget;

import fi.dy.masa.malilib.gui.GuiTextFieldGeneric;
import fi.dy.masa.malilib.gui.MaLiLibIcons;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.button.IButtonActionListener;
import fi.dy.masa.malilib.gui.widgets.WidgetConfigOptionBase;
import fi.dy.masa.malilib.render.RenderUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.core.Vec3i;
import top.hendrixshen.magiclib.api.i18n.I18n;
import top.hendrixshen.magiclib.api.malilib.config.option.ConfigVec3iList;

import java.util.Collections;
import java.util.List;

//#if MC > 11904
//$$ import net.minecraft.client.gui.GuiGraphics;
//#elseif MC > 11502
import com.mojang.blaze3d.vertex.PoseStack;
//#endif

public class WidgetVec3iListEditEntry extends WidgetConfigOptionBase<Vec3i> {
    protected final WidgetListVec3iListEdit parent;
    protected final Vec3i defaultValue;
    protected Vec3i initialValue;
    protected Vec3i lastAppliedValue;
    protected final int listIndex;
    protected final boolean isOdd;
    protected final WidgetVec3iEntry vec3iEntry;

    public WidgetVec3iListEditEntry(int x, int y, int width, int height, int listIndex, boolean isOdd, Vec3i initialValue, Vec3i defaultValue, WidgetListVec3iListEdit parent) {
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
            this.vec3iEntry = new WidgetVec3iEntry(x, y, vec3iWidth, height, initialValue, defaultValue);
            this.vec3iEntry.getTextFields().forEach(this.parent::addTextField);
            x += vec3iWidth + 2;
            this.addListActionButton(x, iy, ButtonType.ADD);
            x += 18;
            this.addListActionButton(x, iy, ButtonType.REMOVE);
            x += 18;

            if (this.canBeMoved(true)) {
                this.addListActionButton(x, iy, ButtonType.MOVE_DOWN);
            }

            x += 18;

            if (this.canBeMoved(false)) {
                this.addListActionButton(x, iy, ButtonType.MOVE_UP);
            }
        } else {
            this.vec3iEntry = null;
            this.addListActionButton(x, y + 3, ButtonType.ADD);
        }
    }

    protected boolean isDummy() {
        return this.listIndex < 0;
    }

    protected void addListActionButton(int x, int y, ButtonType type) {
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

        return this.vec3iEntry.wasConfigModified();
    }

    // We don't use the textField of the super, so we need to override it here.
    @Override
    public boolean hasPendingModifications() {
        if (this.isDummy()) {
            return false;
        }

        return !this.vec3iEntry.getVec3iValue().equals(this.vec3iEntry.lastAppliedValue);
    }

    @Override
    public void applyNewValueToConfig() {
        if (!this.isDummy()) {
            ConfigVec3iList config = this.parent.getParent().getConfig();
            List<Vec3i> list = config.getVec3iList();
            Vec3i newValue = this.vec3iEntry.getVec3iValue();

            if (list.size() > this.listIndex) {
                list.set(this.listIndex, newValue);
                this.vec3iEntry.lastAppliedValue = newValue;
            }
        }
    }

    private void insertEntryBefore() {
        List<Vec3i> list = this.parent.getParent().getConfig().getVec3iList();
        int size = list.size();
        int index = this.listIndex < 0 ? size : (Math.min(this.listIndex, size));
        list.add(index, Vec3i.ZERO);
        this.parent.refreshEntries();
        this.parent.markConfigsModified();
    }

    private void removeEntry() {
        List<Vec3i> list = this.parent.getParent().getConfig().getVec3iList();
        int size = list.size();

        if (this.listIndex >= 0 && this.listIndex < size) {
            list.remove(this.listIndex);
            this.parent.refreshEntries();
            this.parent.markConfigsModified();
        }
    }

    private void moveEntry(boolean down) {
        List<Vec3i> list = this.parent.getParent().getConfig().getVec3iList();
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
        int size = this.parent.getParent().getConfig().getVec3iList().size();
        return this.listIndex >= 0 && this.listIndex < size && (down && this.listIndex < size - 1 || !down && this.listIndex > 0);
    }

    @Override
    public void render(
            int mouseX,
            int mouseY,
            boolean selected
            //#if MC > 11904
            //$$ , GuiGraphics poseStackOrGuiGraphics
            //#elseif MC > 11502
            , PoseStack poseStackOrGuiGraphics
            //#endif
    ) {
        RenderUtils.color(1.0F, 1.0F, 1.0F, 1.0F);

        if (this.isOdd) {
            RenderUtils.drawRect(this.x, this.y, this.width, this.height, 0x20FFFFFF);
        } else {
            RenderUtils.drawRect(this.x, this.y, this.width, this.height, 0x30FFFFFF);
        }

        this.drawSubWidgets(
                mouseX,
                mouseY
                //#if MC > 11502
                , poseStackOrGuiGraphics
                //#endif
        );
        this.vec3iEntry.render(
                mouseX,
                mouseY,
                selected
                //#if MC > 11502
                , poseStackOrGuiGraphics
                //#endif
        );
        super.render(
                mouseX,
                mouseY,
                selected
                //#if MC > 11502
                , poseStackOrGuiGraphics
                //#endif
        );
    }

    @Override
    protected boolean onMouseClickedImpl(int mouseX, int mouseY, int mouseButton) {
        if (super.onMouseClickedImpl(mouseX, mouseY, mouseButton)) {
            return true;
        }

        if (this.vec3iEntry == null) {
            return false;
        }

        return this.vec3iEntry.onMouseClickedImpl(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean onKeyTypedImpl(int keyCode, int scanCode, int modifiers) {
        return this.vec3iEntry.onKeyTypedImpl(keyCode, scanCode, modifiers);
    }

    @Override
    protected boolean onCharTypedImpl(char charIn, int modifiers) {
        if (this.vec3iEntry == null) {
            return false;
        }

        return this.vec3iEntry.onCharTypedImpl(charIn, modifiers);
    }

    @AllArgsConstructor
    private enum ButtonType {
        ADD(MaLiLibIcons.PLUS, "malilib.gui.button.hovertext.add"),
        REMOVE(MaLiLibIcons.MINUS, "malilib.gui.button.hovertext.remove"),
        MOVE_UP(MaLiLibIcons.ARROW_UP, "malilib.gui.button.hovertext.move_up"),
        MOVE_DOWN(MaLiLibIcons.ARROW_DOWN, "malilib.gui.button.hovertext.move_down");

        @Getter
        private final MaLiLibIcons icon;
        private final String hoverTextTrKey;

        public String getDisplayName() {
            return I18n.tr(this.hoverTextTrKey);
        }
    }

    @AllArgsConstructor
    private static class ListenerListActions implements IButtonActionListener {
        private final WidgetVec3iListEditEntry.ButtonType type;
        private final WidgetVec3iListEditEntry parent;

        public void actionPerformedWithButton(ButtonBase button, int mouseButton) {
            if (this.type == WidgetVec3iListEditEntry.ButtonType.ADD) {
                this.parent.insertEntryBefore();
            } else if (this.type == WidgetVec3iListEditEntry.ButtonType.REMOVE) {
                this.parent.removeEntry();
            } else {
                this.parent.moveEntry(this.type == WidgetVec3iListEditEntry.ButtonType.MOVE_DOWN);
            }
        }
    }

    protected static class WidgetVec3iEntry extends WidgetVec3iEdit {
        public WidgetVec3iEntry(int x, int y, int width, int height, Vec3i initialValue, Vec3i defaultValue) {
            super(x, y, width, height, initialValue, defaultValue, null);
        }
    }
}
