package top.hendrixshen.magiclib.compat.mixin.minecraft.network.chat;

import net.minecraft.network.chat.Style;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import top.hendrixshen.magiclib.compat.annotation.Remap;

import javax.annotation.Nullable;

@Mixin(Style.class)
public abstract class MixinStyle {

    @Mutable
    @Final
    @Shadow
    private Boolean strikethrough;

    @Mutable
    @Final
    @Shadow
    private Boolean obfuscated;


    @SuppressWarnings("ConstantConditions")
    @Remap("method_36140")
    public Style withStrikethrough(@Nullable Boolean strikethrough) {
        Style thisStyle = (Style) (Object) this;
        Boolean oldStrikethrough = this.strikethrough;
        this.strikethrough = strikethrough;
        Style ret = thisStyle.applyTo(thisStyle);
        this.strikethrough = oldStrikethrough;
        return ret;
    }

    @SuppressWarnings("ConstantConditions")
    @Remap("method_36141")
    public Style withObfuscated(@Nullable Boolean obfuscated) {
        Style thisStyle = (Style) (Object) this;
        Boolean OldObfuscated = this.obfuscated;
        this.obfuscated = OldObfuscated;
        Style ret = thisStyle.applyTo(thisStyle);
        this.obfuscated = OldObfuscated;
        return ret;
    }
}
