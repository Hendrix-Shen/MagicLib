package top.hendrixshen.magiclib.compat.mixin.minecraft.world.item.context;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import top.hendrixshen.magiclib.compat.annotation.InitMethod;
import top.hendrixshen.magiclib.compat.annotation.ThisInitMethod;

@Mixin(BlockPlaceContext.class)
public class MixinBlockPlaceContext {

    @InitMethod
    public void magicInit(Player player, InteractionHand interactionHand, ItemStack itemStack, BlockHitResult blockHitResult) {
        magicThis(player.level, player, interactionHand, itemStack, blockHitResult);
    }

    @ThisInitMethod
    public void magicThis(Level level, @Nullable Player player, InteractionHand interactionHand, ItemStack itemStack, BlockHitResult blockHitResult) {
    }
}
