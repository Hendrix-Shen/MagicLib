package top.hendrixshen.magiclib.mixin.malilib.config;

import fi.dy.masa.malilib.config.options.ConfigBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.hendrixshen.magiclib.api.malilib.config.option.MagicIConfigBase;
import top.hendrixshen.magiclib.impl.malilib.config.GlobalConfigManager;
import top.hendrixshen.magiclib.util.MiscUtil;

//#if MC > 11701
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.ModifyArg;
//#endif

/**
 * Reference to <a href="https://github.com/Fallen-Breath/TweakerMore">TweakerMore</a>
 */
@Mixin(value = ConfigBase.class, remap = false)
public class ConfigBaseMixin {
    //#if MC > 11701
    @Shadow
    private String comment;

    @ModifyArg(
            method = "getComment",
            at = @At(
                    value = "INVOKE",
                    target = "Lfi/dy/masa/malilib/util/StringUtils;getTranslatedOrFallback(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;"
            ),
            index = 0
    )
    private String magicConfigCommentIsTheTranslationKey(String key) {
        if (MiscUtil.cast(this) instanceof MagicIConfigBase) {
            key = this.comment;
        }

        return key;
    }
    //#endif

    @Inject(
            method = "getComment",
            at = @At(
                    value = "TAIL"
            ),
            cancellable = true
    )
    private void appendComment(CallbackInfoReturnable<String> cir) {
        if (!(MiscUtil.cast(this) instanceof MagicIConfigBase)) {
            return;
        }

        GlobalConfigManager.getInstance().getContainerByConfig((MagicIConfigBase) this)
                .ifPresent(configContainer -> cir.setReturnValue(configContainer.modifyComment(cir.getReturnValue())));
    }
}
