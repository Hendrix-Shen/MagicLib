package top.hendrixshen.magiclib.entrypoint.legacy;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import top.hendrixshen.magiclib.impl.carpet.CarpetEntrypoint;

public class MagicLibFabric implements ModInitializer, ClientModInitializer, DedicatedServerModInitializer {
    @Override
    public void onInitializeClient() {
    }

    @Override
    public void onInitializeServer() {
    }

    @Override
    public void onInitialize() {
        CarpetEntrypoint.init();
    }
}
