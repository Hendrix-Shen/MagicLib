package top.hendrixshen.magiclib.mixin.minecraft.compat;

// CHECKSTYLE.OFF: ImportOrder
//#if MC > 11902
//$$ import org.joml.Matrix4f;
//#endif
// CHECKSTYLE.ON: ImportOrder

import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.FormattedCharSequence;

// CHECKSTYLE.OFF: ImportOrder
//#if MC < 11903
import com.mojang.math.Matrix4f;
//#endif
// CHECKSTYLE.ON: ImportOrder

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import top.hendrixshen.magiclib.api.fake.compat.FontAccessor;

// CHECKSTYLE.OFF: JavadocStyle
/**
 * <li>mc1.14 ~ mc1.15.2: subproject 1.15.2 [dummy]</li>
 * <li>mc1.15 ~ mc1.21.5: subproject 1.16.5 (main project)        &lt;--------</li>
 * <li>mc1.21.6+        : subproject 1.21.8 [dummy]</li>
 */
// CHECKSTYLE.ON: JavadocStyle
@Mixin(Font.class)
public abstract class FontMixin implements FontAccessor {
    @Shadow
    protected abstract int drawInternal(
            String text,
            float x,
            float y,
            int color,
            boolean dropShadow,
            Matrix4f matrix4f,
            MultiBufferSource buffer,
            //#if MC > 11903
            //$$ Font.DisplayMode displayMode,
            //#else
            boolean seeThrough,
            //#endif
            int backgroundColor,
            int packedLightCoords,
            //#if MC > 12101
            //$$ boolean inverseDepth
            //#else
            boolean bidirectional
            //#endif
    );

    @Shadow
    protected abstract int drawInternal(
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
            //$$ Font.DisplayMode displayMode,
            //#else
            boolean seeThrough,
            //#endif
            int backgroundColor,
            int packedLightCoords
            //#if MC > 12101
            //$$ , boolean inverseDepth
            //#endif
            // CHECKSTYLE.ON: SeparatorWrap
            // CHECKSTYLE.ON: NoWhitespaceBefore
    );

    //#if MC > 12101
    //$$ @Shadow
    //$$ public abstract String bidirectionalShaping(String string);
    //#endif

    @Override
    public int magiclib$drawInternal(
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
    ) {
        //#if MC > 12101
        //$$ if (bidirectional) {
        //$$     text = this.bidirectionalShaping(text);
        //$$ }
        //#endif

        return this.drawInternal(
                text,
                x,
                y,
                color,
                dropShadow,
                matrix4f,
                buffer,
                seeThroughOrDisplayMode,
                backgroundColor,
                packedLightCoords,
                //#if MC > 12101
                //$$ inverseDepth
                //#else
                bidirectional
                //#endif
        );
    }

    @Override
    public int magiclib$drawInternal(
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
    ) {
        return this.drawInternal(
                // CHECKSTYLE.OFF: NoWhitespaceBefore
                // CHECKSTYLE.OFF: SeparatorWrap
                formattedCharSequence,
                x,
                y,
                color,
                dropShadow,
                matrix4f,
                buffer,
                seeThroughOrDisplayMode,
                backgroundColor,
                packedLightCoords
                //#if MC > 12101
                //$$ , inverseDepth
                //#endif
                // CHECKSTYLE.ON: SeparatorWrap
                // CHECKSTYLE.ON: NoWhitespaceBefore
        );
    }
}
