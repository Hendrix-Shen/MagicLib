package top.hendrixshen.magiclib.compat.mixin.minecraft.network.chat;

import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import top.hendrixshen.magiclib.compat.minecraft.network.chat.StyleCompatApi;

//#if MC <= 11605
//$$ import org.spongepowered.asm.mixin.Final;
//$$ import org.spongepowered.asm.mixin.Mutable;
//#endif

@Mixin(Style.class)
public abstract class MixinStyle implements StyleCompatApi {
    //#if MC > 11605
    @Shadow
    public abstract Style withStrikethrough(@Nullable Boolean strikethrough);

    @Shadow
    public abstract Style withObfuscated(@Nullable Boolean obfuscated);

    //#elseif MC > 11502
    //$$ @Mutable
    //$$ @Final
    //$$ @Shadow
    //$$ private Boolean strikethrough;
    //$$
    //$$ @Mutable
    //$$ @Final
    //$$ @Shadow
    //$$ private Boolean obfuscated;
    //#else
    //$$ @Shadow
    //$$ public abstract Style copy();
    //#endif

    //#if MC > 11502
    @Shadow
    public abstract Style withUnderlined(@Nullable Boolean underlined);
    //#endif

    //#if MC >11502 && MC <= 11605
    //$$ @SuppressWarnings("ConstantConditions")
    //#endif
    @Override

    public Style withStrikethroughCompat(@Nullable Boolean strikethrough) {
        //#if MC > 11605
        return this.withStrikethrough(strikethrough);
        //#elseif MC > 11502
        //$$     Style thisStyle = (Style) (Object) this;
        //$$     Boolean oldStrikethrough = this.strikethrough;
        //$$     this.strikethrough = strikethrough;
        //$$     Style ret = thisStyle.applyTo(thisStyle);
        //$$     this.strikethrough = oldStrikethrough;
        //$$     return ret;
        //#else
        //$$     return this.copy().setStrikethrough(strikethrough);
        //#endif
    }

    //#if MC >11502 && MC <= 11605
    //$$ @SuppressWarnings("ConstantConditions")
    //#endif
    @Override
    public Style withObfuscatedCompat(@Nullable Boolean obfuscated) {
        //#if MC > 11605
        return this.withObfuscated(obfuscated);
        //#elseif MC > 11502
        //$$     Style thisStyle = (Style) (Object) this;
        //$$     Boolean OldObfuscated = this.obfuscated;
        //$$     this.obfuscated = OldObfuscated;
        //$$     Style ret = thisStyle.applyTo(thisStyle);
        //$$     this.obfuscated = OldObfuscated;
        //$$     return ret;
        //#else
        //$$     return this.copy().setObfuscated(obfuscated);
        //#endif
    }

    @Override
    public Style withUnderlinedCompat(@Nullable Boolean underlined) {
        //#if MC > 11502
        return this.withUnderlined(underlined);
        //#else
        //$$     return this.copy().setUnderlined(underlined);
        //#endif
    }
}
