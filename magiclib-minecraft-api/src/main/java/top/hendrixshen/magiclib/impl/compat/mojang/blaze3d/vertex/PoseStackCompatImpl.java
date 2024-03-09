package top.hendrixshen.magiclib.impl.compat.mojang.blaze3d.vertex;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.math.Quaternion;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.compat.AbstractCompat;
import top.hendrixshen.magiclib.api.compat.mojang.blaze3d.vertex.PoseStackCompat;

//#if MC > 11404
//$$ import com.mojang.blaze3d.vertex.PoseStack;
//#endif

@Environment(EnvType.CLIENT)
public class PoseStackCompatImpl extends AbstractCompat<
        //#if MC > 11404
        //$$ PoseStack
        //#else
        Object
        //#endif
        > implements PoseStackCompat {
    public PoseStackCompatImpl(@NotNull
                               //#if MC > 11404
                               //$$ PoseStack type
                               //#else
                               Object type
                               //#endif

    ) {
        super(type);
    }

    @Override
    public void translate(double x, double y, double z) {
        //#if MC > 11404
        //$$ this.get().translate(x, y, z);
        //#else
        GlStateManager.translated(x, y, z);
        //#endif
    }

    @Override
    public void scale(float x, float y, float z) {
        //#if MC > 11404
        //$$ this.get().scale(x, y, z);
        //#else
        GlStateManager.scaled(x, y, z);
        //#endif
    }

    @Override
    public void mulPose(@NotNull Quaternion quaternion) {
        // //#if MC > 11404
        // //$$ this.get().mulPose(quaternion);
        // //#else
        // GlStateManager.rotatef(0, quaternion.i(), quaternion.j(), quaternion.k());
        // //#endif
        // TODO
    }

    @Override
    public void pushPose() {
        //#if MC > 11404
        //$$ this.get().pushPose();
        //#else
        GlStateManager.pushMatrix();
        //#endif
    }

    @Override
    public void popPose() {
        //#if MC > 11404
        //$$ this.get().popPose();
        //#else
        GlStateManager.popMatrix();
        //#endif
    }
}
