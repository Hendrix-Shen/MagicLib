package top.hendrixshen.magiclib.mixin.minecraft.accessor;

import com.mojang.math.Quaternion;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Environment(EnvType.CLIENT)
@Mixin(Quaternion.class)
public interface QuaternionAccessor {
    @Accessor("values")
    float[] magiclib$getValues();
}
