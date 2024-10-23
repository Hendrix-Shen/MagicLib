package top.hendrixshen.magiclib.mixin.minecraft.compat;

import com.mojang.math.Matrix4f;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import top.hendrixshen.magiclib.api.fake.compat.FontAccessor;

@Environment(EnvType.CLIENT)
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
    );

    //#if MC > 12101
    //$$ @Shadow
    //$$ public abstract String bidirectionalShaping(String string);
    //#endif

    @Override
    public int magiclib$drawInternal(
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
    ) {
        return this.drawInternal(
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
        );
    }
}
