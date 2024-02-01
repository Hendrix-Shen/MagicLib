package top.hendrixshen.magiclib.entrypoint.core;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import org.jetbrains.annotations.ApiStatus;
import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.impl.mixin.extension.MagicExtensions;
import top.hendrixshen.magiclib.impl.platform.fabric.FabricPlatformImpl;

public class MagicLibFabric implements ModInitializer, ClientModInitializer, DedicatedServerModInitializer {
    @Override
    public void onInitialize() {
    }

    @Override
    public void onInitializeClient() {
    }

    @Override
    public void onInitializeServer() {
    }

    @ApiStatus.Internal
    public static void bootstrap() {
        MagicLib.getInstance().getPlatformManage().register(FabricPlatformImpl.getInstance());
        MagicExtensions.init();
    }
}
