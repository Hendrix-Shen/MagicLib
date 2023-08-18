package top.hendrixshen.magiclib.compat.minecraft.api.network.chat;

import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.contents.LiteralContents;
import net.minecraft.network.chat.contents.TranslatableContents;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.compat.api.UnImplCompatApiException;

//#if MC < 11900
//$$ import net.minecraft.network.chat.BaseComponent;
//#endif

//#if MC > 11502
import net.minecraft.network.chat.MutableComponent;
//#endif

public interface ComponentCompatApi {
    @Contract("_ -> new")
    //#if MC > 11802
    static @NotNull MutableComponent literal(String string) {
    //#else
    //$$ static @NotNull BaseComponent literal(String string) {
    //#endif
        //#if MC > 11802
        return MutableComponent.create(new LiteralContents(string));
        //#else
        //$$ return new TextComponent(string);
        //#endif
    }

    @Contract("_, _ -> new")
    //#if MC > 11802
    static @NotNull MutableComponent translatable(String string, Object... objects) {
    //#else
    //$$ static @NotNull BaseComponent translatable(String string, Object... objects) {
    //#endif
        //#if MC > 11903
        return MutableComponent.create(new TranslatableContents(string, null, objects));
        //#elseif MC > 11802
        //$$ return MutableComponent.create(new TranslatableContents(string, objects));
        //#else
        //$$ return new TranslatableComponent(string, objects);
        //#endif
    }

    //#if MC > 11802
    default MutableComponent withStyleCompat(Style style) {
    //#else
    //$$ default BaseComponent withStyleCompat(Style style) {
    //#endif
        throw new UnImplCompatApiException();
    }

    //#if MC < 11600
    //$$ default BaseComponent withStyle(Style style) {
    //$$     return (BaseComponent) this.withStyleCompat(style);
    //$$ }
    //#endif
}
