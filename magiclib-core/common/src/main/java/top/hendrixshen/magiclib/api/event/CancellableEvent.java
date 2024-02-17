package top.hendrixshen.magiclib.api.event;

public interface CancellableEvent<T extends Listener> extends Event<T> {
    void setCancelled(boolean cancelled);

    boolean isCancelled();
}
