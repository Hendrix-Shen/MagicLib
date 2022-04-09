package top.hendrixshen.magiclib.compat;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.transformer.ext.IExtension;
import org.spongepowered.asm.mixin.transformer.ext.ITargetClassContext;
import top.hendrixshen.magiclib.MagicLibReference;
import top.hendrixshen.magiclib.dependency.Dependencies;
import top.hendrixshen.magiclib.dependency.mixin.DepCheckFailureCallback;
import top.hendrixshen.magiclib.util.MixinUtil;

import java.util.Optional;

public class MagicExtension implements IExtension {

    public static DepCheckFailureCallback depCheckFailureCallback =
            (targetClassName, mixinClassName, reason) -> {
                if (MixinEnvironment.getCurrentEnvironment().getOption(MixinEnvironment.Option.DEBUG_EXPORT)) {
                    MagicLibReference.LOGGER.warn("{}: \nMixin {} can't apply to {} because: \n{}",
                            Optional.ofNullable(reason.getCause()).orElse(reason).getClass().getSimpleName(),
                            mixinClassName, targetClassName, reason.getMessage());
                }
            };

    @Override
    public boolean checkActive(MixinEnvironment environment) {
        return true;
    }

    @Override
    public void preApply(ITargetClassContext context) {
        MixinUtil.getMixins(context).removeIf(
                iMixinInfo -> iMixinInfo.getTargetClasses().stream().anyMatch(
                        targetClassName ->
                                !Dependencies.checkDependency(targetClassName, iMixinInfo.getClassName(), depCheckFailureCallback)));
    }

    @Override
    public void postApply(ITargetClassContext context) {
        ClassNode classNode = context.getClassNode();
        MixinUtil.applyPublic(classNode);
        MixinUtil.applyInit(classNode);
        for (IMixinInfo iMixinInfo : MixinUtil.getMixins(context)) {
            MixinUtil.applyInnerClass(classNode, iMixinInfo.getClassNode(ClassReader.SKIP_CODE));
        }
        MixinUtil.applyRemap(classNode);

    }

    @Override
    public void export(MixinEnvironment env, String name, boolean force, ClassNode classNode) {

    }
}
