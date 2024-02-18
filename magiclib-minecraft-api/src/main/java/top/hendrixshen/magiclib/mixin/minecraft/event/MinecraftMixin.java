package top.hendrixshen.magiclib.mixin.minecraft.event;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.hendrixshen.magiclib.impl.event.EventManager;
import top.hendrixshen.magiclib.impl.event.minecraft.MinecraftEvent;

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
        EventManager.dispatch(new MinecraftEvent.MinecraftPostInitEvent());
    }
}
