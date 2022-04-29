package top.hendrixshen.magiclib.impl.mixin.client.language;

import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.hendrixshen.magiclib.language.MagicLanguageManager;

@Mixin(Minecraft.class)
public class MixinMinecraft {
    //#if MC > 11404
    @Inject(method = "<init>", at = @At(value = "RETURN"))
    public void postClientInit(GameConfig gameConfig, CallbackInfo ci) {
        MagicLanguageManager.INSTANCE.initClient();
    }

    //#else
    //$$ @Inject(method = "init", at = @At(value = "RETURN"))
    //$$ public void postClientInit(CallbackInfo ci) {
    //$$     MagicLanguageManager.INSTANCE.initClient();
    //$$ }
    //#endif
}
