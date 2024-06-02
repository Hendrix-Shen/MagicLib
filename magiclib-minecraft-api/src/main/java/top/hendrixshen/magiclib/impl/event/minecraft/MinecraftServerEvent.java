package top.hendrixshen.magiclib.impl.event.minecraft;

import lombok.AllArgsConstructor;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.event.Event;
import top.hendrixshen.magiclib.api.event.minecraft.MinecraftServerListener;

import java.util.List;

public class MinecraftServerEvent {
    @AllArgsConstructor
    public static class ServerLoadedEvent implements Event<MinecraftServerListener> {
        private final MinecraftServer server;

        @Override
        public void dispatch(@NotNull List<MinecraftServerListener> listeners) {
            listeners.forEach(listener -> listener.onServerLoaded(this.server));
        }

        @Override
        public Class<MinecraftServerListener> getListenerType() {
            return MinecraftServerListener.class;
        }
    }

    @AllArgsConstructor
    public static class ServerLevelLoadedEvent implements Event<MinecraftServerListener> {
        private final MinecraftServer server;

        @Override
        public void dispatch(@NotNull List<MinecraftServerListener> listeners) {
            listeners.forEach(listener -> listener.onServerLoaded(this.server));
        }

        @Override
        public Class<MinecraftServerListener> getListenerType() {
            return MinecraftServerListener.class;
        }
    }

    @AllArgsConstructor
    public static class ServerCloseEvent implements Event<MinecraftServerListener> {
        private final MinecraftServer server;

        @Override
        public void dispatch(@NotNull List<MinecraftServerListener> listeners) {
            listeners.forEach(listener -> listener.onServerClosed(this.server));
        }

        @Override
        public Class<MinecraftServerListener> getListenerType() {
            return MinecraftServerListener.class;
        }
    }
}
