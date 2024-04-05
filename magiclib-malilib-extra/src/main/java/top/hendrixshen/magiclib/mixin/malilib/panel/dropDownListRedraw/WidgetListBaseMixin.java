package top.hendrixshen.magiclib.mixin.malilib.panel.dropDownListRedraw;

import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.widgets.WidgetListBase;
import fi.dy.masa.malilib.gui.widgets.WidgetListConfigOptions;
import fi.dy.masa.malilib.gui.widgets.WidgetListEntryBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.hendrixshen.magiclib.impl.malilib.config.gui.MagicConfigGui;
import top.hendrixshen.magiclib.mixin.malilib.accessor.WidgetListConfigOptionsAccessor;
import top.hendrixshen.magiclib.util.MiscUtil;

//#if MC > 11904
//$$ import net.minecraft.client.gui.GuiGraphics;
//#elseif MC > 11502
//$$ import com.mojang.blaze3d.vertex.PoseStack;
//#endif

/**
 * Reference to <a href="https://github.com/Fallen-Breath/TweakerMore">TweakerMore</a>
 */
@Mixin(value = WidgetListBase.class, remap = false)
public abstract class WidgetListBaseMixin<TYPE, WIDGET extends WidgetListEntryBase<TYPE>> {
    // To make sure it only once gets rendered
    @Unique
    private boolean magiclib$shouldRenderMagicConfigGuiDropDownList = false;

    @SuppressWarnings("ConstantConditions")
    @Unique
    private boolean magiclib$isMagicConfigGui() {
        if (MiscUtil.cast(this) instanceof WidgetListConfigOptions) {
            GuiConfigsBase guiConfig = ((WidgetListConfigOptionsAccessor) this).magiclib$getParent();
            return guiConfig instanceof MagicConfigGui;
        }

        return false;
    }

    @Unique
    private void magiclib$drawMagicConfigGuiDropDownListAgain(
            //#if MC > 11904
            //$$ GuiGraphics poseStackOrGuiGraphics,
            //#elseif MC > 11502
            //$$ PoseStack poseStackOrGuiGraphics,
            //#endif
            int mouseX,
            int mouseY
    ) {
        if (this.magiclib$isMagicConfigGui() && this.magiclib$shouldRenderMagicConfigGuiDropDownList) {
            GuiConfigsBase guiConfig = ((WidgetListConfigOptionsAccessor) this).magiclib$getParent();

            // Render it again to make sure it's on the top but below hovering widgets.
            ((MagicConfigGui) guiConfig).renderDropDownList(
                    //#if MC > 11600
                    //$$ poseStackOrGuiGraphics,
                    //#endif
                    mouseX,
                    mouseY
            );

            this.magiclib$shouldRenderMagicConfigGuiDropDownList = false;
        }
    }

    @Inject(
            method = "drawContents",
            at = @At(
                    "HEAD"
            )
    )
    private void drawMagicConfigGuiDropDownListSetFlag(CallbackInfo ci) {
        this.magiclib$shouldRenderMagicConfigGuiDropDownList = true;
    }

    //#if MC < 11904
    @Inject(
            method = "drawContents",
            at = @At(
                    value = "INVOKE",
                    //#if MC > 11502
                    //$$ target = "Lfi/dy/masa/malilib/gui/widgets/WidgetBase;postRenderHovered(IIZLcom/mojang/blaze3d/vertex/PoseStack;)V",
                    //$$ remap = true
                    //#else
                    target = "Lfi/dy/masa/malilib/gui/widgets/WidgetBase;postRenderHovered(IIZ)V"
                    //#endif
            )
    )
    private void drawMagicConfigGuiDropDownListAgainBeforeHover(
            //#if MC > 11502
            //$$ PoseStack poseStack,
            //#endif
            int mouseX,
            int mouseY,
            float partialTicks,
            CallbackInfo ci
    ) {
        this.magiclib$drawMagicConfigGuiDropDownListAgain(
                //#if MC > 11502
                //$$ poseStack,
                //#endif
                mouseX,
                mouseY
        );
    }
    //#endif

    @Inject(
            method = "drawContents",
            at = @At(
                    value = "TAIL"
            )
    )
    private void drawMagicConfigGuiDropDownListAgainAfterHover(
            //#if MC > 11904
            //$$ GuiGraphics poseStackOrGuiGraphics,
            //#elseif MC > 11502
            //$$ PoseStack poseStackOrGuiGraphics,
            //#endif
            int mouseX,
            int mouseY,
            float partialTicks,
            CallbackInfo ci
    ) {
        this.magiclib$drawMagicConfigGuiDropDownListAgain(
                //#if MC > 11502
                //$$ poseStackOrGuiGraphics,
                //#endif
                mouseX,
                mouseY
        );
    }
}
