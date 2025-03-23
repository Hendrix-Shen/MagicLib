package top.hendrixshen.magiclib.impl.compat.minecraft.network.chat;

import org.jetbrains.annotations.NotNull;

// CHECKSTYLE.OFF: ImportOrder
//#if MC < 12105
import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;
//#endif
// CHECKSTYLE.OFF: ImportOrder

import net.minecraft.network.chat.ClickEvent;

// CHECKSTYLE.OFF: ImportOrder
//#if MC < 12105
import net.minecraft.network.chat.ClickEvent.Action;
//#endif
// CHECKSTYLE.OFF: ImportOrder

import top.hendrixshen.magiclib.api.compat.AbstractCompat;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.ClickEventCompat;

public class ClickEventCompatImpl extends AbstractCompat<ClickEvent> implements ClickEventCompat {
    public ClickEventCompatImpl(@NotNull ClickEvent type) {
        super(type);
    }

    //#if MC < 12105
    @ScheduledForRemoval
    @Deprecated
    public static @NotNull ClickEventCompatImpl of(Action action, String string) {
        return new ClickEventCompatImpl(new ClickEvent(action, string));
    }
    //#endif
}
