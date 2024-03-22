//#if FABRIC
package top.hendrixshen.magiclib.api.compat.modmenu;

import io.github.prospector.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.screens.Screen;

//#if MC > 11404
//$$ import io.github.prospector.modmenu.api.ConfigScreenFactory;
//#else
import java.util.function.Function;
//#endif

public interface ModMenuApiCompat extends ModMenuApi {
    ConfigScreenFactoryCompat<?> getConfigScreenFactoryCompat();

    String getModIdCompat();

    //#if MC < 11500
    @Override
    default String getModId() {
        return this.getModIdCompat();
    }
    //#endif

    @Override
    default
    //#if MC > 11404
    //$$ ConfigScreenFactory<? extends Screen> getModConfigScreenFactory()
    //#else
    Function<Screen, ? extends Screen> getConfigScreenFactory()
    //#endif
    {
        return (screen) -> this.getConfigScreenFactoryCompat().create(screen);
    }

    @FunctionalInterface
    interface ConfigScreenFactoryCompat<S extends Screen> {
        S create(Screen screen);
    }
}
//#endif
