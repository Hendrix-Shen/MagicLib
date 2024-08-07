//#if FORGE_LIKE
//$$ package top.hendrixshen.magiclib.util.minecraft;
//$$
//$$ import net.minecraft.client.gui.screens.Screen;
//$$
//$$ import java.util.function.UnaryOperator;
//$$
//#if NEO_FORGE
//$$ import net.neoforged.fml.ModList;
//$$
//#if MC > 12002
//$$ import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
//#else
//$$ import net.neoforged.neoforge.client.ConfigScreenHandler;
//#endif
//#else
//$$ import net.minecraftforge.fml.ModList;
//$$
//#if MC > 11802
//$$ import net.minecraftforge.client.ConfigScreenHandler;
//#elseif MC > 11701
//$$ import net.minecraftforge.client.ConfigGuiHandler;
//#else
//$$ import net.minecraftforge.fmlclient.ConfigGuiHandler;
//#endif
//#endif
//$$
//$$ public class ForgePlatformUtil {
//$$     private ForgePlatformUtil() {
//$$         throw new AssertionError("No top.hendrixshen.magiclib.util.minecraft.ForgePlatformUtil instances for you!");
//$$     }
//$$
//$$     public static void registerModConfigScreen(String modId, UnaryOperator<Screen> screenFactory) {
//$$         ModList.get().getModContainerById(modId)
//$$                 .orElseThrow(RuntimeException::new)
//$$                 .registerExtensionPoint(
//#if NEO_FORGE
//#if MC > 12002
//$$                         IConfigScreenFactory.class,
//$$                         (client, screen) -> screenFactory.apply(screen)
//#else
//$$                         ConfigScreenHandler.ConfigScreenFactory.class,
//$$                         () -> new ConfigScreenHandler.ConfigScreenFactory((client, screen) -> screenFactory.apply(screen))
//#endif
//#else
//#if MC > 11802
//$$                         ConfigScreenHandler.ConfigScreenFactory.class,
//$$                         () -> new ConfigScreenHandler.ConfigScreenFactory((client, screen) -> screenFactory.apply(screen))
//#else
//$$                         ConfigGuiHandler.ConfigGuiFactory.class,
//$$                         () -> new ConfigGuiHandler.ConfigGuiFactory((client, screen) -> screenFactory.apply(screen))
//#endif
//#endif
//$$         );
//$$     }
//$$ }
//#endif
