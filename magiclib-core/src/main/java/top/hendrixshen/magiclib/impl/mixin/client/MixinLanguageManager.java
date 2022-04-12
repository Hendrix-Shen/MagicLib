package top.hendrixshen.magiclib.impl.mixin.client;

import net.minecraft.client.resources.language.LanguageManager;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.hendrixshen.magiclib.untils.language.ClientLanguage;

@Mixin(LanguageManager.class)
public class MixinLanguageManager {
    @Inject(
            method = "onResourceManagerReload",
            at = @At(
                    value = "RETURN"
            )
    )

    private void onResourceManagerReload(ResourceManager resourceManager, CallbackInfo ci) {
        ClientLanguage.getInstance().load();
    }
}
