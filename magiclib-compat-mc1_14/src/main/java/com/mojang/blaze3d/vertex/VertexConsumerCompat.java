package com.mojang.blaze3d.vertex;

import top.hendrixshen.magiclib.compat.annotation.Remap;

@Remap("net/minecraft/class_4588")
public interface VertexConsumerCompat {

    @Remap("method_1336")
    VertexConsumerCompat color(int i, int j, int k, int l);

    @Remap("method_22915")
    default VertexConsumerCompat color(float f, float g, float h, float i) {
        return this.color((int) (f * 255), (int) (g * 255), (int) (h * 255), (int) (i * 255.0F));
    }
}
