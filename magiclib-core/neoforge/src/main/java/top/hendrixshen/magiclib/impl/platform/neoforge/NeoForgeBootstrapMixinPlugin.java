package top.hendrixshen.magiclib.impl.platform.neoforge;

import top.hendrixshen.magiclib.api.mixin.EmptyMixinPlugin;
import top.hendrixshen.magiclib.entrypoint.core.MagicLibNeoForge;

public class NeoForgeBootstrapMixinPlugin extends EmptyMixinPlugin {
    @Override
    public void onLoad(String mixinPackage) {
        MagicLibNeoForge.bootstrap();
    }
}
