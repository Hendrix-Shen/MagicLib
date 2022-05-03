package top.hendrixshen.magiclib.test.compat.minecraft.world.inventory;

import net.minecraft.world.inventory.Slot;

public class TestSlot {
    public static void test() {
        Slot slot = new Slot(null, 0, 0, 0);
        slot.getContainerSlotCompat();
        slot.getContainerSlot();
    }
}
