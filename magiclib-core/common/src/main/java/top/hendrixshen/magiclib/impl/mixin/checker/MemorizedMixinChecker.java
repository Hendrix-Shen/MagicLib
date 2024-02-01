package top.hendrixshen.magiclib.impl.mixin.checker;

import com.google.common.collect.Maps;
import top.hendrixshen.magiclib.api.mixin.checker.MixinDependencyCheckFailureCallback;
import top.hendrixshen.magiclib.api.mixin.checker.MixinDependencyChecker;

import java.util.Map;

public class MemorizedMixinChecker implements MixinDependencyChecker {
    private final MixinDependencyChecker checker;
    private final Map<String, Boolean> memory = Maps.newConcurrentMap();

    public MemorizedMixinChecker(MixinDependencyChecker checker) {
        this.checker = checker;
    }

    @Override
    public boolean check(String targetClassName, String mixinClassName) {
        Boolean result = this.memory.get(mixinClassName);

        if (result == null) {
            result = this.checker.check(targetClassName, mixinClassName);
            this.memory.put(mixinClassName, result);
        }

        return result;
    }

    @Override
    public void setCheckFailureCallback(MixinDependencyCheckFailureCallback callback) {
        this.checker.setCheckFailureCallback(callback);
    }
}
