package net.minecraft.client.renderer;

import com.mojang.blaze3d.vertex.BufferBuilder;
import top.hendrixshen.magiclib.compat.annotation.Remap;

@Remap("net/minecraft/class_4597")
public interface MultiBufferSourceCompat {
    @Remap("net/minecraft/class_4597$class_4598")
    class BufferSourceCompat implements MultiBufferSourceCompat {
        @Remap("method_22993")
        public void endBatch() {
        }
    }

    @Remap("method_22991")
    static BufferSourceCompat immediate(BufferBuilder bufferBuilder) {
        return new BufferSourceCompat();
    }

}