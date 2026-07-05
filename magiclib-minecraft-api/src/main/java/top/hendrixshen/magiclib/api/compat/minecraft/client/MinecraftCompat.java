package top.hendrixshen.magiclib.api.compat.minecraft.client;

import org.jetbrains.annotations.NotNull;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.gui.screens.Screen;

import top.hendrixshen.magiclib.impl.compat.minecraft.client.MinecraftCompatImpl;
import top.hendrixshen.magiclib.util.collect.Provider;

public interface MinecraftCompat extends Provider<Minecraft> {
    static MinecraftCompat getInstance() {
        return MinecraftCompatImpl.getInstance();
    }

    ToastComponent getToasts();

    Window getWindow();

    Screen getScreen();

    void setScreen(Screen screen);

    Camera getMainCamera();

    CameraCompat getMainCameraCompat();

    @Override
    default @NotNull Minecraft get() {
        return Minecraft.getInstance();
    }
}
