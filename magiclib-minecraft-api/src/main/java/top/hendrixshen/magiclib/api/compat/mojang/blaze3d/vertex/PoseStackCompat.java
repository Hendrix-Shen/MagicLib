package top.hendrixshen.magiclib.api.compat.mojang.blaze3d.vertex;

import org.jetbrains.annotations.NotNull;

// CHECKSTYLE.OFF: ImportOrder
//#if MC > 11605
//$$ import com.mojang.blaze3d.vertex.PoseStack;
//#endif
// CHECKSTYLE.ON: ImportOrder

import top.hendrixshen.magiclib.impl.compat.mojang.blaze3d.vertex.PoseStackCompatImpl;

// CHECKSTYLE.OFF: ImportOrder
//#if MC > 11605
//$$ import top.hendrixshen.magiclib.util.collect.Provider;
//#endif
// CHECKSTYLE.ON: ImportOrder

// CHECKSTYLE.OFF: Indentation
// @formatter:off
public interface
//#if MC > 11605
//$$ PoseStackCompat extends Provider<PoseStack> {
//#else
PoseStackCompat {
//#endif
// @formatter:on
    // CHECKSTYLE.ON: Indentation

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
