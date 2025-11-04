package top.hendrixshen.magiclib.mixin.minecraft.accessor;

import org.spongepowered.asm.mixin.Mixin;

import top.hendrixshen.magiclib.api.preprocess.DummyClass;

// CHECKSTYLE.OFF: JavadocStyle
/**
 * <li>mc1.14 ~ mc1.20: subproject 1.16.5 (main project) [dummy]</li>
 * <li>mc1.20.1+      : subproject 1.20.1        &lt;--------</li>
 */
// CHECKSTYLE.ON: JavadocStyle
@Mixin(DummyClass.class)
public interface GuiGraphicsAccessor {
}
