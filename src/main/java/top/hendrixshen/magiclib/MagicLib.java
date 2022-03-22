package top.hendrixshen.magiclib;

import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.hendrixshen.magiclib.config.ConfigManager;
import top.hendrixshen.magiclib.util.FabricUtil;

public class MagicLib implements ModInitializer {
    public static Logger getLogger() {
        return LogManager.getLogger(MagicLibReference.getModId());
    }
    public static ConfigManager cm;

    @Override
    public void onInitialize() {
        getLogger().info(String.format("[%s]: Mod initialized - Version: %s", MagicLibReference.getModName(), MagicLibReference.getModVersion()));
        if (FabricUtil.isModLoaded("malilib")) {
            cm = new ConfigManager(MagicLibReference.getModId());
            cm.parseConfigClass(MagicLibConfigs.class);
            MagicLibConfigs.init();
        }
    }
}
