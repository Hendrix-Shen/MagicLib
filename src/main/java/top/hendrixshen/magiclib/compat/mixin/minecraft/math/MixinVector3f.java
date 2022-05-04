package top.hendrixshen.magiclib.compat.mixin.minecraft.math;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import top.hendrixshen.magiclib.compat.minecraft.math.Vector3fCompatApi;

//#if MC <= 11404
//$$ import top.hendrixshen.magiclib.util.MiscUtil;
//#endif

@Mixin(Vector3f.class)
public abstract class MixinVector3f implements Vector3fCompatApi {
    //#if MC > 11404
    @Shadow
    public abstract Quaternion rotationDegrees(float degrees);
    //#endif

    @Override
    public Quaternion rotationDegreesCompat(float degrees) {
        //#if MC > 11404
        return this.rotationDegrees(degrees);
        //#else
        //$$ return new Quaternion(MiscUtil.cast(this), degrees, true);
        //#endif
    }
}
