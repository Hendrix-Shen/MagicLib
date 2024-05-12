package top.hendrixshen.magiclib.api.compat.minecraft.network.chat;

import net.minecraft.network.chat.ClickEvent;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.impl.compat.minecraft.network.chat.ClickEventCompatImpl;
import top.hendrixshen.magiclib.util.collect.Provider;

public interface ClickEventCompat extends Provider<ClickEvent> {
    static @NotNull ClickEventCompat of(ClickEvent clickEvent) {
        return new ClickEventCompatImpl(clickEvent);
    }

    static @NotNull ClickEventCompat of(ClickEvent.Action action, String string) {
        return ClickEventCompatImpl.of(action, string);
    }
}
