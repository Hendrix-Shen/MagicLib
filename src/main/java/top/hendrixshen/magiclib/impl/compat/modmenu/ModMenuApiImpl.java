package top.hendrixshen.magiclib.impl.compat.modmenu;

import top.hendrixshen.magiclib.MagicLibReference;
import top.hendrixshen.magiclib.compat.modmenu.ModMenuCompatApi;
import top.hendrixshen.magiclib.impl.config.ConfigEntrypoint;
import top.hendrixshen.magiclib.impl.config.MagicLibConfigGui;

public class ModMenuApiImpl implements ModMenuCompatApi {
    @Override
    public ConfigScreenFactoryCompat<?> getConfigScreenFactoryCompat() {
        return ConfigEntrypoint::getGui;
    }

    @Override
    public String getModIdCompat() {
        return MagicLibReference.getModIdentifierCurrent();
    }
}
