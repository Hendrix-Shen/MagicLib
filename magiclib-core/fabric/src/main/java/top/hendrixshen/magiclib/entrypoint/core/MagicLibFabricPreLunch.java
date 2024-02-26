package top.hendrixshen.magiclib.entrypoint.core;

import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import top.hendrixshen.magiclib.impl.dependency.EntryPointDependency;

public class MagicLibFabricPreLunch implements PreLaunchEntrypoint {
    @Override
    public void onPreLaunch() {
        String headless = System.getProperty("java.awt.headless");

        if (Boolean.parseBoolean(headless)) {
            System.setProperty("java.awt.headless", "");
        }

        EntryPointDependency.getInstance().check();
    }
}
