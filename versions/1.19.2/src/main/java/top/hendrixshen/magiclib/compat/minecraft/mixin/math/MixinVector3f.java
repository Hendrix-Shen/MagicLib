package top.hendrixshen.magiclib.compat.minecraft.mixin.math;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import top.hendrixshen.magiclib.compat.minecraft.api.math.Vector3fCompatApi;

//#if MC < 11500
//$$ import top.hendrixshen.magiclib.util.MiscUtil;
//#endif

/**
 * The implementation for mc [1.14.4, ~)
 */
@Environment(EnvType.CLIENT)
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
