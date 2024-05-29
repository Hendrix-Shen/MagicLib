package top.hendrixshen.magiclib.impl.mixin;

import org.objectweb.asm.tree.ClassNode;
import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.api.mixin.MixinPredicate;

/**
 * MagicLib built-in MixinDependencyPredicates.
 * <p>
 * Some basic uses of predicates are given here.
 */
public class BuiltInPredicates {
    /**
     * Predicate that implements {@link MixinPredicate} for mixin predicate checking.
     * <p>
     * This predicate always returns true.
     */
    public static class TruePredicate implements MixinPredicate {
        @Override
        public boolean test(ClassNode classNode) {
            return true;
        }
    }

    /**
     * Predicate that implements {@link MixinPredicate} for mixin predicate checking.
     * <p>
     * This predicate returns true only if Platform is running at the development environment.
     */
    public static class DevMixinPredicate implements MixinPredicate {
        @Override
        public boolean test(ClassNode classNode) {
            return MagicLib.getInstance().getCurrentPlatform().isDevelopmentEnvironment();
        }
    }

    /**
     * Predicate that implements {@link DevMixinPredicate} for mixin predicate checking.
     * <p>
     * This predicate returns true only if minecraft de-obfuscated by mojang mapping.
     */
    public static class MojangMappingMixinPredicate implements MixinPredicate {
        @Override
        public boolean test(ClassNode classNode) {
            return "mojang".equals(MagicLib.getInstance().getCurrentPlatform().getNamedMappingName());
        }
    }
}
