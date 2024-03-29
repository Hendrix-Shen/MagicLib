package top.hendrixshen.magiclib.mixin.malilib.accessor;

import fi.dy.masa.malilib.hotkeys.IHotkeyCallback;
import fi.dy.masa.malilib.hotkeys.KeybindMulti;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = KeybindMulti.class, remap = false)
public interface KeybindMultiAccessor {
    @Accessor("callback")
    IHotkeyCallback magiclib$getCallback();
}
