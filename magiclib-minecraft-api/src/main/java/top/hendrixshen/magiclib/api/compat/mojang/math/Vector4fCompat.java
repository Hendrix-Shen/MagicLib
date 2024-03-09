package top.hendrixshen.magiclib.api.compat.mojang.math;

import com.mojang.math.Matrix4f;
import com.mojang.math.Vector4f;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.impl.compat.mojang.math.Vector4fCompatImpl;
import top.hendrixshen.magiclib.util.collect.Provider;

@Environment(EnvType.CLIENT)
public interface Vector4fCompat extends Provider<Vector4f> {
    static @NotNull Vector4fCompat of(@NotNull Vector4f vector4f) {
        return new Vector4fCompatImpl(vector4f);
    }

    void transform(@NotNull Matrix4f matrix4f);
}
