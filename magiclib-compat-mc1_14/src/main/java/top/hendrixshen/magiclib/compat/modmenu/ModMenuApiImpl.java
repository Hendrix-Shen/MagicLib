package top.hendrixshen.magiclib.compat.modmenu;

import io.github.prospector.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.screens.Screen;
import top.hendrixshen.magiclib.MagicLibReference;
import top.hendrixshen.magiclib.MagiclibConfigGui;

import java.util.function.Function;

public class ModMenuApiImpl implements ModMenuApi {
    @Override
    public String getModId() {
        return MagicLibReference.getModId();
    }

    @Override
    public Function<Screen, ? extends Screen> getConfigScreenFactory() {
        return (screen) -> {
            MagiclibConfigGui gui = MagiclibConfigGui.getInstance();
            gui.setParent(screen);
            return gui;
        };
    }
}