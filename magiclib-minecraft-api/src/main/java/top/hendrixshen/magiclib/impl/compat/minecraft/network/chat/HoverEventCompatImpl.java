package top.hendrixshen.magiclib.impl.compat.minecraft.network.chat;

import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.HoverEvent;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.compat.AbstractCompat;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.ClickEventCompat;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.HoverEventCompat;

public class HoverEventCompatImpl extends AbstractCompat<HoverEvent> implements HoverEventCompat {
    public HoverEventCompatImpl(@NotNull HoverEvent type) {
        super(type);
    }

    //#if MC > 11502
    public static @NotNull <T> HoverEventCompatImpl of(HoverEvent.Action<T> action, T object) {
        return new HoverEventCompatImpl(new HoverEvent(action, object));
    }
    //#else
    //#endif
}
