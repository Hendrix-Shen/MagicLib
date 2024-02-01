package top.hendrixshen.magiclib.api.mixin.extension;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.transformer.ext.IExtension;
import org.spongepowered.asm.mixin.transformer.ext.ITargetClassContext;

public class EmptyExtension implements IExtension {
    @Override
    public boolean checkActive(MixinEnvironment environment) {
        return false;
    }

    @Override
    public void preApply(ITargetClassContext context) {
    }

    @Override
    public void postApply(ITargetClassContext context) {
    }

    @Override
    public void export(MixinEnvironment env, String name, boolean force, ClassNode classNode) {
    }
}
