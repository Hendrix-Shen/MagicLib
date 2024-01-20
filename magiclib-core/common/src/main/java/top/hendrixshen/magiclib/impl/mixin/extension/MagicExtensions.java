package top.hendrixshen.magiclib.impl.mixin.extension;

import org.jetbrains.annotations.ApiStatus;
import top.hendrixshen.magiclib.util.mixin.MixinInternals;

public class MagicExtensions {
    @ApiStatus.Internal
    public static void init() {
        MixinInternals.registerExtension(new AnnotationRestorerExtension());
        MixinInternals.registerExtension(new EraserExtension());
    }
}
