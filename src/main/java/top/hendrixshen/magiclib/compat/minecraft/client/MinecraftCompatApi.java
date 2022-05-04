package top.hendrixshen.magiclib.compat.minecraft.client;

import com.mojang.blaze3d.platform.Window;

public interface MinecraftCompatApi {
    default Window getWindowCompat() {
        throw new UnsupportedOperationException();
    }

    //#if MC <= 11404
    //$$ default Window getWindow() {
    //$$     return this.getWindowCompat();
    //$$ }
    //#endif
}
