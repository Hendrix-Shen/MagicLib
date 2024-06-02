package top.hendrixshen.magiclib.mixin.minecraft.event;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.hendrixshen.magiclib.impl.event.EventManager;
import top.hendrixshen.magiclib.impl.event.minecraft.MinecraftServerEvent;
import top.hendrixshen.magiclib.util.MiscUtil;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Inject(
            method = "loadLevel",
            at = @At(
                    value = "HEAD"
            )
    )
    private void onServerLoaded(CallbackInfo ci) {
        EventManager.dispatch(new MinecraftServerEvent.ServerLoadedEvent(MiscUtil.cast(this)));
    }

    @Inject(
            method = "loadLevel",
            at = @At(
                    value = "HEAD"
            )
    )
    private void onServerLevelLoaded(CallbackInfo ci) {
        EventManager.dispatch(new MinecraftServerEvent.ServerLevelLoadedEvent(MiscUtil.cast(this)));
    }

    @Inject(
            method = "stopServer",
            at = @At(
                    value = "HEAD"
            )
    )
    private void onStopServer(CallbackInfo ci) {
        EventManager.dispatch(new MinecraftServerEvent.ServerCloseEvent(MiscUtil.cast(this)));
    }
}
