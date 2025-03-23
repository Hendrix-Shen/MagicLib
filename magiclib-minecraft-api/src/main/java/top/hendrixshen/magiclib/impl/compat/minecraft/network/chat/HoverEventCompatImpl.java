package top.hendrixshen.magiclib.impl.compat.minecraft.network.chat;

import org.jetbrains.annotations.NotNull;

// CHECKSTYLE.OFF: ImportOrder
//#if 12105 > MC && MC > 11502
import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;
//#endif
// CHECKSTYLE.ON: ImportOrder

import net.minecraft.network.chat.HoverEvent;

import top.hendrixshen.magiclib.api.compat.AbstractCompat;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.HoverEventCompat;

public class HoverEventCompatImpl extends AbstractCompat<HoverEvent> implements HoverEventCompat {
    public HoverEventCompatImpl(@NotNull HoverEvent type) {
        super(type);
    }

    //#if 12105 > MC && MC > 11502
    @ScheduledForRemoval
    @Deprecated
    public static @NotNull <T> HoverEventCompatImpl of(HoverEvent.Action<T> action, T object) {
        return new HoverEventCompatImpl(new HoverEvent(action, object));
    }
    //#endif
}
