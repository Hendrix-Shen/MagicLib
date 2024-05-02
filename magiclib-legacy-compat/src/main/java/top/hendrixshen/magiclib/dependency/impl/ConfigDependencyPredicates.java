package top.hendrixshen.magiclib.dependency.impl;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import top.hendrixshen.magiclib.dependency.api.ConfigDependencyPredicate;
import top.hendrixshen.magiclib.impl.config.MagicLibConfigs;
import top.hendrixshen.magiclib.malilib.impl.ConfigOption;
import top.hendrixshen.magiclib.util.FabricUtil;

/**
 * MagicLib built-in ConfigDependencyPredicates.
 * <p>
 * Some basic uses of predicates are given here.
 */
@Environment(EnvType.CLIENT)
public class ConfigDependencyPredicates {
    /**
     * Predicate that implements {@link ConfigDependencyPredicate} for config predicate checking.
     * <p>
     * Default value for config predicate check, this predicate always returns true.
     */
    public static class TrueConfigPredicate implements ConfigDependencyPredicate {
        @Override
        public boolean isSatisfied(ConfigOption option) {
            return true;
        }
    }

    /**
     * Predicate that implements {@link ConfigDependencyPredicate} for config predicate checking.
     * <p>
     * This predicate returns true only when MagicLib debug mode is enabled.
     */
    public static class DebugConfigPredicate implements ConfigDependencyPredicate {
        @Override
        public boolean isSatisfied(ConfigOption option) {
            return MagicLibConfigs.debug;
        }
    }

    /**
     * Predicate that implements {@link ConfigDependencyPredicate} for config predicate checking.
     * <p>
     * This predicate returns true only when Fabric is enabled in the development environment.
     */
    public static class DevConfigPredicate implements ConfigDependencyPredicate {
        @Override
        public boolean isSatisfied(ConfigOption option) {
            return FabricUtil.isDevelopmentEnvironment();
        }
    }

    /**
     * Predicate that implements {@link ConfigDependencyPredicate} for config predicate checking.
     * <p>
     * This predicate returns true only when Fabric is enabled in the development environment and use mojang's mapping.
     */
    public static class DevMojangConfigPredicate implements ConfigDependencyPredicate {
        @Override
        public boolean isSatisfied(ConfigOption option) {
            return FabricUtil.isDevelopmentEnvironment() &&
                    FabricLoader.getInstance().getMappingResolver()
                            .mapClassName("intermediary", "net.minecraft.class_310").equals("net.minecraft.client.Minecraft");
        }
    }
}
