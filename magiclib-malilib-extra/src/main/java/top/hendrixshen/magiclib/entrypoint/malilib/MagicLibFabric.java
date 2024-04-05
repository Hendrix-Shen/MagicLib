//#if FABRIC
package top.hendrixshen.magiclib.entrypoint.malilib;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import top.hendrixshen.magiclib.game.malilib.MalilibStuffsInitializer;

public class MagicLibFabric implements ModInitializer, ClientModInitializer, DedicatedServerModInitializer {
    @Override
    public void onInitializeClient() {
        MalilibStuffsInitializer.init();
    }

    @Override
    public void onInitializeServer() {
    }

    @Override
    public void onInitialize() {
    }
}
//#endif
