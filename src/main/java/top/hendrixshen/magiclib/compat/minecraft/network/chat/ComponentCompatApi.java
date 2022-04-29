package top.hendrixshen.magiclib.compat.minecraft.network.chat;

import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;

//#if MC > 11502
import net.minecraft.network.chat.MutableComponent;
//#else
//$$ import net.minecraft.network.chat.BaseComponent;
//#endif

public interface ComponentCompatApi {

    //#if MC > 11502
    default MutableComponent
    //#else
    //$$ default BaseComponent
    //#endif
    withStyleCompat(Style style) {
        throw new UnsupportedOperationException();
    }

    //#if MC > 11502
    static MutableComponent
    //#else
    //$$ static BaseComponent
    //#endif
    literal(String string) {
        //#if MC > 11802
        //$$ return MutableComponent.create(new LiteralContents(string));
        //#else
        return new TextComponent(string);
        //#endif
    }

    //#if MC <= 11502
    //$$ default BaseComponent withStyle(Style style) {
    //$$     return (BaseComponent) this.withStyleCompat(style);
    //$$ }
    //#endif
}
