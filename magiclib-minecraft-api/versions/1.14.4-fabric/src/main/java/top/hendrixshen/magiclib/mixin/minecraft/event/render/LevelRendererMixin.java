package top.hendrixshen.magiclib.mixin.minecraft.event.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import org.spongepowered.asm.mixin.Mixin;

import top.hendrixshen.magiclib.api.preprocess.DummyClass;

// CHECKSTYLE.OFF: JavadocStyle
/**
 * <li>mc1.14 : subproject 1.14.4 [dummy]        &lt;--------</li>
 * <li>mc1.15+: subproject 1.16.5 (main project)</li>
 */
// CHECKSTYLE.ON: JavadocStyle
@Environment(EnvType.CLIENT)
@Mixin(DummyClass.class)
public class LevelRendererMixin {
}
