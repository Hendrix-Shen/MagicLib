package top.hendrixshen.magiclib.api.event.minecraft;

import top.hendrixshen.magiclib.api.event.Listener;

public interface LanguageManagerListener extends Listener {
    void postLanguageReload();

    void postLanguageSelect();
}
