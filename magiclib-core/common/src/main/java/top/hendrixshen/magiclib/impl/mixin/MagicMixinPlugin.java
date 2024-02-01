package top.hendrixshen.magiclib.impl.mixin;

import org.spongepowered.asm.mixin.MixinEnvironment;
import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.api.dependency.DependencyCheckException;
import top.hendrixshen.magiclib.api.mixin.EmptyMixinPlugin;
import top.hendrixshen.magiclib.api.mixin.checker.MixinDependencyChecker;
import top.hendrixshen.magiclib.api.mixin.checker.MixinDependencyCheckers;

import java.util.Optional;

public class MagicMixinPlugin extends EmptyMixinPlugin {
    private static final MixinDependencyChecker checker = MixinDependencyCheckers.memorized();

    public MagicMixinPlugin() {
        this.getChecker().setCheckFailureCallback(this::onCheckFailed);
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return this.getChecker().check(targetClassName, mixinClassName);
    }

    public MixinDependencyChecker getChecker() {
        return MagicMixinPlugin.checker;
    }

    private void onCheckFailed(String targetClassName, String mixinClassName, DependencyCheckException reason) {
        if (MixinEnvironment.getCurrentEnvironment().getOption(MixinEnvironment.Option.DEBUG_EXPORT)) {
            MagicLib.getLogger().warn("{}: \nMixin {} can't apply to {} because: \n{}",
                    Optional.ofNullable(reason.getCause()).orElse(reason).getClass().getSimpleName(),
                    mixinClassName, targetClassName, reason.getMessage());
        }
    }
}
