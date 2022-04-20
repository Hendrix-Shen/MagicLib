package top.hendrixshen.magiclib.compat.mixin.minecraft.blaze3d.vertex;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatCompat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import top.hendrixshen.magiclib.compat.annotation.Remap;

@Mixin(BufferBuilder.class)
public abstract class MixinBufferBuilder {

    @Shadow
    public abstract void begin(int i, VertexFormat vertexFormat);

    @Remap("method_1328")
    public void begin(VertexFormatCompat.Mode mode, VertexFormat vertexFormat) {
        this.begin(mode.value, vertexFormat);
    }
}
