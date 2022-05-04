package top.hendrixshen.magiclib.compat.mixin.minecraft.client;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import top.hendrixshen.magiclib.compat.minecraft.client.MinecraftCompatApi;

//#if MC <= 11404
//$$ import org.spongepowered.asm.mixin.Final;
//#endif

@Mixin(Minecraft.class)
public abstract class MixinMinecraft implements MinecraftCompatApi {
    //#if MC > 11404
    @Shadow
    public abstract Window getWindow();
    //#else
    //$$ @Shadow
    //$$ @Final
    //$$ public Window window;
    //#endif


    @Override
    public Window getWindowCompat() {
        //#if MC > 11404
        return this.getWindow();
        //#else
        //$$ return this.window;
        //#endif
    }
}
