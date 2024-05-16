package top.hendrixshen.magiclib.mixin.minecraft.event;

import net.minecraft.client.resources.language.LanguageManager;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.hendrixshen.magiclib.impl.event.EventManager;
import top.hendrixshen.magiclib.impl.event.minecraft.LanguageManagerEvent.LanguageReloadEvent;
import top.hendrixshen.magiclib.impl.event.minecraft.LanguageManagerEvent.LanguageSelectEvent;

//#if MC < 11904
import net.minecraft.client.resources.language.LanguageInfo;
//#endif

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
            LanguageInfo language,
            //#endif
            CallbackInfo ci
    ) {
        EventManager.dispatch(new LanguageSelectEvent());
    }
}
