package top.hendrixshen.magiclib.impl.mixin.client.language;

import net.minecraft.client.resources.language.LanguageInfo;
import net.minecraft.client.resources.language.LanguageManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.hendrixshen.magiclib.language.MagicLanguageManager;

@Mixin(LanguageManager.class)
public class MixinLanguageManager {
    @Shadow
    private String currentCode;

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    private void postInit(String string, CallbackInfo ci) {
        MagicLanguageManager.INSTANCE.setCurrentCode(this.currentCode);
    }

    @Inject(method = "setSelected", at = @At(value = "RETURN"))
    private void postSetSelected(LanguageInfo languageInfo, CallbackInfo ci) {
        MagicLanguageManager.INSTANCE.setCurrentCode(this.currentCode);
    }
}
