package top.hendrixshen.magiclib.dependency.impl;

import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import top.hendrixshen.magiclib.dependency.api.MixinDependencyPredicate;
import top.hendrixshen.magiclib.util.FabricUtil;

/**
 * MagicLib built-in MixinDependencyPredicates.
 * <p>
 * Some basic uses of predicates are given here.
 */
public class MixinDependencyPredicates {
    /**
     * Predicate that implements {@link MixinDependencyPredicate} for config predicate checking.
     * <p>
     * Default value for config predicate check, this predicate always returns true.
     */
    public static class TrueConfigPredicate implements MixinDependencyPredicate {
        @Override
        public boolean isSatisfied(ClassNode classNode) {
            return true;
        }
    }

    /**
     * Predicate that implements {@link MixinDependencyPredicate} for config predicate checking.
     * <p>
     * This predicate returns true only when Fabric is enabled in the development environment.
     */
    public static class DevMixinPredicate implements MixinDependencyPredicate {
        @Override
        public boolean isSatisfied(ClassNode classNode) {
            return FabricUtil.isDevelopmentEnvironment();
        }
    }

    /**
     * Predicate that implements {@link MixinDependencyPredicate} for config predicate checking.
     * <p>
     * This predicate returns true only when Fabric is enabled in the development environment and use mojang's mapping.
     */
    public static class DevMojangMixinPredicate implements MixinDependencyPredicate {
        @Override
        public boolean isSatisfied(ClassNode mixinClass) {
            return FabricUtil.isDevelopmentEnvironment() &&
                    FabricLoader.getInstance().getMappingResolver()
                            .mapClassName("intermediary", "net.minecraft.class_310").equals("net.minecraft.client.Minecraft");
        }
    }
}
