package top.hendrixshen.magiclib.mixin.minecraft.accessor;

import com.mojang.blaze3d.vertex.Tesselator;
import org.spongepowered.asm.mixin.Mixin;

//#if MC >= 12100
//$$ import org.spongepowered.asm.mixin.gen.Accessor;
//$$ import com.mojang.blaze3d.vertex.ByteBufferBuilder;
//#endif

@Mixin(Tesselator.class)
public interface TesselatorAccessor {
    //#if MC >= 12100
    //$$ @Accessor("buffer")
    //$$ ByteBufferBuilder magiclib$getBuffer();
    //$$
    //#endif
}
