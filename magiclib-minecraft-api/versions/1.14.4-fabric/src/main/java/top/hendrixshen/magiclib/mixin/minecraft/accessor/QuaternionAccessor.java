package top.hendrixshen.magiclib.mixin.minecraft.accessor;

import com.mojang.math.Quaternion;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

// CHECKSTYLE.OFF: JavadocStyle
/**
 * <li>mc1.14 ~ mc1.15: subproject 1.14.4        &lt;--------</li>
 * <li>mc1.16+        : subproject 1.16.5 (main project) [dummy]</li>
 */
// CHECKSTYLE.ON: JavadocStyle
@Mixin(Quaternion.class)
public interface QuaternionAccessor {
    @Accessor("values")
    float[] magiclib$getValues();
}
