package top.hendrixshen.magiclib.impl.render.context;

import org.jetbrains.annotations.NotNull;

// CHECKSTYLE.OFF: ImportOrder
//#if MC > 11904
//$$ import net.minecraft.client.gui.GuiGraphics;
//#endif
// CHECKSTYLE.ON: ImportOrder

// CHECKSTYLE.OFF: ImportOrder
//#if MC > 12004
//$$ import top.hendrixshen.magiclib.impl.render.matrix.JomlMatrixStack;
//#else
import top.hendrixshen.magiclib.impl.render.matrix.MinecraftPoseStack;
//#endif
// CHECKSTYLE.ON: ImportOrder

public class LevelRenderContextImpl extends RenderContextImpl {
    public LevelRenderContextImpl(
            //#if MC > 11904
            //$$ GuiGraphics guiGraphics,
            //#endif
            //#if MC > 12004
            //$$ @NotNull JomlMatrixStack matrixStack
            //#else
            @NotNull MinecraftPoseStack matrixStack
            //#endif
    ) {
        super(
                //#if MC > 11904
                //$$ guiGraphics,
                //#endif
                matrixStack
        );
    }
}
