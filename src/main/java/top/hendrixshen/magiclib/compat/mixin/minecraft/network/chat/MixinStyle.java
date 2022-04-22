package top.hendrixshen.magiclib.compat.mixin.minecraft.network.chat;

import net.minecraft.network.chat.Style;
import org.spongepowered.asm.mixin.Mixin;
import top.hendrixshen.magiclib.compat.minecraft.network.chat.StyleCompatApi;

//#if MC <= 11605
//$$ import org.jetbrains.annotations.Nullable;
//$$ import org.spongepowered.asm.mixin.Final;
//$$ import org.spongepowered.asm.mixin.Mutable;
//$$ import org.spongepowered.asm.mixin.Shadow;
//#endif

//#if MC <= 11502
//$$ import net.minecraft.ChatFormatting;
//$$ import net.minecraft.network.chat.ClickEvent;
//$$ import net.minecraft.network.chat.HoverEvent;
//$$ import org.spongepowered.asm.mixin.injection.At;
//$$ import org.spongepowered.asm.mixin.injection.Inject;
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//#endif

@Mixin(Style.class)
public abstract class MixinStyle implements StyleCompatApi {

    //#if MC <= 11605
    //$$ @Mutable
    //$$ @Final
    //$$ @Shadow
    //$$ private Boolean strikethrough;

    //$$ @Mutable
    //$$ @Final
    //$$ @Shadow
    //$$ private Boolean obfuscated;


    //$$ @SuppressWarnings("ConstantConditions")
    //$$ @Override
    //$$ public Style withStrikethrough(@Nullable Boolean strikethrough) {
    //#if MC > 11502
    //$$     Style thisStyle = (Style) (Object) this;
    //$$     Boolean oldStrikethrough = this.strikethrough;
    //$$     this.strikethrough = strikethrough;
    //$$     Style ret = thisStyle.applyTo(thisStyle);
    //$$     this.strikethrough = oldStrikethrough;
    //$$     return ret;
    //#else
    //$$     return this.copy().setStrikethrough(strikethrough);
    //#endif
    //$$ }

    //$$ @SuppressWarnings("ConstantConditions")
    //$$ @Override
    //$$ public Style withObfuscated(@Nullable Boolean obfuscated) {
    //#if MC > 11502
    //$$     Style thisStyle = (Style) (Object) this;
    //$$     Boolean OldObfuscated = this.obfuscated;
    //$$     this.obfuscated = OldObfuscated;
    //$$     Style ret = thisStyle.applyTo(thisStyle);
    //$$     this.obfuscated = OldObfuscated;
    //$$     return ret;
    //#else
    //$$     return this.copy().setObfuscated(obfuscated);
    //#endif
    //$$ }
    //#endif


    //#if MC <= 11502

    //$$ @Shadow
    //$$ public abstract Style copy();

    //$$ @Override
    //$$ public Style withUnderlined(@Nullable Boolean underlined) {
    //$$     return this.copy().setUnderlined(underlined);
    //$$ }
    //#endif

}
