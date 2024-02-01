package top.hendrixshen.magiclib.api.mixin.extension.jikuTsuiho.predicate;

import top.hendrixshen.magiclib.api.mixin.extension.jikuTsuiho.info.MixinFieldInfo;
import top.hendrixshen.magiclib.util.collect.SimplePredicate;

@FunctionalInterface
public interface MixinEraserField extends SimplePredicate<MixinFieldInfo> {
    @Override
    default boolean test(MixinFieldInfo mixinFieldInfo) {
        return this.shouldErase(mixinFieldInfo);
    }

    boolean shouldErase(MixinFieldInfo mixinFieldInfo);
}
