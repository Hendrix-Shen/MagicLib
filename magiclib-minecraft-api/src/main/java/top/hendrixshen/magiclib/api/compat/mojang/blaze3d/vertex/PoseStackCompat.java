package top.hendrixshen.magiclib.api.compat.mojang.blaze3d.vertex;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.impl.compat.mojang.blaze3d.vertex.PoseStackCompatImpl;

//#if MC > 11605
//$$ import com.mojang.blaze3d.vertex.PoseStack;
//$$ import top.hendrixshen.magiclib.util.collect.Provider;
//#endif

@Environment(EnvType.CLIENT)
public interface PoseStackCompat
        //#if MC > 11605
        //$$ extends Provider<PoseStack>
        //#endif
{
    static @NotNull PoseStackCompat of(
            //#if MC > 11605
            //$$ @NotNull PoseStack poseStack
            //#endif
    ) {
        return new PoseStackCompatImpl(
                //#if MC > 11605
                //$$ poseStack
                //#endif
        );
    }

    void translate(double x, double y, double z);

    void scale(float x, float y, float z);

    void pushPose();

    void popPose();
}
