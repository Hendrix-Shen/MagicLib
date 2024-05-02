package top.hendrixshen.magiclib.compat.minecraft.api.client;

import com.mojang.blaze3d.platform.Window;
import top.hendrixshen.magiclib.compat.api.UnImplCompatApiException;

public interface MinecraftCompatApi {
    default Window getWindowCompat() {
        throw new UnImplCompatApiException();
    }

    //#if MC <= 11404
    //$$ default Window getWindow() {
    //$$     return this.getWindowCompat();
    //$$ }
    //#endif
}
