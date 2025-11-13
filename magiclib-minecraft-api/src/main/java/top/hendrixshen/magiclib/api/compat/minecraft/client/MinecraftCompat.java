package top.hendrixshen.magiclib.api.compat.minecraft.client;

import org.jetbrains.annotations.NotNull;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;

import top.hendrixshen.magiclib.impl.compat.minecraft.client.MinecraftCompatImpl;
import top.hendrixshen.magiclib.util.collect.Provider;

public interface MinecraftCompat extends Provider<Minecraft> {
    static MinecraftCompat getInstance() {
        return MinecraftCompatImpl.getInstance();
    }

    Window getWindow();

    @Override
    default @NotNull Minecraft get() {
        return Minecraft.getInstance();
    }
}
