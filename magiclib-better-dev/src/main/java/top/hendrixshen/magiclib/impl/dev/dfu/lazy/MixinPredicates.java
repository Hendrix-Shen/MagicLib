package top.hendrixshen.magiclib.impl.dev.dfu.lazy;

import org.objectweb.asm.tree.ClassNode;
import top.hendrixshen.magiclib.MagicLibProperties;
import top.hendrixshen.magiclib.api.mixin.MixinPredicate;

public class MixinPredicates {
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

    public static class ChunkPredicate implements MixinPredicate {
        @Override
        public boolean test(ClassNode classNode) {
            return MagicLibProperties.DEV_QOL_CHUNK.getBooleanValue();
        }
    }
}
