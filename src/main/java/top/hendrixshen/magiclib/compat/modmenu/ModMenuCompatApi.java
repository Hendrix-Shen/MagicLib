package top.hendrixshen.magiclib.compat.modmenu;

//#if MC >= 11605
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
//#endif

import net.minecraft.client.gui.screens.Screen;

//#if MC <= 11502
//#if MC > 11404
//$$ import io.github.prospector.modmenu.api.ConfigScreenFactory;
//#else
//$$ import java.util.function.Function;
//#endif
//$$ import io.github.prospector.modmenu.api.ModMenuApi;
//#endif


public interface ModMenuCompatApi extends ModMenuApi {

    ConfigScreenFactoryCompat<?> getConfigScreenFactoryCompat();

    String getModIdCompat();

    @Override
    //#if MC > 11404
    default ConfigScreenFactory<?> getModConfigScreenFactory() {
        //#else
        //$$ default Function<Screen, ? extends Screen> getConfigScreenFactory() {
        //#endif
        return (screen) -> this.getConfigScreenFactoryCompat().create(screen);
    }

    //#if MC <= 11404
    //$$ @Override
    //$$ default String getModId() {
    //$$     return this.getModIdCompat();
    //$$ }
    //#endif

    @FunctionalInterface
    interface ConfigScreenFactoryCompat<S extends Screen> {
        S create(Screen var1);
    }

}
