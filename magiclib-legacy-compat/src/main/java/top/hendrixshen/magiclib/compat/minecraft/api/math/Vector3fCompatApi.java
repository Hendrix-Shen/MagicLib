package top.hendrixshen.magiclib.compat.minecraft.api.math;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.ApiStatus;
import top.hendrixshen.magiclib.compat.api.UnImplCompatApiException;

@Deprecated
@ApiStatus.ScheduledForRemoval
@Environment(EnvType.CLIENT)
public interface Vector3fCompatApi {
    Vector3f XN = new Vector3f(-1.0F, 0.0F, 0.0F);
    Vector3f XP = new Vector3f(1.0F, 0.0F, 0.0F);
    Vector3f YN = new Vector3f(0.0F, -1.0F, 0.0F);
    Vector3f YP = new Vector3f(0.0F, 1.0F, 0.0F);
    Vector3f ZN = new Vector3f(0.0F, 0.0F, -1.0F);
    Vector3f ZP = new Vector3f(0.0F, 0.0F, 1.0F);
    Vector3f ZERO = new Vector3f(0.0F, 0.0F, 0.0F);

    default Quaternion rotationDegreesCompat(float degrees) {
        throw new UnImplCompatApiException();
    }
    //#if MC < 11500
    //$$ default Quaternion rotationDegrees(float degrees) {
    //$$     return this.rotationDegreesCompat(degrees);
    //$$ }
    //#endif
}
