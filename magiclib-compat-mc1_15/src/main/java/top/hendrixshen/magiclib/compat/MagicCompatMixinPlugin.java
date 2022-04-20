package top.hendrixshen.magiclib.compat;

import top.hendrixshen.magiclib.MagicMixinPlugin;
import top.hendrixshen.magiclib.util.MixinUtil;

import java.io.IOException;

public class MagicCompatMixinPlugin extends MagicMixinPlugin {
    @Override
    public void onLoad(String mixinPackage) {
        try {
            MixinUtil.addMagicClass("com.mojang.blaze3d.vertex.VertexFormatCompat");
            MixinUtil.addMagicClass("net.minecraft.network.chat.FormattedTextCompat");
            MixinUtil.addMagicClass("net.minecraft.network.chat.HoverEventCompat");
            MixinUtil.addMagicClass("net.minecraft.network.chat.MutableComponentCompat");
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
        super.onLoad(mixinPackage);
    }
}
