package top.hendrixshen.magiclib.impl.compat.minecraft.network.chat;

import net.minecraft.network.chat.ClickEvent;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.compat.AbstractCompat;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.ClickEventCompat;

public class ClickEventCompatImpl extends AbstractCompat<ClickEvent> implements ClickEventCompat {
    public ClickEventCompatImpl(@NotNull ClickEvent type) {
        super(type);
    }

    public static @NotNull ClickEventCompatImpl of(ClickEvent.Action action, String string) {
        return new ClickEventCompatImpl(new ClickEvent(action, string));
    }
}
