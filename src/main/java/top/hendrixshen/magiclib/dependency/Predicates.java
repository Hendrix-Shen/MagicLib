package top.hendrixshen.magiclib.dependency;

import org.objectweb.asm.tree.ClassNode;
import top.hendrixshen.magiclib.MagicLibConfigs;
import top.hendrixshen.magiclib.config.Option;
import top.hendrixshen.magiclib.dependency.annotation.MixinDependencyPredicate;
import top.hendrixshen.magiclib.dependency.annotation.OptionDependencyPredicate;
import top.hendrixshen.magiclib.util.FabricUtil;

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
        public boolean test(ClassNode mixinClass) {
            return FabricUtil.isDevelopmentEnvironment();
        }
    }
}
