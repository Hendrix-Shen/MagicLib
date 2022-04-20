package com.mojang.blaze3d.vertex;

import org.lwjgl.opengl.GL11;
import top.hendrixshen.magiclib.compat.annotation.InnerClass;
import top.hendrixshen.magiclib.compat.annotation.Remap;

@InnerClass(VertexFormat.class)
public class VertexFormatCompat {
    @Remap("net/minecraft/class_293$class_5596")
    public static class Mode {
        @Remap("field_27382")
        public static final Mode QUADS = new Mode(GL11.GL_QUADS);
        public final int value;

        public Mode(int value) {
            this.value = value;
        }
    }
}