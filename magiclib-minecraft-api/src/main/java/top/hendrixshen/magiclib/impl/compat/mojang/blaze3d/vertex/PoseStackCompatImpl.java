package top.hendrixshen.magiclib.impl.compat.mojang.blaze3d.vertex;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import top.hendrixshen.magiclib.api.compat.mojang.blaze3d.vertex.PoseStackCompat;

//#if MC > 11605
//$$ import com.mojang.blaze3d.vertex.PoseStack;
//$$ import org.jetbrains.annotations.NotNull;
//$$ import top.hendrixshen.magiclib.api.compat.AbstractCompat;
//#else
import com.mojang.blaze3d.systems.RenderSystem;
//#endif

@Environment(EnvType.CLIENT)
public class PoseStackCompatImpl
        //#if MC > 11605
        //$$ extends AbstractCompat<PoseStack>
        //#endif
        implements PoseStackCompat {
    public PoseStackCompatImpl(
            //#if MC > 11605
            //$$ @NotNull PoseStack type
            //#endif
    ) {
        //#if MC > 11605
        //$$ super(type);
        //#endif
    }

    @Override
    public void translate(double x, double y, double z) {
        //#if MC > 11605
        //$$ this.get().translate(x, y, z);
        //#else
        RenderSystem.translated(x, y, z);
        //#endif
    }

    @Override
    public void scale(float x, float y, float z) {
        //#if MC > 11605
        //$$ this.get().scale(x, y, z);
        //#else
        RenderSystem.scaled(x, y, z);
        //#endif
    }

    @Override
    public void pushPose() {
        //#if MC > 11605
        //$$ this.get().pushPose();
        //#else
        RenderSystem.pushMatrix();
        //#endif
    }

    @Override
    public void popPose() {
        //#if MC > 11605
        //$$ this.get().popPose();
        //#else
        RenderSystem.popMatrix();
        //#endif
    }
}
