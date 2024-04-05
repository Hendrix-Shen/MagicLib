//#if FABRIC
package top.hendrixshen.magiclib.game.malilib;

import top.hendrixshen.magiclib.api.compat.modmenu.ModMenuApiCompat;
import top.hendrixshen.magiclib.impl.malilib.SharedConstants;

public class ModMenuImpl implements ModMenuApiCompat {
    @Override
    public ConfigScreenFactoryCompat<?> getConfigScreenFactoryCompat() {
        return (screen) -> {
            ConfigGui configGui = new ConfigGui();
            //#if MC > 11903
            //$$ configGui.setParent(screen);
            //#else
            configGui.setParentGui(screen);
            //#endif
            return configGui;
        };
    }

    @Override
    public String getModIdCompat() {
        return SharedConstants.getModIdentifierCurrent();
    }
}
//#endif
