package top.hendrixshen.magiclib.compat.mixin.minecraft.network.chat;

import net.minecraft.network.chat.BaseComponent;
import org.spongepowered.asm.mixin.Mixin;
import top.hendrixshen.magiclib.compat.minecraft.network.chat.ComponentCompatApi;

//#if MC <= 11502
//$$ import org.spongepowered.asm.mixin.Shadow;
//$$ import net.minecraft.network.chat.Style;
//$$ import net.minecraft.network.chat.Component;
//#endif

@Mixin(BaseComponent.class)
public abstract class MixinBaseComponent implements ComponentCompatApi {


    //#if MC <= 11502
    //$$ @Shadow
    //$$ public abstract Component setStyle(Style style);
    //$$ @Override
    //$$ public Component withStyle(Style style) {
    //$$     return setStyle(style);
    //$$ }
    //#endif


}
