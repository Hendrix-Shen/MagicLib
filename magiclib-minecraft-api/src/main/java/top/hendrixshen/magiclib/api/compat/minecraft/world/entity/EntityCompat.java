package top.hendrixshen.magiclib.api.compat.minecraft.world.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.compat.minecraft.chat.network.ComponentCompat;
import top.hendrixshen.magiclib.api.compat.minecraft.world.level.LevelCompat;
import top.hendrixshen.magiclib.impl.compat.minecraft.world.entity.EntityCompatImpl;
import top.hendrixshen.magiclib.util.collect.Provider;

public interface EntityCompat extends Provider<Entity> {
    static @NotNull EntityCompat of(@NotNull Entity entity) {
        return new EntityCompatImpl(entity);
    }

    LevelCompat getLevel();

    double getX();

    double getY();

    double getZ();

    int getBlockX();

    int getBlockY();

    int getBlockZ();

    float getYRot();

    void setYRot(float yRot);

    float getXRot();

    void setXRot(float xRot);

    // TODO: Make BlockPos Compatible
    BlockPos getBlockPosition();

    boolean isOnGround();

    void setOnGround(boolean onGround);

    void sendSystemMessage(@NotNull ComponentCompat component);

    float getMaxUpStep();

    void setMaxUpStep(float maxUpStep);
}
