package top.hendrixshen.magiclib.mixin.malilib.accessor;

import fi.dy.masa.malilib.hotkeys.IHotkeyCallback;
import fi.dy.masa.malilib.hotkeys.KeybindMulti;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import top.hendrixshen.magiclib.api.dependency.DependencyType;
import top.hendrixshen.magiclib.api.dependency.annotation.CompositeDependencies;
import top.hendrixshen.magiclib.api.dependency.annotation.Dependencies;
import top.hendrixshen.magiclib.api.dependency.annotation.Dependency;

@Environment(EnvType.CLIENT)
@CompositeDependencies(@Dependencies(require = @Dependency(dependencyType = DependencyType.MOD_ID, value = "malilib")))
@Mixin(value = KeybindMulti.class, remap = false)
public interface KeybindMultiAccessor {
    @Accessor()
    IHotkeyCallback getCallback();
}
