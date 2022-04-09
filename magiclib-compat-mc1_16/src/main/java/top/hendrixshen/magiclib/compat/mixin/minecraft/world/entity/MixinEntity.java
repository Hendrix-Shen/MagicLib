package top.hendrixshen.magiclib.compat.mixin.minecraft.world.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import top.hendrixshen.magiclib.compat.annotation.Remap;

@Mixin(Entity.class)
public class MixinEntity {
    @Shadow
    private BlockPos blockPosition;

    @Remap("method_31477")
    public int getBlockX() {
        return this.blockPosition.getX();
    }

    @Remap("method_31478")
    public int getBlockY() {
        return this.blockPosition.getY();
    }

    @Remap("method_31479")
    public int getBlockZ() {
        return this.blockPosition.getZ();
    }
}
