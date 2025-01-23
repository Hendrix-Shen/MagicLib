//#if FABRIC

package top.hendrixshen.magiclib.api.compat.modmenu;

import com.terraformersmc.modmenu.api.ModMenuApi;

// CHECKSTYLE.OFF: ImportOrder
//#if MC > 11404
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
//#endif
// CHECKSTYLE.ON: ImportOrder

import net.minecraft.client.gui.screens.Screen;

// CHECKSTYLE.OFF: ImportOrder
//#if MC < 11500
//$$ import java.util.function.Function;
//#endif
// CHECKSTYLE.ON: ImportOrder

public interface ModMenuApiCompat extends ModMenuApi {
    ConfigScreenFactoryCompat<?> getConfigScreenFactoryCompat();

    String getModIdCompat();

    //#if MC < 11500
    //$$ @Override
    //$$ default String getModId() {
    //$$     return this.getModIdCompat();
    //$$ }
    //#endif

    // CHECKSTYLE.OFF: Indentation
    // @formatter:off
    @Override
    default
    //#if MC > 11404
    ConfigScreenFactory<? extends Screen> getModConfigScreenFactory() {
    //#else
    //$$ Function<Screen, ? extends Screen> getConfigScreenFactory() {
    //#endif
    // CHECKSTYLE.ON: Indentation
        // @formatter:on
        return (screen) -> this.getConfigScreenFactoryCompat().create(screen);
    }

    @FunctionalInterface
    interface ConfigScreenFactoryCompat<S extends Screen> {
        S create(Screen screen);
    }
}
//#endif
