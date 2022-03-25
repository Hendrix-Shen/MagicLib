package top.hendrixshen.magiclib.dependency.mixin;

import org.spongepowered.asm.mixin.MixinEnvironment;
import top.hendrixshen.magiclib.MagicLibReference;
import top.hendrixshen.magiclib.dependency.Dependencies;
import top.hendrixshen.magiclib.util.FabricUtil;

import java.util.Optional;

public class MagicMixinPlugin extends EmptyMixinPlugin {
    private static boolean compatVersionChecked = false;

    private DepCheckFailureCallback depCheckFailureCallback =
            (targetClassName, mixinClassName, reason) -> {
                if (MixinEnvironment.getCurrentEnvironment().getOption(MixinEnvironment.Option.DEBUG_EXPORT)) {
                    MagicLibReference.LOGGER.warn("{}: \nMixin {} can't apply to {} because: \n{}",
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

    @Override
    public void onLoad(String mixinPackage) {
        if (!compatVersionChecked) {
            FabricUtil.compatVersionCheck();
            compatVersionChecked = true;
        }
    }

}
