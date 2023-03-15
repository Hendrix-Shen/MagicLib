package top.hendrixshen.magiclib.compat.minecraft.mixin.blaze3d.vertex;

import com.mojang.blaze3d.vertex.BufferBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import top.hendrixshen.magiclib.compat.minecraft.api.blaze3d.vertex.BufferBuilderCompatApi;
import top.hendrixshen.magiclib.util.MiscUtil;

//#if MC <= 11404
//$$ import org.spongepowered.asm.mixin.Shadow;
//$$ import com.mojang.math.Vector4f;
//#endif

@Environment(EnvType.CLIENT)
@Mixin(BufferBuilder.class)
public abstract class MixinBufferBuilder implements BufferBuilderCompatApi {
    //#if MC <= 11404
    //$$ @Shadow
    //$$ public abstract BufferBuilder vertex(double x, double y, double z);
    //#endif

    @SuppressWarnings("ConstantConditions")
    @Override
    public BufferBuilder vertexCompat(Matrix4f matrix4f, float x, float y, float z) {
        //#if MC > 11404
        return (BufferBuilder) ((BufferBuilder) MiscUtil.cast(this)).vertex(matrix4f, x, y, z);
        //#else
        //$$ Vector4f vector4f = new Vector4f(x, y, z, 1.0F);
        //$$ vector4f.transform(matrix4f);
        //$$ return this.vertex(vector4f.x(), vector4f.y(), vector4f.z());
        //#endif
    }
}
