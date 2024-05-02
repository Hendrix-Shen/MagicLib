package top.hendrixshen.magiclib.compat.modmenu;

import net.minecraft.client.gui.screens.Screen;
import com.terraformersmc.modmenu.api.ModMenuApi;

//#if MC > 11404
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
//#else
//$$ import java.util.function.Function;
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
        S create(Screen screen);
    }
}
