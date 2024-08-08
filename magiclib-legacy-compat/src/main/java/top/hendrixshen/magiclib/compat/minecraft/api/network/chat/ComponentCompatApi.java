package top.hendrixshen.magiclib.compat.minecraft.api.network.chat;

import net.minecraft.network.chat.*;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.ComponentCompat;
import top.hendrixshen.magiclib.compat.api.UnImplCompatApiException;

@Deprecated
@ApiStatus.ScheduledForRemoval
public interface ComponentCompatApi {
    @Contract("_ -> new")
    static @NotNull BaseComponent literal(String string) {
        return ComponentCompat.literal(string);
    }

    @Contract("_, _ -> new")
    static @NotNull BaseComponent translatable(String string, Object... objects) {
        return ComponentCompat.translatable(string, objects);
    }

    default BaseComponent withStyleCompat(Style style) {
        throw new UnImplCompatApiException();
    }

    //#if MC < 11600
    //$$ default BaseComponent withStyle(Style style) {
    //$$     return (BaseComponent) this.withStyle(style);
    //$$ }
    //#endif
}
