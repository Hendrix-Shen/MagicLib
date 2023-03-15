package top.hendrixshen.magiclib.compat.minecraft.api.network.chat;

import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.contents.LiteralContents;
import net.minecraft.network.chat.contents.TranslatableContents;

import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.compat.api.UnImplCompatApiException;

public interface ComponentCompatApi {
    @Contract("_ -> new")
    static @NotNull MutableComponent literal(String string) {
        //#if MC > 11802
        return MutableComponent.create(new LiteralContents(string));
        //#else
        //$$ return new TextComponent(string);
        //#endif
    }

    @Contract("_, _ -> new")
    static @NotNull MutableComponent translatable(String string, Object... objects) {
        //#if MC > 11802
        return MutableComponent.create(new TranslatableContents(string, objects));
        //#else
        //$$ return new TranslatableComponent(string, objects);
        //#endif
    }

    default MutableComponent withStyleCompat(Style style) {
        throw new UnImplCompatApiException();
    }

    //#if MC <= 11502
    //$$ default BaseComponent withStyle(Style style) {
    //$$     return (BaseComponent) this.withStyleCompat(style);
    //$$ }
    //#endif
}
