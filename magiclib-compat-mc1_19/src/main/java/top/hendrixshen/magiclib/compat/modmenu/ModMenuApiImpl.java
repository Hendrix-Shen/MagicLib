package top.hendrixshen.magiclib.compat.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
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