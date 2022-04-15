package top.hendrixshen.magiclib.compat.mixin.minecraft.client.gui;

import com.mojang.math.Matrix4f;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedTextCompat;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import top.hendrixshen.magiclib.compat.annotation.Remap;

@Mixin(Font.class)
public abstract class MixinFont {
    @Shadow
    public abstract int width(String string);

    @Shadow
    public abstract int drawInBatch(String string, float x, float y, int color, boolean shadow, Matrix4f matrix4f,
                                    MultiBufferSource multiBufferSource, boolean seeThrough, int backgroundColor, int light);

    @Remap("method_27525")
    public int width(FormattedTextCompat formattedText) {
        return Mth.ceil(this.width(((Component) formattedText).getString()));
    }

    @Remap("method_30882")
    public int drawInBatch(Component component, float x, float y, int color, boolean shadow, Matrix4f matrix4f,
                           MultiBufferSource multiBufferSource, boolean seeThrough, int backgroundColor, int light) {
        return this.drawInBatch(component.getColoredString(), x, y, color, shadow, matrix4f, multiBufferSource,
                seeThrough, backgroundColor, light);
    }

}
