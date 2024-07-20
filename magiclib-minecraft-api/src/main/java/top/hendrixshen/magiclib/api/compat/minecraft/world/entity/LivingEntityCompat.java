package top.hendrixshen.magiclib.api.compat.minecraft.world.entity;

import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.impl.compat.minecraft.world.entity.LivingEntityCompatImpl;

public interface LivingEntityCompat extends EntityCompat {
    static @NotNull LivingEntityCompat of(@NotNull LivingEntity entity) {
        return new LivingEntityCompatImpl(entity);
    }

    @Override
    @NotNull
    LivingEntity get();
}
