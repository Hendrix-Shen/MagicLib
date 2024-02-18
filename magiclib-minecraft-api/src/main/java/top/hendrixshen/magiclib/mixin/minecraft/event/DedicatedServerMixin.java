package top.hendrixshen.magiclib.mixin.minecraft.event;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.dedicated.DedicatedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.hendrixshen.magiclib.impl.event.EventManager;
import top.hendrixshen.magiclib.impl.event.minecraft.DedicatedServerEvent.PostInitEvent;

@Environment(EnvType.SERVER)
@Mixin(DedicatedServer.class)
public class DedicatedServerMixin {
    @Inject(
            method = "initServer",
            at = @At(
                    "TAIL"
            )
    )
    private void postInitServer(CallbackInfoReturnable<Boolean> cir) {
        EventManager.dispatch(new PostInitEvent());
    }
}
