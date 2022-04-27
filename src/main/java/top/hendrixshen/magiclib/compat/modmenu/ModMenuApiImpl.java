package top.hendrixshen.magiclib.compat.modmenu;

import top.hendrixshen.magiclib.MagicLibConfigGui;
import top.hendrixshen.magiclib.MagicLibReference;

public class ModMenuApiImpl implements ModMenuCompatApi {

    @Override
    public ConfigScreenFactoryCompat<?> getConfigScreenFactoryCompat() {
        return (screen) -> {
            MagicLibConfigGui gui = MagicLibConfigGui.getInstance();
            gui.setParentGui(screen);
            return gui;
        };
    }

    @Override
    public String getModIdCompat() {
        return MagicLibReference.CURRENT_MOD_ID;
    }

}