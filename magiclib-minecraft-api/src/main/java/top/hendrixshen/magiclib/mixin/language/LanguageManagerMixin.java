package top.hendrixshen.magiclib.mixin.language;

import net.minecraft.client.resources.language.Language;
import net.minecraft.client.resources.language.LanguageManager;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.hendrixshen.magiclib.impl.i18n.minecraft.MinecraftLanguageManager;

@Mixin(LanguageManager.class)
public class LanguageManagerMixin {
    @Inject(
            method = "onResourceManagerReload",
            at = @At(
                    value = "RETURN"
            )
    )
    private void postOnResourceManagerReload(ResourceManager resourceManager, CallbackInfo ci) {
        MinecraftLanguageManager.reload();
    }

    @Inject(
            method = "setSelected",
            at = @At(
                    value = "RETURN"
            )
    )
    private void postSetSelected(
            //#if MC > 11903
            //$$ String languageCode,
            //#else
            Language language,
            //#endif
            CallbackInfo ci
    ) {
        MinecraftLanguageManager.updateLanguage();
    }
}
