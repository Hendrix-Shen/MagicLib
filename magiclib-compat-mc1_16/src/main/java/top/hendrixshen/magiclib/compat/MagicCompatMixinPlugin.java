package top.hendrixshen.magiclib.compat;

import top.hendrixshen.magiclib.MagicMixinPlugin;
import top.hendrixshen.magiclib.util.MixinUtil;

import java.io.IOException;

public class MagicCompatMixinPlugin extends MagicMixinPlugin {
    @Override
    public void onLoad(String mixinPackage) {
        try {
            MixinUtil.addMagicClass("net.minecraft.client.renderer.ShaderInstanceCompat");
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
        super.onLoad(mixinPackage);
    }
}
