package top.hendrixshen.magiclib.mixin.client;

import fi.dy.masa.malilib.gui.GuiTextFieldGeneric;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.hendrixshen.magiclib.api.dependencyValidator.annotation.Dependencies;
import top.hendrixshen.magiclib.api.dependencyValidator.annotation.Dependency;
import top.hendrixshen.magiclib.util.Predicates;

// Fix mojang mappings stackoverflow
@Dependencies(require = @Dependency(value = "malilib", predicate = Predicates.DevMixinPredicate.class))
@Mixin(value = GuiTextFieldGeneric.class, remap = false)
public abstract class MixinGuiTextFieldGeneric extends EditBox {
    private boolean setCursorPositionCalled = false;

    public MixinGuiTextFieldGeneric(Font font, int i, int j, int k, int l, Component component) {
        super(font, i, j, k, l, component);
    }

    @Inject(method = "setCursorPosition", at = @At(value = "HEAD"), cancellable = true)
    private void preSetCursorPosition(int pos, CallbackInfo ci) {
        if (setCursorPositionCalled) {
            super.setCursorPosition(pos);
            ci.cancel();
        }
        setCursorPositionCalled = true;
    }

    @Inject(method = "setCursorPosition", at = @At(value = "RETURN"))
    private void postSetCursorPosition(int pos, CallbackInfo ci) {
        setCursorPositionCalled = false;
    }
}
