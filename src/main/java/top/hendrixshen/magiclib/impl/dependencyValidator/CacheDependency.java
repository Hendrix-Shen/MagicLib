package top.hendrixshen.magiclib.impl.dependencyValidator;

import com.google.common.collect.Maps;

import java.util.Map;

public class CacheDependency extends AbstractDependencyValidator {
    private final Map<String, Boolean> cache = Maps.newHashMap();

    public boolean checkDependency(String targetClassName, String mixinClassName) {
        return this.cache.computeIfAbsent(mixinClassName, key -> super.checkDependency(targetClassName, mixinClassName));
    }
}
