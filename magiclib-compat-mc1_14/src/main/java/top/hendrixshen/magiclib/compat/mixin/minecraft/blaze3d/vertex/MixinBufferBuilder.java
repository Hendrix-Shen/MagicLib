package top.hendrixshen.magiclib.compat.mixin.minecraft.blaze3d.vertex;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexConsumerCompat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatCompat;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import top.hendrixshen.magiclib.compat.annotation.Remap;

@Mixin(BufferBuilder.class)
public abstract class MixinBufferBuilder implements VertexConsumerCompat {

    @Shadow
    public abstract void begin(int i, VertexFormat vertexFormat);

    @Shadow
    public abstract BufferBuilder vertex(double par1, double par2, double par3);

    @Remap("method_1328")
    public void begin(VertexFormatCompat.Mode mode, VertexFormat vertexFormat) {
        this.begin(mode.value, vertexFormat);
    }

    @Remap("method_22918")
    public VertexConsumerCompat vertex(Matrix4f matrix4f, float x, float y, float z) {
        Vector4f vector4f = new Vector4f(x, y, z, 1.0F);
        matrix4f.multiplyVector4f(vector4f);
        return (VertexConsumerCompat) this.vertex(vector4f.x(), vector4f.y(), vector4f.z());
    }
}
