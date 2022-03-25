package top.hendrixshen.magiclib.compat.modmenu;

import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import top.hendrixshen.magiclib.MagicLibConfigGui;

public class ModMenuApiImpl implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (screen) -> {
            MagicLibConfigGui gui = MagicLibConfigGui.getInstance();
            gui.setParent(screen);
            return gui;
        };
    }
}