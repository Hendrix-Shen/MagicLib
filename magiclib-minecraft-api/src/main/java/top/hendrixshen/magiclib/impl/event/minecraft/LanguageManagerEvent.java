package top.hendrixshen.magiclib.impl.event.minecraft;

import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.event.Event;
import top.hendrixshen.magiclib.api.event.minecraft.LanguageManagerListener;

import java.util.List;

public class LanguageManagerEvent {
    public static class LanguageReloadEvent implements Event<LanguageManagerListener> {
        @Override
        public void dispatch(@NotNull List<LanguageManagerListener> listeners) {
            listeners.forEach(LanguageManagerListener::postLanguageReload);
        }

        @Override
        public Class<LanguageManagerListener> getListenerType() {
            return LanguageManagerListener.class;
        }
    }

    public static class LanguageSelectEvent implements Event<LanguageManagerListener> {
        @Override
        public void dispatch(@NotNull List<LanguageManagerListener> listeners) {
            listeners.forEach(LanguageManagerListener::postLanguageSelect);
        }

        @Override
        public Class<LanguageManagerListener> getListenerType() {
            return LanguageManagerListener.class;
        }
    }
}
