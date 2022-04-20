package com.mojang.blaze3d.vertex;

import top.hendrixshen.magiclib.compat.annotation.InnerClass;
import top.hendrixshen.magiclib.compat.annotation.Remap;

@InnerClass(VertexFormat.class)
public class VertexFormatCompat {
    @Remap("net/minecraft/class_293$class_5596")
    public static class Mode {
        @Remap("field_27382")
        public static final Mode QUADS = new Mode(7);
        public final int value;

        public Mode(int value) {
            this.value = value;
        }
    }
}