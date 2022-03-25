package top.hendrixshen.magiclib.dependency.mixin;

import org.spongepowered.asm.mixin.MixinEnvironment;
import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.dependency.Dependencies;

import java.util.Optional;

public class MagicMixinPlugin extends EmptyMixinPlugin {
    private DepCheckFailureCallback depCheckFailureCallback =
            (targetClassName, mixinClassName, reason) -> {
                if (MixinEnvironment.getCurrentEnvironment().getOption(MixinEnvironment.Option.DEBUG_EXPORT)) {
                    MagicLib.getLogger().warn("{}: Mixin {} can't apply to {} because: {}",
                            Optional.ofNullable(reason.getCause()).orElse(reason).getClass().getSimpleName(),
                            mixinClassName, targetClassName, reason.getMessage());
                }
            };


    public void setDepCheckFailureCallback(DepCheckFailureCallback depCheckFailureCallback) {
        this.depCheckFailureCallback = depCheckFailureCallback;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return Dependencies.checkDependency(targetClassName, mixinClassName, this.depCheckFailureCallback);
    }

}
