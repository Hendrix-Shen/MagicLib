package top.hendrixshen.magiclib.api.dependencyValidator.mixin;

import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.impl.dependencyValidator.CacheDependency;

public class MagicMixinPlugin extends EmptyMixinPlugin {
    protected final CacheDependency cache = new CacheDependency();

    public MagicMixinPlugin() {
        this.cache.setFailureCallback(this::onDependencyFailure);
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return this.cache.checkDependency(targetClassName, mixinClassName);
    }

    private void onDependencyFailure(String mixinClassName, String reason) {
        MagicLib.getLogger().warn(String.format("Mixin %s not applied because: %s", mixinClassName, reason));
    }
}
