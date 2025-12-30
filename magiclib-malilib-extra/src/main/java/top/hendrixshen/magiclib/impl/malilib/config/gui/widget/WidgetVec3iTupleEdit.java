package top.hendrixshen.magiclib.impl.malilib.config.gui.widget;

import fi.dy.masa.malilib.gui.widgets.WidgetContainer;

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

import java.util.function.BiConsumer;

public class WidgetVec3iTupleEdit extends WidgetContainer {
    protected final WidgetVec3iEditEntry firstVec3iEdit;
    protected final WidgetVec3iEditEntry secondVec3iEdit;
    protected final BiConsumer<Vec3i, Vec3i> valueApplier;

    public WidgetVec3iTupleEdit(int x, int y, int width, int height,
                                Vec3i initialFirstValue, Vec3i defaultFirstValue,
                                Vec3i initialSecondValue, Vec3i defaultSecondValue,
                                BiConsumer<Vec3i, Vec3i> valueApplier) {
        super(x, y, width, height);
        this.firstVec3iEdit = new WidgetVec3iEditEntry(x, y, width, height, initialFirstValue, defaultFirstValue, 1);
        this.secondVec3iEdit = new WidgetVec3iEditEntry(x, y + 22, width, height, initialSecondValue, defaultSecondValue, 2);
        this.valueApplier = valueApplier;
    }

    public boolean wasConfigModified() {
        return this.firstVec3iEdit.wasConfigModified() || this.secondVec3iEdit.wasConfigModified();
    }

    public void applyNewValueToConfig() {
        Vec3i newFirstValue = this.firstVec3iEdit.getVec3iValue();
        Vec3i newSecondValue = this.secondVec3iEdit.getVec3iValue();

        if (this.valueApplier != null) {
            this.valueApplier.accept(newFirstValue, newSecondValue);
        }

        this.firstVec3iEdit.lastAppliedValue = newFirstValue;
        this.secondVec3iEdit.lastAppliedValue = newSecondValue;
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
    //$$     this.firstVec3iEdit.render(guiGraphics, mouseX, mouseY, selected);
    //$$     this.secondVec3iEdit.render(guiGraphics, mouseX, mouseY, selected);
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
        this.firstVec3iEdit.render(
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
        this.secondVec3iEdit.render(
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
    //$$ protected boolean onMouseClickedImpl(MouseButtonEvent click, boolean doubleClick) {
    //$$     boolean ret = false;
    //$$     ret |= this.firstVec3iEdit.onMouseClickedImpl(click, doubleClick);
    //$$     ret |= this.secondVec3iEdit.onMouseClickedImpl(click, doubleClick);
    //$$     return ret;
    //$$ }
    //$$
    //$$ @Override
    //$$ protected boolean onKeyTypedImpl(KeyEvent input) {
    //$$     return this.firstVec3iEdit.onKeyTypedImpl(input)
    //$$             || this.secondVec3iEdit.onKeyTypedImpl(input);
    //$$ }
    //$$
    //$$ @Override
    //$$ protected boolean onCharTypedImpl(CharacterEvent input) {
    //$$     return this.firstVec3iEdit.onCharTypedImpl(input)
    //$$             || this.secondVec3iEdit.onCharTypedImpl(input);
    //$$ }
    //#else
    @Override
    protected boolean onMouseClickedImpl(int mouseX, int mouseY, int mouseButton) {
        boolean ret = false;
        ret |= this.firstVec3iEdit.onMouseClickedImpl(mouseX, mouseY, mouseButton);
        ret |= this.secondVec3iEdit.onMouseClickedImpl(mouseX, mouseY, mouseButton);
        return ret;
    }

    @Override
    public boolean onKeyTypedImpl(int keyCode, int scanCode, int modifiers) {
        return this.firstVec3iEdit.onKeyTypedImpl(keyCode, scanCode, modifiers)
                || this.secondVec3iEdit.onKeyTypedImpl(keyCode, scanCode, modifiers);
    }

    @Override
    protected boolean onCharTypedImpl(char charIn, int modifiers) {
        return this.firstVec3iEdit.onCharTypedImpl(charIn, modifiers)
                || this.secondVec3iEdit.onCharTypedImpl(charIn, modifiers);
    }
    //#endif

    protected static class WidgetVec3iEditEntry extends WidgetVec3iEdit {
        public WidgetVec3iEditEntry(int x, int y, int width, int height, Vec3i initialValue, Vec3i defaultValue, int num) {
            super(x, y, width, height, initialValue, defaultValue, null,
                    "x" + num + ":", "y" + num + ":", "z" + num + ":");
        }
    }
}
