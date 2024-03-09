package top.hendrixshen.magiclib.api.compat.mojang.math;

import com.mojang.math.Vector3f;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.impl.compat.mojang.math.Vector3fCompatImpl;
import top.hendrixshen.magiclib.util.collect.Provider;

@Environment(EnvType.CLIENT)
public interface Vector3fCompat extends Provider<Vector3f> {
    static @NotNull Vector3fCompat of(@NotNull Vector3f vector3f) {
        return new Vector3fCompatImpl(vector3f);
    }

    QuaternionCompat rotationDegrees(float degrees);
}
