package top.hendrixshen.magiclib.impl.compat.minecraft.client;

import lombok.Getter;

import net.minecraft.client.gui.components.toasts.ToastComponent;

import org.jetbrains.annotations.NotNull;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

import top.hendrixshen.magiclib.api.compat.AbstractCompat;
import top.hendrixshen.magiclib.api.compat.minecraft.client.CameraCompat;
import top.hendrixshen.magiclib.api.compat.minecraft.client.MinecraftCompat;

public class MinecraftCompatImpl extends AbstractCompat<Minecraft> implements MinecraftCompat {
    @Getter(lazy = true)
    private static final MinecraftCompatImpl instance = new MinecraftCompatImpl(Minecraft.getInstance());

    private MinecraftCompatImpl(@NotNull Minecraft type) {
        super(type);
    }

    @Override
    public ToastComponent getToasts() {
        //#if MC >= 26.2
        //$$ return this.get().gui.toastManager();
        //#else
        return this.get().getToasts();
        //#endif
    }

    @Override
    public Window getWindow() {
        //#if MC > 11404
        return this.get().getWindow();
        //#else
        //$$ return this.get().window;
        //#endif
    }

    @Override
    public Screen getScreen() {
        //#if MC >= 26.2
        //$$ return this.get().gui.screen();
        //#else;
        return this.get().screen;
        //#endif
    }

    @Override
    public void setScreen(Screen screen) {
        //#if MC >= 26.2
        //$$ this.get().gui.setScreen(screen);
        //#else
        this.get().setScreen(screen);
        //#endif
    }

    @Override
    public Camera getMainCamera() {
        return this.get().gameRenderer.getMainCamera();
    }

    @Override
    public CameraCompat getMainCameraCompat() {
        return CameraCompat.of(this.getMainCamera());
    }

    @Override
    public @NotNull Minecraft get() {
        return MinecraftCompat.super.get();
    }
}
