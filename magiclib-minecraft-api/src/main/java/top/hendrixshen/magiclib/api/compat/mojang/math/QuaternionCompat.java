package top.hendrixshen.magiclib.api.compat.mojang.math;

import com.mojang.math.Quaternion;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.impl.compat.mojang.math.QuaternionCompatImpl;
import top.hendrixshen.magiclib.util.collect.Provider;

@Environment(EnvType.CLIENT)
public interface QuaternionCompat extends Provider<Quaternion> {
    QuaternionCompat ONE = QuaternionCompat.of(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));

    static @NotNull QuaternionCompat of(@NotNull Quaternion quaternion) {
        return new QuaternionCompatImpl(quaternion);
    }

    void mul(float f);
}
