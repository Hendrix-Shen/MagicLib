package top.hendrixshen.magiclib.mixin.util.qol.auth;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Redirect;
import top.hendrixshen.magiclib.dependency.api.annotation.Dependencies;
import top.hendrixshen.magiclib.dependency.impl.MixinDependencyPredicates;

/**
 * The implementation for mc [1.16.5, ~)
 */
@Dependencies(predicate = MixinDependencyPredicates.DevMixinPredicate.class)
@Environment(EnvType.CLIENT)
@Mixin(Minecraft.class)
public class MixinMinecraft {
    @Redirect(
            //#if MC > 11701
            method = "createUserApiService",
            //#else
            //$$ method = "createSocialInteractions",
            //#endif
            at = @At(
                    value = "INVOKE",
                    //#if MC > 11701
                    target = "Lorg/slf4j/Logger;error(Ljava/lang/String;Ljava/lang/Throwable;)V",
                    //#else
                    //$$ target = "Lorg/apache/logging/log4j/Logger;error(Ljava/lang/String;Ljava/lang/Throwable;)V",
                    //#endif
                    remap = false
            ),
            require = 0
    )
    private void dontThrowAuthFailureInDevEnv(@Coerce Object logger, String s, Throwable throwable) {
        // NO-OP
    }
}
