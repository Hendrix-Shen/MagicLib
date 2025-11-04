package top.hendrixshen.magiclib.api.fake.compat;

// CHECKSTYLE.OFF: ImportOrder
//#if MC > 11902
//$$ import org.joml.Matrix4f;
//#endif
// CHECKSTYLE.ON: ImportOrder

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.FormattedCharSequence;

// CHECKSTYLE.OFF: ImportOrder
//#if MC > 11903
//$$ import net.minecraft.client.gui.Font;
//#endif

//#if MC < 11903
import com.mojang.math.Matrix4f;
//#endif
// CHECKSTYLE.ON: ImportOrder

// CHECKSTYLE.OFF: JavadocStyle
/**
 * <li>mc1.14 ~ mc1.15.2: subproject 1.15.2 [dummy]</li>
 * <li>mc1.16 ~ mc1.21.5: subproject 1.16.5 (main project)        &lt;--------</li>
 * <li>mc1.21.6+        : subproject 1.21.8 [dummy]</li>
 */
// CHECKSTYLE.ON: JavadocStyle
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
            // CHECKSTYLE.OFF: NoWhitespaceBefore
            // CHECKSTYLE.OFF: SeparatorWrap
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
            // CHECKSTYLE.ON: SeparatorWrap
            // CHECKSTYLE.ON: NoWhitespaceBefore
    );

    int magiclib$drawInternal(
            // CHECKSTYLE.OFF: NoWhitespaceBefore
            // CHECKSTYLE.OFF: SeparatorWrap
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
            // CHECKSTYLE.ON: SeparatorWrap
            // CHECKSTYLE.ON: NoWhitespaceBefore
    );
}
