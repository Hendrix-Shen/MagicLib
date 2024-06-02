package top.hendrixshen.magiclib.impl.event.minecraft;

import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.event.Event;
import top.hendrixshen.magiclib.api.event.minecraft.DedicatedServerListener;

import java.util.List;

public class DedicatedServerEvent {
    public static class PostInitEvent implements Event<DedicatedServerListener> {
        @Override
        public void dispatch(@NotNull List<DedicatedServerListener> listeners) {
            listeners.forEach(DedicatedServerListener::postServerInit);
        }

        @Override
        public Class<DedicatedServerListener> getListenerType() {
            return DedicatedServerListener.class;
        }
    }
}
