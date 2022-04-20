package top.hendrixshen.magiclib.impl.mixin.client.malilib;

import fi.dy.masa.malilib.gui.GuiTextFieldGeneric;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.hendrixshen.magiclib.dependency.Predicates;
import top.hendrixshen.magiclib.dependency.annotation.Dependencies;
import top.hendrixshen.magiclib.dependency.annotation.Dependency;

// Fix mojang mappings stackoverflow
@Dependencies(and = @Dependency(value = "malilib"), predicate = Predicates.DevMojangMixinPredicate.class)
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

    @Inject(method = "getCursorPosition", at = @At(value = "HEAD"), cancellable = true)
    private void preGetCursorPosition(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(super.getCursorPosition());
    }
}
