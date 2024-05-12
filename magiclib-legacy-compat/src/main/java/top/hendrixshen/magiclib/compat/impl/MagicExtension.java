package top.hendrixshen.magiclib.compat.impl;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.transformer.ext.IExtension;
import org.spongepowered.asm.mixin.transformer.ext.ITargetClassContext;
import top.hendrixshen.magiclib.util.MixinUtil;

@Deprecated
@ApiStatus.ScheduledForRemoval
public class MagicExtension implements IExtension {
    @Override
    public boolean checkActive(MixinEnvironment environment) {
        return true;
    }

    @Override
    public void preApply(ITargetClassContext context) {
    }

    @Override
    public void postApply(@NotNull ITargetClassContext context) {
        ClassNode classNode = context.getClassNode();
        MixinUtil.applyPublic(classNode);
        MixinUtil.applyInit(classNode);
        MixinUtil.applyRemap(classNode);
    }

    @Override
    public void export(MixinEnvironment env, String name, boolean force, ClassNode classNode) {
    }
}
