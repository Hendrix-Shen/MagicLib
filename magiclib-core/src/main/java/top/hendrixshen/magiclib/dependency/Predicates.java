package top.hendrixshen.magiclib.dependency;

import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import top.hendrixshen.magiclib.MagicLibConfigs;
import top.hendrixshen.magiclib.config.Option;
import top.hendrixshen.magiclib.dependency.annotation.MixinDependencyPredicate;
import top.hendrixshen.magiclib.dependency.annotation.OptionDependencyPredicate;
import top.hendrixshen.magiclib.util.FabricUtil;

/**
 * MagicLib built-in predicates.
 * <p>
 * Some basic uses of predicates are given here.
 */
public class Predicates {
    /**
     * Predicate that implements {@link OptionDependencyPredicate} for config predicate checking.
     * <p>
     * Default value for config predicate check, this predicate always returns true.
     */
    public static class TrueOptionPredicate implements OptionDependencyPredicate {
        @Override
        public boolean test(Option option) {
            return true;
        }
    }

    /**
     * Predicate that implements {@link OptionDependencyPredicate} for config predicate checking.
     * <p>
     * This predicate returns true only when MagicLib debug mode is enabled.
     */
    public static class DebugOptionPredicate implements OptionDependencyPredicate {
        @Override
        public boolean test(Option option) {
            return MagicLibConfigs.debug;
        }
    }

    /**
     * Predicate that implements {@link OptionDependencyPredicate} for config predicate checking.
     * <p>
     * This predicate returns true only when Fabric is enabled in the development environment.
     */
    public static class DevOptionPredicate implements OptionDependencyPredicate {
        @Override
        public boolean test(Option option) {
            return FabricUtil.isDevelopmentEnvironment();
        }
    }

    /**
     * Predicate that implements {@link MixinDependencyPredicate} for config predicate checking.
     * <p>
     * This predicate returns true only when Fabric is enabled in the development environment.
     */
    public static class DevMixinPredicate implements MixinDependencyPredicate {

        @Override
        public boolean test(ClassNode mixinClass) {
            return FabricUtil.isDevelopmentEnvironment();
        }
    }


    /**
     * Predicate that implements {@link OptionDependencyPredicate} for config predicate checking.
     * <p>
     * This predicate returns true only when Fabric is enabled in the development environment and use mojang's mapping.
     */
    public static class DevMojangOptionPredicate implements OptionDependencyPredicate {
        @Override
        public boolean test(Option option) {
            return FabricUtil.isDevelopmentEnvironment() &&
                    FabricLoader.getInstance().getMappingResolver()
                            .mapClassName("intermediary", "net.minecraft.class_310").equals("net.minecraft.client.Minecraft");
        }
    }

    /**
     * Predicate that implements {@link MixinDependencyPredicate} for config predicate checking.
     * <p>
     * This predicate returns true only when Fabric is enabled in the development environment and use mojang's mapping.
     */
    public static class DevMojangMixinPredicate implements MixinDependencyPredicate {

        @Override
        public boolean test(ClassNode mixinClass) {
            return FabricUtil.isDevelopmentEnvironment() &&
                    FabricLoader.getInstance().getMappingResolver()
                            .mapClassName("intermediary", "net.minecraft.class_310").equals("net.minecraft.client.Minecraft");
        }
    }
}
