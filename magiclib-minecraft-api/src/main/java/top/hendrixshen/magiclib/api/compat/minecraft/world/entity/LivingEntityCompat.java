package top.hendrixshen.magiclib.api.compat.minecraft.world.entity;

import org.jetbrains.annotations.NotNull;

import net.minecraft.world.entity.LivingEntity;

import top.hendrixshen.magiclib.impl.compat.minecraft.world.entity.LivingEntityCompatImpl;

public interface LivingEntityCompat extends EntityCompat {
    static @NotNull LivingEntityCompat of(@NotNull LivingEntity entity) {
        return new LivingEntityCompatImpl(entity);
    }

    @Override
    @NotNull
    LivingEntity get();
}
