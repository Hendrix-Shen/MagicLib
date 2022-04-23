package top.hendrixshen.magiclib.compat.minecraft.world.entity.player;

//#if MC <= 11605
//$$ import net.minecraft.world.entity.player.Inventory;
//#endif

public interface PlayerCompatApi {
    //#if MC <= 11605
    //$$ default Inventory getInventory() {
    //$$     throw new UnsupportedOperationException();
    //$$ }
    //#endif
}
