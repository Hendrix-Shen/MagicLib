package top.hendrixshen.magiclib.compat.mixin.minecraft.world.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import top.hendrixshen.magiclib.compat.annotation.Remap;

@Mixin(Entity.class)
public class MixinEntity {
    @Shadow
    private BlockPos blockPosition;

    @Shadow
    public Level level;

    @Shadow
    public float yRot;

    @Shadow
    public float xRot;

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

    @Remap("method_37908")
    public Level getLevel() {
        return this.level;
    }

    @Remap("method_36454")
    public float getYRot() {
        return this.yRot;
    }

    @Remap("method_36456")
    public void setYRot(float f) {
        this.yRot = f;
    }

    @Remap("method_36455")
    public float getXRot() {
        return this.xRot;
    }

    @Remap("method_36457")
    public void setXRot(float f) {
        this.xRot = f;
    }
}
