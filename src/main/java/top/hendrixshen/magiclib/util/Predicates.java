package top.hendrixshen.magiclib.util;

import org.objectweb.asm.tree.ClassNode;
import top.hendrixshen.magiclib.MagicLibConfigs;
import top.hendrixshen.magiclib.api.dependencyValidator.annotation.MixinDependencyPredicate;
import top.hendrixshen.magiclib.api.dependencyValidator.annotation.OptionDependencyPredicate;
import top.hendrixshen.magiclib.config.Option;

public class Predicates {

    public static class TrueOptionPredicate implements OptionDependencyPredicate {
        @Override
        public boolean test(Option option) {
            return true;
        }
    }

    public static class DebugOptionPredicate implements OptionDependencyPredicate {
        @Override
        public boolean test(Option option) {
            return MagicLibConfigs.debug;
        }
    }

    public static class DevOptionPredicate implements OptionDependencyPredicate {
        @Override
        public boolean test(Option option) {
            return FabricUtil.isDevelopmentEnvironment();
        }
    }

    public static class DevMixinPredicate implements MixinDependencyPredicate {
        @Override
        public boolean test(ClassNode classNode) {
            return FabricUtil.isDevelopmentEnvironment();
        }
    }
}
