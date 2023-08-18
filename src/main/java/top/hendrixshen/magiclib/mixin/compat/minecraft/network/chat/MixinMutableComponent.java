package top.hendrixshen.magiclib.mixin.compat.minecraft.network.chat;

import net.minecraft.network.chat.Style;
import org.spongepowered.asm.mixin.Mixin;
import top.hendrixshen.magiclib.compat.minecraft.api.network.chat.ComponentCompatApi;
import top.hendrixshen.magiclib.util.MiscUtil;

//#if MC < 11900
//$$ import net.minecraft.network.chat.BaseComponent;
//#endif

//#if MC > 11502
import net.minecraft.network.chat.MutableComponent;
//#else
//$$ import net.minecraft.network.chat.Component;
//$$ import org.spongepowered.asm.mixin.Shadow;
//#endif

@Mixin(
        //#if MC > 11802
        MutableComponent.class
        //#else
        //$$ BaseComponent.class
        //#endif
)
public abstract class MixinMutableComponent implements ComponentCompatApi {
    //#if MC < 11600
    //$$ @Shadow
    //$$ public abstract Component setStyle(Style style);
    //#endif

    @Override
    //#if MC > 11802
    public MutableComponent withStyleCompat(Style style) {
    //#else
    //$$ public BaseComponent withStyleCompat(Style style) {
    //#endif
        //#if MC > 11802
        return ((MutableComponent) MiscUtil.cast(this)).withStyle(style);
        //#elseif MC > 11502
        //$$ return (BaseComponent) ((MutableComponent) MiscUtil.cast(this)).withStyle(style);
        //#else
        //$$ return (BaseComponent) setStyle(style);
        //#endif
    }
}
