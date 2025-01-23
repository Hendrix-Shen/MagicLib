package top.hendrixshen.magiclib.api.compat.minecraft.world.inventory;

import org.jetbrains.annotations.NotNull;

import net.minecraft.world.inventory.Slot;

import top.hendrixshen.magiclib.impl.compat.minecraft.world.inventory.SlotCompatImpl;
import top.hendrixshen.magiclib.util.collect.Provider;

public interface SlotCompat extends Provider<Slot> {
    static @NotNull SlotCompat of(@NotNull Slot slot) {
        return new SlotCompatImpl(slot);
    }

    int getContainerSlot();
}
