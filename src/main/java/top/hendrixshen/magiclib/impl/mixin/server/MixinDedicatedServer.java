package top.hendrixshen.magiclib.impl.mixin.server;

import net.minecraft.server.dedicated.DedicatedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.hendrixshen.magiclib.untils.language.ServerLanguage;

@Mixin(DedicatedServer.class)
public class MixinDedicatedServer {
    @Inject(
            method = "initServer",
            at = @At(
                    value = "HEAD"
            )
    )
    private void onInitServer(CallbackInfoReturnable<Boolean> cir) {
        ServerLanguage.getInstance().load();
    }
}
