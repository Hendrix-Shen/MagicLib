package top.hendrixshen.magiclib.compat;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.transformer.ext.IExtension;
import org.spongepowered.asm.mixin.transformer.ext.ITargetClassContext;
import top.hendrixshen.magiclib.util.MixinUtil;

public class MagicExtension implements IExtension {
    @Override
    public boolean checkActive(MixinEnvironment environment) {
        return true;
    }

    @Override
    public void preApply(ITargetClassContext context) {
        for (IMixinInfo iMixinInfo : MixinUtil.getMixins(context)) {
            ClassNode mixinClassNode = iMixinInfo.getClassNode(ClassReader.SKIP_CODE);
            MixinUtil.remapInterface(mixinClassNode);
            MixinUtil.applyInnerClass(context.getClassNode(), mixinClassNode);
        }
    }

    @Override
    public void postApply(ITargetClassContext context) {
        ClassNode classNode = context.getClassNode();
        MixinUtil.applyPublic(classNode);
        MixinUtil.applyInit(classNode);
        MixinUtil.applyRemap(classNode);

    }

    @Override
    public void export(MixinEnvironment env, String name, boolean force, ClassNode classNode) {
    }
}
