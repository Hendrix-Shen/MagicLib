package top.hendrixshen.magiclib.compat;

import net.fabricmc.api.ClientModInitializer;
import top.hendrixshen.magiclib.dependency.annotation.Dependencies;
import top.hendrixshen.magiclib.dependency.annotation.Dependency;

public class MagiclibCompat implements ClientModInitializer {
    @Dependencies(and = @Dependency(value = "modmenu", versionPredicate = ">=1.16.22", optional = true))
    @Override
    public void onInitializeClient() {
    }
}
