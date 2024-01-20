package top.hendrixshen.magiclib.api.mixin;

import java.util.List;

@FunctionalInterface
public interface IMixinEraser {
    boolean shouldErase(List<String> targetClassNames, String mixinClassName);
}
