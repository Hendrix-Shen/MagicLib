package top.hendrixshen.magiclib.compat.mixin.minecraft.world.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import top.hendrixshen.magiclib.compat.annotation.Remap;

import java.util.UUID;

@Mixin(Entity.class)
public abstract class MixinEntity {
    @Shadow
    public abstract void sendMessage(Component component);

    @Shadow
    public abstract BlockPos getCommandSenderBlockPosition();

    @Shadow
    public Level level;

    @Remap("method_9203")
    public void sendMessage(Component component, UUID uuid) {
        this.sendMessage(component);
    }

    @Remap("method_24515")
    public BlockPos blockPosition() {
        return this.getCommandSenderBlockPosition();
    }

    @Remap("method_31477")
    public int getBlockX() {
        return this.blockPosition().getX();
    }

    @Remap("method_31478")
    public int getBlockY() {
        return this.blockPosition().getY();
    }

    @Remap("method_31479")
    public int getBlockZ() {
        return this.blockPosition().getZ();
    }

    @Remap("method_37908")
    public Level getLevel() {
        return this.level;
    }
}
