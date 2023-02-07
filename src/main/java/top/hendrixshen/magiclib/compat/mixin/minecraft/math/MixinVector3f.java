package top.hendrixshen.magiclib.compat.mixin.minecraft.math;

import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
//#if MC < 11903
//$$ import org.spongepowered.asm.mixin.Shadow;
//#endif
import top.hendrixshen.magiclib.compat.minecraft.math.Vector3fCompatApi;
import top.hendrixshen.magiclib.util.MiscUtil;

//#if MC <= 11404
//$$ import top.hendrixshen.magiclib.util.MiscUtil;
//#endif

@Mixin(Vector3f.class)
public abstract class MixinVector3f implements Vector3fCompatApi {
    //#if MC > 11404 && MC < 11903
    //$$ @Shadow
    //$$ public abstract Quaternion rotationDegrees(float degrees);
    //#endif

    @Override
    public Quaternionf rotationDegreesCompat(float degrees) {
        //#if MC >= 11903
        return new Quaternionf().rotateAxis(degrees, MiscUtil.cast(this));
        //#elseif MC > 11404
        //$$ return this.rotationDegrees(degrees);
        //#else
        //$$ return new Quaternion(MiscUtil.cast(this), degrees, true);
        //#endif
    }
}
