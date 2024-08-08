package top.hendrixshen.magiclib.mixin.minecraft.accessor;

import net.minecraft.world.item.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DyeColor.class)
public interface DyeColorAccessor {
    @Accessor("textColor")
    int magiclib$getTextColor();
}
