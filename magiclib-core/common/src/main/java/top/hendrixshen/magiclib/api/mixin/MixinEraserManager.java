package top.hendrixshen.magiclib.api.mixin;

import com.google.common.collect.Sets;
import top.hendrixshen.magiclib.MagicLib;

import java.util.List;
import java.util.Set;

public class MixinEraserManager {
    private static final Set<IMixinEraser> erasers = Sets.newHashSet();

    public static boolean shouldErase(List<String> targetClassNames, String mixinClassName) {
        return MixinEraserManager.erasers
                .stream()
                .anyMatch(eraser -> eraser.shouldErase(targetClassNames, mixinClassName));
    }

    public static void register(IMixinEraser eraser) {
        MixinEraserManager.erasers.add(eraser);
        MagicLib.getLogger().info("Registered mixin eraser: {}", eraser.getClass().getName());
    }
}
