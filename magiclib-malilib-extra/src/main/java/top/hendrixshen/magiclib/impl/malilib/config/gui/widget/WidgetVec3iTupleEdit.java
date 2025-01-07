package top.hendrixshen.magiclib.impl.malilib.config.gui.widget;

import fi.dy.masa.malilib.gui.widgets.WidgetContainer;
import net.minecraft.core.Vec3i;

import java.util.function.BiConsumer;

//#if MC > 11904
//$$ import net.minecraft.client.gui.GuiGraphics;
//#elseif MC > 11502
import com.mojang.blaze3d.vertex.PoseStack;
//#endif

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

    protected static class WidgetVec3iEditEntry extends WidgetVec3iEdit {
        public WidgetVec3iEditEntry(int x, int y, int width, int height, Vec3i initialValue, Vec3i defaultValue, int num) {
            super(x, y, width, height, initialValue, defaultValue, null,
                    "x" + num + ":", "y" + num + ":", "z" + num + ":");
        }
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
        this.firstVec3iEdit.render(
                mouseX,
                mouseY,
                selected
                //#if MC > 11502
                , poseStackOrGuiGraphics
                //#endif
        );
        this.secondVec3iEdit.render(
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
        boolean ret = false;
        ret |= this.firstVec3iEdit.onMouseClickedImpl(mouseX, mouseY, mouseButton);
        ret |= this.secondVec3iEdit.onMouseClickedImpl(mouseX, mouseY, mouseButton);
        return ret;
    }

    @Override
    public boolean onKeyTypedImpl(int keyCode, int scanCode, int modifiers) {
        return this.firstVec3iEdit.onKeyTypedImpl(keyCode, scanCode, modifiers) ||
                this.secondVec3iEdit.onKeyTypedImpl(keyCode, scanCode, modifiers);
    }

    @Override
    protected boolean onCharTypedImpl(char charIn, int modifiers) {
        return this.firstVec3iEdit.onCharTypedImpl(charIn, modifiers) ||
                this.secondVec3iEdit.onCharTypedImpl(charIn, modifiers);
    }
}
