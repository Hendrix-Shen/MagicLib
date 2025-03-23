package top.hendrixshen.magiclib.api.compat.minecraft.network.chat;

import org.jetbrains.annotations.NotNull;

// CHECKSTYLE.OFF: ImportOrder
//#if MC < 12105
import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;
//#endif
// CHECKSTYLE.ON: ImportOrder

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;

// CHECKSTYLE.OFF: ImportOrder
//#if MC < 12105
import net.minecraft.network.chat.HoverEvent.Action;
//#endif
// CHECKSTYLE.ON: ImportOrder

// CHECKSTYLE.OFF: ImportOrder
//#if MC > 12104
//$$ import net.minecraft.network.chat.HoverEvent.ShowEntity;
//$$ import net.minecraft.network.chat.HoverEvent.ShowItem;
//$$ import net.minecraft.network.chat.HoverEvent.ShowText;
//$$ import net.minecraft.world.item.ItemStack;
//#elseif MC > 11502
import net.minecraft.network.chat.HoverEvent.ItemStackInfo;
//#endif

//#if MC > 11502
import net.minecraft.network.chat.HoverEvent.EntityTooltipInfo;
//#endif
// CHECKSTYLE.ON: ImportOrder

import top.hendrixshen.magiclib.impl.compat.minecraft.network.chat.HoverEventCompatImpl;
import top.hendrixshen.magiclib.util.collect.Provider;

public interface HoverEventCompat extends Provider<HoverEvent> {
    static @NotNull HoverEventCompat of(HoverEvent hoverEvent) {
        return new HoverEventCompatImpl(hoverEvent);
    }

    //#if MC < 12105
    @ScheduledForRemoval
    @Deprecated
    //#if MC > 11502
    static @NotNull <T> HoverEventCompat of(Action<T> action, @NotNull Provider<T> object) {
        return HoverEventCompatImpl.of(action, object.get());
    }
    //#else
    //$$ static @NotNull HoverEventCompatImpl of(Action action, ComponentCompat object) {
    //$$     return new HoverEventCompatImpl(new HoverEvent(action, object.get()));
    //$$ }
    //#endif
    //#endif

    static @NotNull HoverEvent showText(Component text) {
        //#if MC > 12104
        //$$ return new ShowText(text);
        //#else
        return new HoverEvent(Action.SHOW_TEXT, text);
        //#endif
    }

    static @NotNull HoverEventCompat showTextCompat(ComponentCompat text) {
        return HoverEventCompat.of(HoverEventCompat.showText(text.get()));
    }

    static @NotNull HoverEvent showItem(
            //#if MC > 12104
            //$$ ItemStack itemStack
            //#elseif MC > 11502
            ItemStackInfo itemStack
            //#else
            //$$ Component itemStack
            //#endif
    ) {
        //#if MC > 12104
        //$$ return new ShowItem(itemStack);
        //#else
        return new HoverEvent(Action.SHOW_ITEM, itemStack);
        //#endif
    }

    static @NotNull HoverEventCompat showItemCompat(
            //#if MC > 12104
            //$$ ItemStack itemStack
            //#elseif MC > 11502
            ItemStackInfo itemStack
            //#else
            //$$ ComponentCompat itemStack
            //#endif
    ) {
        return HoverEventCompat.of(HoverEventCompat.showItem(
                //#if MC > 11502
                itemStack
                //#else
                //$$ itemStack.get()
                //#endif
        ));
    }

    static @NotNull HoverEvent showEntity(
            //#if MC > 11502
            EntityTooltipInfo entityTooltip
            //#else
            //$$ Component entityTooltip
            //#endif
    ) {
        //#if MC > 12104
        //$$ return new ShowEntity(entityTooltip);
        //#else
        return new HoverEvent(Action.SHOW_ENTITY, entityTooltip);
        //#endif
    }

    static @NotNull HoverEventCompat showEntityCompat(
            //#if MC > 11502
            EntityTooltipInfo entityTooltip
            //#else
            //$$ ComponentCompat entityTooltip
            //#endif
    ) {
        return HoverEventCompat.of(HoverEventCompat.showEntity(
                //#if MC > 11502
                entityTooltip
                //#else
                //$$ entityTooltip.get()
                //#endif
        ));
    }
}
