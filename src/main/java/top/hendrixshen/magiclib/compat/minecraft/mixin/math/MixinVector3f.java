package top.hendrixshen.magiclib.compat.minecraft.mixin.math;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import top.hendrixshen.magiclib.compat.minecraft.api.math.Vector3fCompatApi;
import top.hendrixshen.magiclib.compat.preprocess.api.DummyClass;

//#if MC <= 11404
//$$ import top.hendrixshen.magiclib.util.MiscUtil;
//#endif

//#if MC < 11903
//$$ import com.mojang.math.Quaternion;
//$$ import com.mojang.math.Vector3f;
//$$ import org.spongepowered.asm.mixin.Shadow;
//#endif

@Environment(EnvType.CLIENT)
//#if MC > 11902
@Mixin(DummyClass.class)
//#else
//$$ @Mixin(Vector3f.class)
//#endif
public abstract class MixinVector3f implements Vector3fCompatApi {
    //#if MC < 11903
    //#if MC > 11404
    //$$ @Shadow
    //$$ public abstract Quaternion rotationDegrees(float degrees);
    //#endif
    //$$
    //$$ @Override
    //$$ public Quaternion rotationDegreesCompat(float degrees) {
    //#if MC > 11404
    //$$     return this.rotationDegrees(degrees);
    //#else
    //$$     return new Quaternion(MiscUtil.cast(this), degrees, true);
    //#endif
    //$$ }
    //#endif
}
