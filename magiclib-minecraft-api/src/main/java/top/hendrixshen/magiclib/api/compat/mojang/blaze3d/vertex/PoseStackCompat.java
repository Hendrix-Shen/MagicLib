package top.hendrixshen.magiclib.api.compat.mojang.blaze3d.vertex;

import com.mojang.math.Quaternion;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.impl.compat.mojang.blaze3d.vertex.PoseStackCompatImpl;
import top.hendrixshen.magiclib.util.collect.Provider;

//#if MC > 11404
//$$ import com.mojang.blaze3d.vertex.PoseStack;
//#endif

@Environment(EnvType.CLIENT)
public interface PoseStackCompat extends Provider<
        //#if MC > 11404
        //$$ PoseStack
        //#else
        Object
        //#endif
        > {
    static @NotNull PoseStackCompat of(
            //#if MC > 11404
            //$$ @NotNull PoseStack poseStack
            //#endif
    ) {
        return new PoseStackCompatImpl(
                //#if MC > 11404
                //$$ poseStack
                //#else
                new Object()
                //#endif
        );
    }


    void translate(double x, double y, double z);

    void scale(float x, float y, float z);

    void mulPose(@NotNull Quaternion quaternion);

    void pushPose();

    void popPose();
}
