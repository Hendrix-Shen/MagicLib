package top.hendrixshen.magiclib.game.malilib;

import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.event.InitializationHandler;
import fi.dy.masa.malilib.event.InputEventHandler;
import fi.dy.masa.malilib.hotkeys.IKeybindProvider;
import top.hendrixshen.magiclib.impl.malilib.SharedConstants;

//#if FORGE_LIKE
//#if MC > 12006
//$$ import org.thinkingstudio.mafglib.util.NeoUtils;
//#elseif MC > 11701 && MC != 11904
//$$ import org.thinkingstudio.mafglib.util.ForgePlatformUtils;
//#else
//$$ import fi.dy.masa.malilib.compat.forge.ForgePlatformCompat;
//#endif
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
    //$$ private static void setupForgeConfigGui() {
    //#if MC > 12006
    //$$     NeoUtils.getInstance().registerModConfigScreen(SharedConstants.getModIdentifier(),
    //#elseif MC > 11701 && MC != 11904
    //$$     ForgePlatformUtils.getInstance().registerModConfigScreen(SharedConstants.getModIdentifier(),
    //#else
    //$$     ForgePlatformCompat.getInstance()
    //$$         .getMod(SharedConstants.getModIdentifier())
    //$$         .registerModConfigScreen(
    //#endif
    //$$             screen -> {
    //$$                 ConfigGui gui = new ConfigGui();
    //#if MC > 11903
    //$$                 gui.setParent(screen);
    //#else
    //$$                 gui.setParentGui(screen);
    //#endif
    //$$                 return gui;
    //$$             });
    //$$ }
    //#endif
}
