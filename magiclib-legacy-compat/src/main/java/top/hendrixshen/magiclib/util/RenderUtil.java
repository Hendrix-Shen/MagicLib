package top.hendrixshen.magiclib.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.ApiStatus;
import top.hendrixshen.magiclib.api.compat.minecraft.client.gui.FontCompat;

@Deprecated
@ApiStatus.ScheduledForRemoval
public class RenderUtil {
    private static final Font TEXT_RENDERER = Minecraft.getInstance().font;

    public static final int TEXT_HEIGHT = RenderUtil.TEXT_RENDERER.lineHeight;
    public static final int TEXT_LINE_HEIGHT = RenderUtil.TEXT_HEIGHT + 1;

    public static int getRenderWidth(String text) {
        return RenderUtil.TEXT_RENDERER.width(text);
    }

    public static int getRenderWidth(Component text) {
        return FontCompat.of(RenderUtil.TEXT_RENDERER).width(text);
    }
}
