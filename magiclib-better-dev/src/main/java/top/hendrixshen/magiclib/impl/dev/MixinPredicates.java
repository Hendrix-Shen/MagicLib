package top.hendrixshen.magiclib.impl.dev;

import org.objectweb.asm.tree.ClassNode;
import top.hendrixshen.magiclib.MagicLibProperties;
import top.hendrixshen.magiclib.api.mixin.MixinPredicate;

public class MixinPredicates {
    public static class AuthEmptyKeyPredicate implements MixinPredicate {
        @Override
        public boolean test(ClassNode classNode) {
            return MagicLibProperties.DEV_QOL_AUTH_EMPTY_KEY.getBooleanValue();
        }
    }

    public static class AuthVerifyPredicate implements MixinPredicate {
        @Override
        public boolean test(ClassNode classNode) {
            return MagicLibProperties.DEV_QOL_AUTH.getBooleanValue();
        }
    }

    public static class ChunkPredicate implements MixinPredicate {
        @Override
        public boolean test(ClassNode classNode) {
            return MagicLibProperties.DEV_QOL_CHUNK.getBooleanValue();
        }
    }

    public static class DestroyDFUPredicate implements MixinPredicate {
        @Override
        public boolean test(ClassNode classNode) {
            return MagicLibProperties.DEV_QOL_DFU_BREAK.getBooleanValue();
        }
    }

    public static class LazyDFUPredicate implements MixinPredicate {
        @Override
        public boolean test(ClassNode classNode) {
            return MagicLibProperties.DEV_QOL_DFU_LAZY.getBooleanValue();
        }
    }

    public static class TheadTweakPredicate implements MixinPredicate {
        @Override
        public boolean test(ClassNode classNode) {
            return MagicLibProperties.DEV_QOL_THREAD_TWEAK.getBooleanValue();
        }
    }
}
