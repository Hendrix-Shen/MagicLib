package top.hendrixshen.magiclib.impl.compat.minecraft.client;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;

import top.hendrixshen.magiclib.api.compat.AbstractCompat;
import top.hendrixshen.magiclib.api.compat.minecraft.client.MinecraftCompat;

public class MinecraftCompatImpl extends AbstractCompat<Minecraft> implements MinecraftCompat {
    @Getter(lazy = true)
    private static final MinecraftCompatImpl instance = new MinecraftCompatImpl(Minecraft.getInstance());

    private MinecraftCompatImpl(@NotNull Minecraft type) {
        super(type);
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
    public @NotNull Minecraft get() {
        return MinecraftCompat.super.get();
    }
}
