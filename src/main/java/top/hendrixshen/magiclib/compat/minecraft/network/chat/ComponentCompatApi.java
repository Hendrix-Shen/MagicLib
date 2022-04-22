package top.hendrixshen.magiclib.compat.minecraft.network.chat;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

public interface ComponentCompatApi {
    //# MC <= 11502
    //$$ default Component withStyle(Style style) {
    //$$     throw new UnsupportedOperationException();
    //$$ }
    //# endif
}
