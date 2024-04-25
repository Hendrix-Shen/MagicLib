package top.hendrixshen.magiclib.mixin.minecraft.accessor;

import com.mojang.math.Matrix4f;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Environment(EnvType.CLIENT)
@Mixin(Matrix4f.class)
public interface Matrix4fAccessor {
    @Accessor("values")
    float[] magiclib$getValues();
}
