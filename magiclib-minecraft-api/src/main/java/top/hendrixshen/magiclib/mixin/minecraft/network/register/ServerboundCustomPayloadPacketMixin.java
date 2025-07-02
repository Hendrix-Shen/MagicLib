package top.hendrixshen.magiclib.mixin.minecraft.network.register;

import org.spongepowered.asm.mixin.Mixin;

import top.hendrixshen.magiclib.api.preprocess.DummyClass;

// CHECKSTYLE.OFF: JavadocStyle
/**
 * <li>mc1.14 ~ mc1.20.1  : subproject 1.16.5 (main project) [dummy]        &lt;--------</li>
 * <li>mc1.20.2 ~ mc1.20.5: subproject 1.20.2</li>
 * <li>mc1.20.6+          : subproject 1.20.6</li>
 */
// CHECKSTYLE.ON: JavadocStyle
@Mixin(DummyClass.class)
public abstract class ServerboundCustomPayloadPacketMixin {
}
