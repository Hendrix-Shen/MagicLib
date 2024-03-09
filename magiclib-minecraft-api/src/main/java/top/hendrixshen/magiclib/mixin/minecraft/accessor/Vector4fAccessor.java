package top.hendrixshen.magiclib.mixin.minecraft.accessor;

import com.mojang.math.Vector4f;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Environment(EnvType.CLIENT)
@Mixin(Vector4f.class)
public interface Vector4fAccessor {
    @Accessor("values")
    float[] magiclib$getValues();
}
