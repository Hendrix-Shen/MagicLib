package top.hendrixshen.magiclib;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.hendrixshen.magiclib.config.ConfigHandler;
import top.hendrixshen.magiclib.config.ConfigManager;
import top.hendrixshen.magiclib.dependency.annotation.Dependencies;
import top.hendrixshen.magiclib.dependency.annotation.Dependency;
import top.hendrixshen.magiclib.util.FabricUtil;

public class MagicLib implements ModInitializer, ClientModInitializer {
    private static final int CONFIG_VERSION = 1;

    public static Logger getLogger() {
        return LogManager.getLogger(MagicLibReference.getModId());
    }

    @Override
    public void onInitialize() {
        FabricUtil.compatVersionCheck();
        getLogger().info("[{}]: Mod initialized - Version: {}", MagicLibReference.getModName(), MagicLibReference.getModVersion());
    }

    @Dependencies(and = @Dependency(value = "malilib"))
    @Override
    public void onInitializeClient() {
        ConfigManager cm = ConfigManager.get(MagicLibReference.getModId());
        cm.parseConfigClass(MagicLibConfigs.class);
        ConfigHandler.register(new ConfigHandler(MagicLibReference.getModId(), cm, CONFIG_VERSION, null, null));
        MagicLibConfigs.init(cm);
    }
}
