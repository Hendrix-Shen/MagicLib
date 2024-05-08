package top.hendrixshen.magiclib.mixin.compat.minecraft.world.inventory;

import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import top.hendrixshen.magiclib.compat.minecraft.api.world.inventory.SlotCompatApi;

//#if MC < 11700
import org.spongepowered.asm.mixin.Final;
//#endif

@Mixin(Slot.class)
public abstract class MixinSlot implements SlotCompatApi {
    //#if MC > 11605
    //$$ @Shadow
    //$$ public abstract int getContainerSlot();
    //#else
    @Shadow
    @Final
    private int slot;
    //#endif

    @Override
    public int getContainerSlotCompat() {
        //#if MC > 11605
        //$$ return this.getContainerSlot();
        //#else
        return this.slot;
        //#endif
    }
}
