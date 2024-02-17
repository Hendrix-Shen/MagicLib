package top.hendrixshen.magiclib.impl.event.minecraft;

import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.event.Event;
import top.hendrixshen.magiclib.api.event.minecraft.LanguageSelectListener;

import java.util.List;

public class LanguageSelectEvent implements Event<LanguageSelectListener> {
    @Override
    public void dispatch(@NotNull List<LanguageSelectListener> listener) {
        listener.forEach(LanguageSelectListener::postLanguageSelect);
    }

    @Override
    public Class<LanguageSelectListener> getListenerType() {
        return LanguageSelectListener.class;
    }
}
