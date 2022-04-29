package top.hendrixshen.magiclib.compat.mixin.minecraft.network.chat;

import net.minecraft.network.chat.Style;
import org.spongepowered.asm.mixin.Mixin;
import top.hendrixshen.magiclib.compat.minecraft.network.chat.ComponentCompatApi;

//#if MC < 11900
import net.minecraft.network.chat.BaseComponent;
//#endif

//#if MC > 11502
import net.minecraft.network.chat.MutableComponent;
//#else
//$$ import net.minecraft.network.chat.Component;
//$$ import org.spongepowered.asm.mixin.Shadow;
//#endif

//#if MC > 11802
//$$ @Mixin(MutableComponent.class)
//#else
@Mixin(BaseComponent.class)
//#endif
public abstract class MixinBaseComponent implements ComponentCompatApi {


    //#if MC <= 11502
    //$$ @Shadow
    //$$ public abstract Component setStyle(Style style);
    //#endif

    @Override
    //#if MC > 11502
    public MutableComponent
    //#else
    //$$ public BaseComponent
    //#endif
    withStyleCompat(Style style) {
        //#if MC > 11502
        return ((MutableComponent) (Object) this).withStyle(style);
        //#else
        //$$ return (BaseComponent) setStyle(style);
        //#endif
    }


}
