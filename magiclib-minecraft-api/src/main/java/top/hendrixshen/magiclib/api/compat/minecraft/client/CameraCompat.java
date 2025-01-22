package top.hendrixshen.magiclib.api.compat.minecraft.client;

import org.jetbrains.annotations.NotNull;

import net.minecraft.client.Camera;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import top.hendrixshen.magiclib.api.compat.mojang.math.QuaternionCompat;
import top.hendrixshen.magiclib.impl.compat.minecraft.client.CameraCompatImpl;
import top.hendrixshen.magiclib.util.collect.Provider;

@Environment(EnvType.CLIENT)
public interface CameraCompat extends Provider<Camera> {
    static @NotNull CameraCompat of(@NotNull Camera camera) {
        return new CameraCompatImpl(camera);
    }

    QuaternionCompat rotation();
}
