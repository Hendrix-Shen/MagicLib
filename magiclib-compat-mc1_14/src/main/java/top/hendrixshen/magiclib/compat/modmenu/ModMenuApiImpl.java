package top.hendrixshen.magiclib.compat.modmenu;

import io.github.prospector.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.screens.Screen;
import top.hendrixshen.magiclib.MagicLibConfigGui;
import top.hendrixshen.magiclib.MagicLibReference;

import java.util.function.Function;

public class ModMenuApiImpl implements ModMenuApi {
    @Override
    public String getModId() {
        return MagicLibReference.getModId();
    }

    @Override
    public Function<Screen, ? extends Screen> getConfigScreenFactory() {
        return (screen) -> {
            MagicLibConfigGui gui = MagicLibConfigGui.getInstance();
            gui.setParent(screen);
            return gui;
        };
    }
}