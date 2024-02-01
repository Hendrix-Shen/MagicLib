package top.hendrixshen.magiclib.api.mixin.extension.jikuTsuiho.predicate;

import top.hendrixshen.magiclib.api.mixin.extension.jikuTsuiho.info.MixinMethodInfo;
import top.hendrixshen.magiclib.util.collect.SimplePredicate;

@FunctionalInterface
public interface MixinEraserMethod extends SimplePredicate<MixinMethodInfo> {
    @Override
    default boolean test(MixinMethodInfo mixinMethodInfo) {
        return this.shouldErase(mixinMethodInfo);
    }

    boolean shouldErase(MixinMethodInfo mixinMethodInfo);
}
