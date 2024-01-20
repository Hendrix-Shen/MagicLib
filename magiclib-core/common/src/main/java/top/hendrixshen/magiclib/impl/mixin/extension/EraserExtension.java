package top.hendrixshen.magiclib.impl.mixin.extension;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.transformer.ext.IExtension;
import org.spongepowered.asm.mixin.transformer.ext.ITargetClassContext;
import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.api.mixin.MixinEraserManager;
import top.hendrixshen.magiclib.util.mixin.MixinInternals;

import java.util.SortedSet;

public class EraserExtension implements IExtension {
    @Override
    public boolean checkActive(MixinEnvironment environment) {
        return true;
    }

    @Override
    public void preApply(ITargetClassContext context) {
        SortedSet<IMixinInfo> mixins = MixinInternals.getMixins(context);

        mixins.removeIf(info -> {
            boolean shouldErase = MixinEraserManager.shouldErase(info.getTargetClasses(), info.getClassName());

            if (shouldErase && MixinEnvironment.getCurrentEnvironment()
                    .getOption(MixinEnvironment.Option.DEBUG_EXPORT)) {
                MagicLib.getLogger().warn("Erasing mixin {}", info.getClassName());
            }

            return shouldErase;
        });
    }

    @Override
    public void postApply(ITargetClassContext context) {
    }

    @Override
    public void export(MixinEnvironment env, String name, boolean force, ClassNode classNode) {
    }
}
