package top.hendrixshen.magiclib.impl.event;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.api.event.Event;
import top.hendrixshen.magiclib.api.event.Listener;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class EventManager {
    @Getter
    private final MagicLib magicLib;
    private final Map<Class<? extends Listener>, List<? extends Listener>> listeners = Maps.newConcurrentMap();

    public EventManager(MagicLib magicLib) {
        Preconditions.checkNotNull(magicLib);
        this.magicLib = magicLib;
    }

    public static <L extends Listener, E extends Event<L>> void dispatch(E event) {
        EventManager manager = MagicLib.getInstance().getEventManager();

        if (manager == null) {
            throw new IllegalStateException("Event " + event.getClass().getName() + "was dispatched too early!");
        }

        manager.dispatchImpl(event);
    }

    private <L extends Listener, E extends Event<L>> void dispatchImpl(@NotNull E event) {
        try {
            Class<L> type = event.getListenerType();
            @SuppressWarnings("unchecked")
            List<L> listeners = (List<L>) this.listeners.get(type);

            if (listeners == null || listeners.isEmpty()) {
                return;
            }

            List<L> listenersCopy = Lists.newArrayList(listeners);
            listenersCopy.removeIf(Objects::isNull);
            event.dispatch(listenersCopy);
        } catch (Throwable t) {
            throw new RuntimeException("Exception occurs while delegating events.\n  Event class: " +
                    event.getClass().getName(), t);
        }
    }

    public <L extends Listener> void register(Class<L> type, L listener) {
        try {
            @SuppressWarnings("unchecked")
            List<L> listeners = (List<L>) this.listeners.computeIfAbsent(type, k -> Lists.newArrayList());
            listeners.add(listener);
        } catch (Throwable t) {
            throw new RuntimeException("Exception occurs while register event.\n  Listener class: " +
                    listener.getClass().getName(), t);
        }
    }

    public <L extends Listener> void unregister(Class<L> type, L listener) {
        try {
            @SuppressWarnings("unchecked")
            List<L> listeners = (List<L>) this.listeners.get(type);

            if (listeners == null) {
                return;
            }

            listeners.remove(listener);
        } catch (Throwable t) {
            throw new RuntimeException("Exception occurs while unregister event.\n  Listener class: " +
                    listener.getClass().getName(), t);
        }
    }
}
