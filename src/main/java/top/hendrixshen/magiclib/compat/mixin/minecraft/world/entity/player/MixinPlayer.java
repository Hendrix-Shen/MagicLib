package top.hendrixshen.magiclib.compat.mixin.minecraft.world.entity.player;

import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import top.hendrixshen.magiclib.compat.minecraft.world.entity.player.PlayerCompatApi;

//#if MC <= 11605
//$$ import net.minecraft.world.entity.player.Inventory;
//$$ import org.spongepowered.asm.mixin.Final;
//$$ import org.spongepowered.asm.mixin.Shadow;
//#endif


@Mixin(Player.class)
public class MixinPlayer implements PlayerCompatApi {
    //#if MC <= 11605
    //$$ @Shadow
    //$$ @Final
    //$$ public Inventory inventory;

    //$$ @Override
    //$$ public Inventory getInventory() {
    //$$     return this.inventory;
    //$$ }
    //#endif
}
