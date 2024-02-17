package top.hendrixshen.magiclib.impl.event.minecraft;

import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.event.Event;
import top.hendrixshen.magiclib.api.event.minecraft.LanguageReloadListener;

import java.util.List;

public class LanguageReloadEvent implements Event<LanguageReloadListener> {
    @Override
    public void dispatch(@NotNull List<LanguageReloadListener> listener) {
        listener.forEach(LanguageReloadListener::postLanguageReload);
    }

    @Override
    public Class<LanguageReloadListener> getListenerType() {
        return LanguageReloadListener.class;
    }
}
