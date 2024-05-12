package top.hendrixshen.magiclib.api.compat.minecraft.network.chat;

import net.minecraft.network.chat.HoverEvent;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.impl.compat.minecraft.network.chat.HoverEventCompatImpl;
import top.hendrixshen.magiclib.util.collect.Provider;

public interface HoverEventCompat extends Provider<HoverEvent> {
    static @NotNull HoverEventCompat of(HoverEvent hoverEvent) {
        return new HoverEventCompatImpl(hoverEvent);
    }

    //#if MC > 11502
    static @NotNull <T> HoverEventCompat of(HoverEvent.Action<T> action, @NotNull Provider<T> object) {
        return HoverEventCompatImpl.of(action, object.get());
    }
    //#else
    //$$ public static @NotNull HoverEventCompatImpl of(HoverEvent.Action action, ComponentCompat object) {
    //$$     return new HoverEventCompatImpl(new HoverEvent(action, object.get()));
    //$$ }
    //#endif
}
