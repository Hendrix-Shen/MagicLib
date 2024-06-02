package top.hendrixshen.magiclib.api.event.minecraft;

import net.minecraft.server.MinecraftServer;
import top.hendrixshen.magiclib.api.event.Listener;

public interface MinecraftServerListener extends Listener {
    void onServerLoaded(MinecraftServer server);

    void onServerLevelLoaded(MinecraftServer server);

    void onServerClosed(MinecraftServer server);
}
