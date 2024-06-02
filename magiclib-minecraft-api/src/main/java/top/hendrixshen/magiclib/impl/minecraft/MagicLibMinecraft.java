package top.hendrixshen.magiclib.impl.minecraft;

import lombok.Getter;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.api.event.minecraft.MinecraftServerListener;
import top.hendrixshen.magiclib.util.collect.ValueContainer;

public class MagicLibMinecraft implements MinecraftServerListener {
    @Nullable
    private MinecraftServer server;
    @Getter
    private static final MagicLibMinecraft instance = new MagicLibMinecraft();

    private MagicLibMinecraft() {
        MagicLib.getInstance().getEventManager().register(MinecraftServerListener.class, this);
    }

    @ApiStatus.Internal
    public static void init() {
        // NO-OP
    }

    public @NotNull ValueContainer<MinecraftServer> getServer() {
        return ValueContainer.ofNullable(this.server);
    }

    @Override
    public void onServerLoaded(MinecraftServer server) {
        this.server = server;
    }

    @Override
    public void onServerLevelLoaded(MinecraftServer server) {

    }

    @Override
    public void onServerClosed(MinecraftServer server) {
        this.server = null;
    }
}
