package top.hendrixshen.magiclib.compat;

import top.hendrixshen.magiclib.MagicMixinPlugin;
import top.hendrixshen.magiclib.util.MixinUtil;

import java.io.IOException;

public class MagicCompatMixinPlugin extends MagicMixinPlugin {
    @Override
    public void onLoad(String mixinPackage) {
        try {
            MixinUtil.addMagicClass("com.mojang.blaze3d.vertex.PoseStackCompat");
            MixinUtil.addMagicClass("com.mojang.blaze3d.vertex.VertexConsumerCompat");
            MixinUtil.addMagicClass("com.mojang.blaze3d.vertex.VertexFormatCompat");
            MixinUtil.addMagicClass("com.mojang.math.Matrix3fCompat");
            MixinUtil.addMagicClass("net.minecraft.client.renderer.MultiBufferSourceCompat");
            MixinUtil.addMagicClass("net.minecraft.client.renderer.ShaderInstanceCompat");
            MixinUtil.addMagicClass("net.minecraft.network.chat.FormattedTextCompat");
            MixinUtil.addMagicClass("net.minecraft.network.chat.HoverEventCompat");
            MixinUtil.addMagicClass("net.minecraft.network.chat.MutableComponentCompat");
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
        super.onLoad(mixinPackage);
    }
}
