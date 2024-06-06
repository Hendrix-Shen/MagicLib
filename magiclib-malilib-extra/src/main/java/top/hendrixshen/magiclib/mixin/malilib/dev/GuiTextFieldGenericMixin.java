package top.hendrixshen.magiclib.mixin.malilib.dev;

import fi.dy.masa.malilib.gui.GuiTextFieldGeneric;
import net.minecraft.client.gui.components.EditBox;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.hendrixshen.magiclib.api.dependency.DependencyType;
import top.hendrixshen.magiclib.api.dependency.annotation.CompositeDependencies;
import top.hendrixshen.magiclib.api.dependency.annotation.Dependencies;
import top.hendrixshen.magiclib.api.dependency.annotation.Dependency;
import top.hendrixshen.magiclib.impl.mixin.BuiltInPredicates;

@CompositeDependencies({
        @Dependencies(
                require = {
                        @Dependency(dependencyType = DependencyType.MOD_ID, value = "malilib"),
                        @Dependency(dependencyType = DependencyType.PREDICATE, predicate = BuiltInPredicates.DevMixinPredicate.class),
                        @Dependency(dependencyType = DependencyType.PREDICATE, predicate = BuiltInPredicates.MojangMappingMixinPredicate.class),

                }
        ),
        @Dependencies(
                require = {
                        @Dependency(dependencyType = DependencyType.MOD_ID, value = "mafglib"),
                        @Dependency(dependencyType = DependencyType.PREDICATE, predicate = BuiltInPredicates.DevMixinPredicate.class),
                        @Dependency(dependencyType = DependencyType.PREDICATE, predicate = BuiltInPredicates.MojangMappingMixinPredicate.class),

                }
        )
})
@Mixin(value = GuiTextFieldGeneric.class, remap = false)
public class GuiTextFieldGenericMixin extends EditBox {
    @Unique
    private boolean magiclib$setCursorPositionCalled = false;

    @SuppressWarnings("ConstantConditions")
    public GuiTextFieldGenericMixin() {
        super(null, 0, 0, 0, 0, null);
    }

    @Inject(
            method = "setCursorPosition",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void preSetCursorPosition(int pos, CallbackInfo ci) {
        if (this.magiclib$setCursorPositionCalled) {
            super.setCursorPosition(pos);
            ci.cancel();
        }

        this.magiclib$setCursorPositionCalled = true;
    }

    @Inject(
            method = "setCursorPosition",
            at = @At(
                    value = "RETURN"
            )
    )
    private void postSetCursorPosition(int pos, CallbackInfo ci) {
        this.magiclib$setCursorPositionCalled = false;
    }

    @Inject(
            method = "getCursorPosition",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void preGetCursorPosition(@NotNull CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(super.getCursorPosition());
    }
}
