package top.hendrixshen.magiclib.compat.mixin.minecraft.network.chat;

import net.minecraft.network.chat.BaseComponent;
import net.minecraft.network.chat.Style;
import org.spongepowered.asm.mixin.Mixin;
import top.hendrixshen.magiclib.compat.minecraft.network.chat.ComponentCompatApi;

//#if MC <= 11502
//$$ import net.minecraft.network.chat.Component;
//$$ import org.spongepowered.asm.mixin.Shadow;
//#endif

@Mixin(BaseComponent.class)
public abstract class MixinBaseComponent implements ComponentCompatApi {


    //#if MC <= 11502
    //$$ @Shadow
    //$$ public abstract Component setStyle(Style style);
    //#endif

    @SuppressWarnings("ConstantConditions")
    @Override
    public BaseComponent withStyleCompat(Style style) {
        //#if MC > 11502
        return (BaseComponent) ((BaseComponent) (Object) this).withStyle(style);
        //#else
        //$$ return (BaseComponent) setStyle(style);
        //#endif
    }


}
