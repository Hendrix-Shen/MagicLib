package top.hendrixshen.magiclib.game.malilib;

import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.event.InitializationHandler;
import fi.dy.masa.malilib.event.InputEventHandler;
import fi.dy.masa.malilib.hotkeys.IKeybindProvider;
import top.hendrixshen.magiclib.impl.malilib.SharedConstants;

//#if FORGE_LIKE
//$$ import top.hendrixshen.magiclib.util.minecraft.ForgePlatformUtil;
//#endif

public class MalilibStuffsInitializer {
    public static void init() {
        InitializationHandler.getInstance().registerInitializationHandler(() ->
                ConfigManager.getInstance().registerConfigHandler(
                        top.hendrixshen.magiclib.SharedConstants.getMagiclibIdentifier(),
                        SharedConstants.getConfigHandler()));
        Configs.init();
        InputEventHandler.getKeybindManager().registerKeybindProvider(
                (IKeybindProvider) SharedConstants.getConfigManager());
        //#if FORGE_LIKE
        //$$ MalilibStuffsInitializer.setupForgeConfigGui();
        //#endif
    }

    //#if FORGE_LIKE
    //$$     private static void setupForgeConfigGui() {
    //$$         ForgePlatformUtil.registerModConfigScreen(SharedConstants.getModIdentifier(),
    //$$                 screen -> {
    //$$                     ConfigGui gui = new ConfigGui();
    //#if MC > 11903
    //$$                     gui.setParent(screen);
    //#else
    //$$                     gui.setParentGui(screen);
    //#endif
    //$$                     return gui;
    //$$                 });
    //$$     }
    //#endif
}
