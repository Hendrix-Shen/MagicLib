package top.hendrixshen.magiclib.mixin.language;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.hendrixshen.magiclib.language.impl.MagicLanguageManager;

@Environment(EnvType.CLIENT)
@Mixin(Minecraft.class)
public class MixinMinecraft {
    @Inject(
            //#if MC > 11404
            method = "<init>",
            //#else
            //$$ method = "init",
            //#endif
            at = @At(
                    value = "RETURN"
            )
    )
    //#if MC > 11404
    private void afterInit(GameConfig gameConfig, CallbackInfo ci) {
    //#else
    //$$ private void afterInit(CallbackInfo ci) {
    //#endif
        MagicLanguageManager.INSTANCE.initClient();
        Minecraft.getInstance().getLanguageManager().onResourceManagerReload(Minecraft.getInstance().getResourceManager());
    }
}
