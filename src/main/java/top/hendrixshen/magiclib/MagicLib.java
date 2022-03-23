package top.hendrixshen.magiclib;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.hendrixshen.magiclib.config.ConfigHandler;
import top.hendrixshen.magiclib.config.ConfigManager;

public class MagicLib implements ModInitializer, ClientModInitializer {
    private static final int CONFIG_VERSION = 1;

    public static Logger getLogger() {
        return LogManager.getLogger(MagicLibReference.getModId());
    }

    @Override
    public void onInitialize() {
        getLogger().info(String.format("[%s]: Mod initialized - Version: %s", MagicLibReference.getModName(), MagicLibReference.getModVersion()));
    }

    @Override
    public void onInitializeClient() {
        ConfigManager cm = ConfigManager.get(MagicLibReference.getModId());
        cm.parseConfigClass(MagicLibConfigs.class);
        ConfigHandler.register(new ConfigHandler(MagicLibReference.getModId(), cm, CONFIG_VERSION, null, null));
        MagicLibConfigs.init(cm);
    }
}
