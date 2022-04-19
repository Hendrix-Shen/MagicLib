package top.hendrixshen.magiclib.compat.mixin.minecraft.world.entity;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import top.hendrixshen.magiclib.compat.annotation.Remap;

@Mixin(Player.class)
public class MixinPlayer {
    @Shadow
    @Final
    public Inventory inventory;

    @Remap("method_31548")
    public Inventory getInventory() {
        return this.inventory;
    }
}
