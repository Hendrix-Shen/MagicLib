package top.hendrixshen.magiclib.mixin.language;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.hendrixshen.magiclib.impl.i18n.MagicLanguageManager;
import top.hendrixshen.magiclib.impl.i18n.minecraft.MinecraftLanguageManager;
import top.hendrixshen.magiclib.impl.i18n.minecraft.ResourceLanguageProvider;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Inject(
            //#if MC > 11404
            //$$ method = "<init>",
            //#else
            method = "init",
            //#endif
            at = @At(
                    value = "RETURN"
            )
    )
    private void postInit(CallbackInfo ci) {
        MinecraftLanguageManager.updateLanguage();
        MagicLanguageManager.getInstance().registerLanguageProvider(ResourceLanguageProvider.getInstance());
    }
}
