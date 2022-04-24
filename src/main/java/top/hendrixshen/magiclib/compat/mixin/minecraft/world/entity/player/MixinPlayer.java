package top.hendrixshen.magiclib.compat.mixin.minecraft.world.entity.player;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import top.hendrixshen.magiclib.compat.minecraft.world.entity.player.PlayerCompatApi;

//#if MC <= 11605
//$$ import org.spongepowered.asm.mixin.Final;
//#endif


@Mixin(Player.class)
public abstract class MixinPlayer implements PlayerCompatApi {

    //#if MC > 11605
    @Shadow
    public abstract Inventory getInventory();
    //#else
    //$$ @Shadow
    //$$ @Final
    //$$ public Inventory inventory;
    //#endif

    public Inventory getInventoryCompat() {
        //#if MC > 11605
        return this.getInventory();
        //#else
        //$$     return this.inventory;
        //#endif
    }
}
