package top.hendrixshen.magiclib.dependency.impl;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import top.hendrixshen.magiclib.carpet.impl.RuleOption;
import top.hendrixshen.magiclib.dependency.api.RuleDependencyPredicate;
import top.hendrixshen.magiclib.impl.carpet.MagicLibSettings;
import top.hendrixshen.magiclib.util.FabricUtil;

/**
 * MagicLib built-in ConfigDependencyPredicates.
 * <p>
 * Some basic uses of predicates are given here.
 */
@Environment(EnvType.CLIENT)
public class RuleDependencyPredicates {
    /**
     * Predicate that implements {@link RuleDependencyPredicate} for config predicate checking.
     * <p>
     * Default value for config predicate check, this predicate always returns true.
     */
    public static class TrueRulePredicate implements RuleDependencyPredicate {
        @Override
        public boolean isSatisfied(RuleOption option) {
            return true;
        }
    }

    /**
     * Predicate that implements {@link RuleDependencyPredicate} for config predicate checking.
     * <p>
     * This predicate returns true only when MagicLib debug mode is enabled.
     */
    public static class DebugRulePredicate implements RuleDependencyPredicate {
        @Override
        public boolean isSatisfied(RuleOption option) {
            return MagicLibSettings.debug;
        }
    }

    /**
     * Predicate that implements {@link RuleDependencyPredicate} for config predicate checking.
     * <p>
     * This predicate returns true only when Fabric is enabled in the development environment.
     */
    public static class DevRulePredicate implements RuleDependencyPredicate {
        @Override
        public boolean isSatisfied(RuleOption option) {
            return FabricUtil.isDevelopmentEnvironment();
        }
    }

    /**
     * Predicate that implements {@link RuleDependencyPredicate} for config predicate checking.
     * <p>
     * This predicate returns true only when Fabric is enabled in the development environment and use mojang's mapping.
     */
    public static class DevMojangRulePredicate implements RuleDependencyPredicate {
        @Override
        public boolean isSatisfied(RuleOption option) {
            return FabricUtil.isDevelopmentEnvironment() &&
                    FabricLoader.getInstance().getMappingResolver()
                            .mapClassName("intermediary", "net.minecraft.class_310").equals("net.minecraft.client.Minecraft");
        }
    }
}
