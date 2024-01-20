package top.hendrixshen.magiclib.impl.platform.fabric;

import top.hendrixshen.magiclib.api.mixin.EmptyMixinPlugin;
import top.hendrixshen.magiclib.entrypoint.core.MagicLibFabric;

public class FabricBootstrapMixinPlugin extends EmptyMixinPlugin {
    @Override
    public void onLoad(String mixinPackage) {
        MagicLibFabric.bootstrap();
    }
}
