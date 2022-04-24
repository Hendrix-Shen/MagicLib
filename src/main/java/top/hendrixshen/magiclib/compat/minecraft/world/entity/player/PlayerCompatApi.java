package top.hendrixshen.magiclib.compat.minecraft.world.entity.player;

import net.minecraft.world.entity.player.Inventory;

public interface PlayerCompatApi {

    default Inventory getInventoryCompat() {
        throw new UnsupportedOperationException();
    }

    //#if MC <= 11605
    //$$ default Inventory getInventory() {
    //$$     return this.getInventoryCompat();
    //$$ }
    //#endif
}
