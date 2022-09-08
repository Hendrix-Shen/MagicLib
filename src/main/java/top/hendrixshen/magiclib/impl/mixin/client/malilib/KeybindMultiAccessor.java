package top.hendrixshen.magiclib.impl.mixin.client.malilib;

import fi.dy.masa.malilib.hotkeys.IHotkeyCallback;
import fi.dy.masa.malilib.hotkeys.KeybindMulti;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(KeybindMulti.class)
public interface KeybindMultiAccessor {
    @Accessor(remap = false)
    IHotkeyCallback getCallback();
}
