package top.hendrixshen.magiclib.compat.minecraft.network.chat;

import net.minecraft.network.chat.BaseComponent;
import net.minecraft.network.chat.Style;

public interface ComponentCompatApi {

    default BaseComponent withStyleCompat(Style style) {
        throw new UnsupportedOperationException();
    }

    //# MC <= 11502
    //$$ default BaseComponent withStyle(Style style) {
    //$$     return this.withStyleCompat(style);
    //$$ }
    //# endif
}
