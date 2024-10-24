package top.hendrixshen.magiclib.api.fake.compat;

import com.mojang.math.Matrix4f;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.FormattedCharSequence;

//#if MC > 11903
//$$ import net.minecraft.client.gui.Font;
//#endif

@Environment(EnvType.CLIENT)
public interface FontAccessor {
    //#if MC > 12101
    //$$ default int magiclib$drawInternal(
    //$$         String text,
    //$$         float x,
    //$$         float y,
    //$$         int color,
    //$$         boolean dropShadow,
    //$$         Matrix4f matrix4f,
    //$$         MultiBufferSource buffer,
    //$$         Font.DisplayMode displayMode,
    //$$         int backgroundColor,
    //$$         int packedLightCoords,
    //$$         boolean bidirectional
    //$$ ) {
    //$$     return this.magiclib$drawInternal(text, x, y, color, dropShadow, matrix4f, buffer, displayMode, backgroundColor, packedLightCoords, bidirectional, true);
    //$$ }
    //$$
    //$$ default int magiclib$drawInternal(
    //$$         FormattedCharSequence formattedCharSequence,
    //$$         float x,
    //$$         float y,
    //$$         int color,
    //$$         boolean dropShadow,
    //$$         Matrix4f matrix4f,
    //$$         MultiBufferSource buffer,
    //$$         Font.DisplayMode displayMode,
    //$$         int backgroundColor,
    //$$         int packedLightCoords
    //$$ ) {
    //$$     return this.magiclib$drawInternal(formattedCharSequence, x, y, color, dropShadow, matrix4f, buffer, displayMode, backgroundColor, packedLightCoords, true);
    //$$ }
    //#endif

    int magiclib$drawInternal(
            String text,
            float x,
            float y,
            int color,
            boolean dropShadow,
            Matrix4f matrix4f,
            MultiBufferSource buffer,
            //#if MC > 11903
            //$$ Font.DisplayMode seeThroughOrDisplayMode,
            //#else
            boolean seeThroughOrDisplayMode,
            //#endif
            int backgroundColor,
            int packedLightCoords,
            boolean bidirectional
            //#if MC > 12101
            //$$ , boolean inverseDepth
            //#endif
    );

    int magiclib$drawInternal(
            FormattedCharSequence formattedCharSequence,
            float x,
            float y,
            int color,
            boolean dropShadow,
            Matrix4f matrix4f,
            MultiBufferSource buffer,
            //#if MC > 11903
            //$$ Font.DisplayMode seeThroughOrDisplayMode,
            //#else
            boolean seeThroughOrDisplayMode,
            //#endif
            int backgroundColor,
            int packedLightCoords
            //#if MC > 12101
            //$$ , boolean inverseDepth
            //#endif
    );
}
