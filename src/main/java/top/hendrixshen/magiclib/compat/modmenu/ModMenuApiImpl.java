package top.hendrixshen.magiclib.compat.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import top.hendrixshen.magiclib.config.GuiConfigs;

public class ModMenuApiImpl implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {

        return (screen) -> {
            GuiConfigs gui = GuiConfigs.getInstance();
            gui.setParent(screen);
            return gui;
        };
    }
}