package top.hendrixshen.magiclib.api.event;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractCancellableEvent<T extends Listener> implements CancellableEvent<T> {
    private boolean cancelled;
}
