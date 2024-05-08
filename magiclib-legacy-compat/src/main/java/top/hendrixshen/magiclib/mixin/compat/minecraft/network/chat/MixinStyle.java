package top.hendrixshen.magiclib.mixin.compat.minecraft.network.chat;

import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import top.hendrixshen.magiclib.compat.minecraft.api.network.chat.StyleCompatApi;

//#if MC < 11700
//#if MC > 11502
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
//#endif
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mutable;
//#endif

@Mixin(Style.class)
public abstract class MixinStyle implements StyleCompatApi {
    //#if MC > 11605
    //$$ @Shadow
    //$$ public abstract Style withStrikethrough(@Nullable Boolean strikethrough);
    //$$
    //$$ @Shadow
    //$$ public abstract Style withObfuscated(@Nullable Boolean obfuscated);
    //#elseif MC > 11502
    @Mutable
    @Final
    @Shadow
    private Boolean strikethrough;

    @Mutable
    @Final
    @Shadow
    private Boolean obfuscated;
    //#else
    //$$ @Shadow
    //$$ public abstract Style copy();
    //#endif

    @Shadow
    //#if MC > 11502
    //#if MC > 11502 && MC < 11700
    @Environment(EnvType.CLIENT)
    //#endif
    public abstract Style withUnderlined(@Nullable Boolean underlined);
    //#else
    //$$ public abstract Style setUnderlined(Boolean underlined);
    //#endif

    //#if MC > 11502 && MC < 11700
    @Shadow
    @Final
    private @Nullable TextColor color;

    @Shadow
    @Final
    private @Nullable Boolean bold;

    @Shadow
    @Final
    private @Nullable Boolean italic;

    @Shadow
    @Final
    private @Nullable ClickEvent clickEvent;

    @Shadow
    @Final
    private @Nullable String insertion;

    @Shadow
    @Final
    private @Nullable ResourceLocation font;

    @Shadow
    @Final
    private @Nullable HoverEvent hoverEvent;
    //#endif

    //#if MC > 11502 && MC < 11700
    @SuppressWarnings("ConstantConditions")
    //#endif
    @Override
    public Style withStrikethroughCompat(@Nullable Boolean strikethrough) {
        //#if MC > 11605
        //$$ return this.withStrikethrough(strikethrough);
        //#elseif MC > 11502
            Style thisStyle = (Style) (Object) this;
            Boolean oldStrikethrough = this.strikethrough;
            this.strikethrough = strikethrough;
            Style ret = thisStyle.applyTo(thisStyle);
            this.strikethrough = oldStrikethrough;
            return ret;
        //#else
        //$$     return this.copy().setStrikethrough(strikethrough);
        //#endif
    }

    //#if MC > 11502 && MC < 11700
    @SuppressWarnings("ConstantConditions")
    //#endif
    @Override
    public Style withObfuscatedCompat(@Nullable Boolean obfuscated) {
        //#if MC > 11605
        //$$ return this.withObfuscated(obfuscated);
        //#elseif MC > 11502
        Style thisStyle = (Style) (Object) this;
        Boolean OldObfuscated = this.obfuscated;
        this.obfuscated = OldObfuscated;
        Style ret = thisStyle.applyTo(thisStyle);
        this.obfuscated = OldObfuscated;
        return ret;
        //#else
        //$$ return this.copy().setObfuscated(obfuscated);
        //#endif
    }

    @Override
    public Style withUnderlinedCompat(@Nullable Boolean underlined) {
        //#if MC > 11605
        //$$ return this.withUnderlined(underlined);
        //#elseif MC > 11502
        return AccessorStyle.invokeConstructor(this.color, this.bold, this.italic, underlined, this.strikethrough,
                this.obfuscated, this.clickEvent, this.hoverEvent, this.insertion, this.font);
        //#else
        //$$ return this.setUnderlined(underlined);
        //#endif
    }
}
