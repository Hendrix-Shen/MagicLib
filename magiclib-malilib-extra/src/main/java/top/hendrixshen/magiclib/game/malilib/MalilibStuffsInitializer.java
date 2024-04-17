package top.hendrixshen.magiclib.game.malilib;

import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.event.InitializationHandler;
import fi.dy.masa.malilib.event.InputEventHandler;
import fi.dy.masa.malilib.hotkeys.IKeybindProvider;
import top.hendrixshen.magiclib.impl.malilib.SharedConstants;

//#if FORGE_LIKE
//$$ import fi.dy.masa.malilib.compat.forge.ForgePlatformCompat;
//#endif

public class MalilibStuffsInitializer {
    public static void init() {
        InitializationHandler.getInstance().registerInitializationHandler(() ->
                ConfigManager.getInstance().registerConfigHandler(SharedConstants.getModIdentifier(),
                        SharedConstants.getConfigHandler()));
        Configs.init();
        InputEventHandler.getKeybindManager().registerKeybindProvider(
                (IKeybindProvider) SharedConstants.getConfigManager());
        //#if FORGE_LIKE
        //$$ MalilibStuffsInitializer.setupForgeConfigGui();
        //#endif
    }

    //#if FORGE_LIKE
    //$$ private static void setupForgeConfigGui() {
    //$$     ForgePlatformCompat.getInstance()
    //$$             .getMod(SharedConstants.getModIdentifier())
    //$$             .registerModConfigScreen(screen -> {
    //$$                 ConfigGui gui = new ConfigGui();
    //$$                 gui.setParentGui(screen);
    //$$                 return gui;
    //$$             });
    //$$ }
    //#endif
}
