package top.hendrixshen.magiclib.impl.platform.forge;

import top.hendrixshen.magiclib.api.mixin.EmptyMixinPlugin;
import top.hendrixshen.magiclib.entrypoint.core.MagicLibForge;

public class ForgeBootstrapMixinPlugin extends EmptyMixinPlugin {
    @Override
    public void onLoad(String mixinPackage) {
        MagicLibForge.bootstrap();
    }
}
