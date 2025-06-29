package top.hendrixshen.magiclib.mixin.minecraft.event.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import org.spongepowered.asm.mixin.Mixin;

import top.hendrixshen.magiclib.api.preprocess.DummyClass;

// Used in mc1.15+
@Environment(EnvType.CLIENT)
@Mixin(DummyClass.class)
public class LevelRendererMixin {
}
