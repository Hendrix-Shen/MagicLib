package top.hendrixshen.magiclib.api.mixin.extension.jikuTsuiho.predicate;

import top.hendrixshen.magiclib.api.mixin.extension.jikuTsuiho.info.MixinClassInfo;
import top.hendrixshen.magiclib.util.collect.SimplePredicate;

@FunctionalInterface
public interface MixinEraserClass extends SimplePredicate<MixinClassInfo> {
    @Override
    default boolean test(MixinClassInfo mixinClassInfo) {
        return this.shouldErase(mixinClassInfo);
    }

    boolean shouldErase(MixinClassInfo mixinClassInfo);
}
