package top.hendrixshen.magiclib.mixin.event.minecraft;

import net.minecraft.client.resources.language.Language;
import net.minecraft.client.resources.language.LanguageManager;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.hendrixshen.magiclib.impl.event.EventManager;
import top.hendrixshen.magiclib.impl.event.minecraft.LanguageReloadEvent;
import top.hendrixshen.magiclib.impl.event.minecraft.LanguageSelectEvent;

@Mixin(LanguageManager.class)
public class LanguageManagerMixin {
    @Inject(
            method = "onResourceManagerReload",
            at = @At(
                    value = "RETURN"
            )
    )
    private void postOnResourceManagerReload(ResourceManager resourceManager, CallbackInfo ci) {
        EventManager.dispatch(new LanguageReloadEvent());
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
        EventManager.dispatch(new LanguageSelectEvent());
    }
}
