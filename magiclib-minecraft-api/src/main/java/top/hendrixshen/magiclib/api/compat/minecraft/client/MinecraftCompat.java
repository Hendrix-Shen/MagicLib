package top.hendrixshen.magiclib.api.compat.minecraft.client;

import com.mojang.blaze3d.platform.Window;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.impl.compat.minecraft.client.MinecraftCompatImpl;
import top.hendrixshen.magiclib.util.collect.Provider;

@Environment(EnvType.CLIENT)
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
