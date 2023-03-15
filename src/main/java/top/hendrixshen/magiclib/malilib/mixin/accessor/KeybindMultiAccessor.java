package top.hendrixshen.magiclib.malilib.mixin.accessor;

import fi.dy.masa.malilib.hotkeys.IHotkeyCallback;
import fi.dy.masa.malilib.hotkeys.KeybindMulti;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import top.hendrixshen.magiclib.dependency.api.annotation.Dependencies;
import top.hendrixshen.magiclib.dependency.api.annotation.Dependency;

@Environment(EnvType.CLIENT)
@Dependencies(and = @Dependency("malilib"))
@Mixin(value = KeybindMulti.class, remap = false)
public interface KeybindMultiAccessor {
    @Accessor()
    IHotkeyCallback getCallback();
}
