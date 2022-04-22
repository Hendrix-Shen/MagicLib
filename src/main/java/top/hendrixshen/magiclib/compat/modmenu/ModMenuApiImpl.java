package top.hendrixshen.magiclib.compat.modmenu;

//#if MC >= 11605
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
//#endif
import top.hendrixshen.magiclib.MagicLibConfigGui;

//#if MC <= 11502
//#if MC > 11404
//$$ import io.github.prospector.modmenu.api.ConfigScreenFactory;
//#else
//$$ import java.util.function.Function;
//$$ import net.minecraft.client.gui.screens.Screen;
//#endif
//$$ import io.github.prospector.modmenu.api.ModMenuApi;
//#endif

//#if MC <= 11404
//$$ import top.hendrixshen.magiclib.MagicLibReference;
//#endif


public class ModMenuApiImpl implements ModMenuApi {
    @Override
    //#if MC > 11404
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        //#else
        //$$ public Function<Screen, ? extends Screen> getConfigScreenFactory() {
        //#endif

        return (screen) -> {
            MagicLibConfigGui gui = MagicLibConfigGui.getInstance();
            gui.setParent(screen);
            return gui;
        };
    }

    //#if MC <= 11404
    //$$ @Override
    //$$ public String getModId() {
    //$$     return MagicLibReference.getModId();
    //$$ }
    //#endif
}