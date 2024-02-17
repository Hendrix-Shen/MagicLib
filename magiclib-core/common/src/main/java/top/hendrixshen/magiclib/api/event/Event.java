package top.hendrixshen.magiclib.api.event;

import java.util.List;

public interface Event<L extends Listener> {
    void dispatch(List<L> listener);

    Class<L> getListenerType();
}
