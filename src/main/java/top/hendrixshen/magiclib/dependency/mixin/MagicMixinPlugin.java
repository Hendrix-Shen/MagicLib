package top.hendrixshen.magiclib.dependency.mixin;

import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.MagicLibReference;
import top.hendrixshen.magiclib.dependency.Dependencies;
import top.hendrixshen.magiclib.util.FabricUtil;

import java.util.Optional;

public class MagicMixinPlugin extends EmptyMixinPlugin {
    private DepCheckFailureCallback depCheckFailureCallback =
            (targetClassName, mixinClassName, reason) -> MagicLib.getLogger().warn("{}: Mixin {} can't apply to {} because: {}",
                    Optional.ofNullable(reason.getCause()).orElse(reason).getClass().getSimpleName(),
                    mixinClassName, targetClassName, reason.getMessage());


    public void setDepCheckFailureCallback(DepCheckFailureCallback depCheckFailureCallback) {
        this.depCheckFailureCallback = depCheckFailureCallback;
    }

    @Override
    public void onLoad(String mixinPackage) {
        FabricUtil.compatVersionCheck(MagicLibReference.getModId());
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return Dependencies.checkDependency(targetClassName, mixinClassName, this.depCheckFailureCallback);
    }

}
