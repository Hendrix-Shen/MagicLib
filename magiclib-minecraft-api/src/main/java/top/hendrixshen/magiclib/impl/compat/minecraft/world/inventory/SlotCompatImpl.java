package top.hendrixshen.magiclib.impl.compat.minecraft.world.inventory;

import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.compat.AbstractCompat;
import top.hendrixshen.magiclib.api.compat.minecraft.world.inventory.SlotCompat;

//#if MC < 11700
import top.hendrixshen.magiclib.mixin.minecraft.accessor.SlotAccessor;
//#endif

public class SlotCompatImpl extends AbstractCompat<Slot> implements SlotCompat {
    public SlotCompatImpl(@NotNull Slot type) {
        super(type);
    }

    @Override
    public int getContainerSlot() {
        //#if MC > 11605
        //$$ return this.get().getContainerSlot();
        //#else
        return ((SlotAccessor) this.get()).magiclib$getSlot();
        //#endif
    }
}
