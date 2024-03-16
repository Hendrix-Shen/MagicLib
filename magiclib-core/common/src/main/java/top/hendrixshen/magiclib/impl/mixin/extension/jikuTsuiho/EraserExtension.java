package top.hendrixshen.magiclib.impl.mixin.extension.jikuTsuiho;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.transformer.ext.ITargetClassContext;
import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.api.mixin.extension.EmptyExtension;
import top.hendrixshen.magiclib.api.mixin.extension.jikuTsuiho.info.MixinFieldInfo;
import top.hendrixshen.magiclib.api.mixin.extension.jikuTsuiho.info.MixinMethodInfo;
import top.hendrixshen.magiclib.impl.mixin.extension.jikuTsuiho.info.MixinClassInfoImpl;
import top.hendrixshen.magiclib.impl.mixin.extension.jikuTsuiho.info.MixinFieldInfoImpl;
import top.hendrixshen.magiclib.impl.mixin.extension.jikuTsuiho.info.MixinMethodInfoImpl;
import top.hendrixshen.magiclib.util.mixin.MixinInternals;

import java.util.Iterator;
import java.util.SortedSet;

public class EraserExtension extends EmptyExtension {
    @Override
    public boolean checkActive(MixinEnvironment environment) {
        return true;
    }

    @Override
    public void preApply(ITargetClassContext context) {
        SortedSet<IMixinInfo> mixins = MixinInternals.getMixins(context);
        Iterator<IMixinInfo> mixinInfoIterator = mixins.iterator();

        while (mixinInfoIterator.hasNext()) {
            IMixinInfo info = mixinInfoIterator.next();
            MixinClassInfoImpl mixinClassInfo = new MixinClassInfoImpl(info, context.getClassNode());
            boolean tsuihoClass = MixinEraserManager.shouldTsuiho(mixinClassInfo);

            if (tsuihoClass) {
                mixinInfoIterator.remove();

                if (MixinEnvironment.getCurrentEnvironment()
                        .getOption(MixinEnvironment.Option.DEBUG_EXPORT)) {
                    MagicLib.getLogger().warn("JikuTsuiho: {}", info.getClassName());
                }
            }

            boolean tsuiho = false;
            ClassNode classNode = MixinInternals.getStateClassNode(info);
            Iterator<MethodNode> methodNodeIterator = classNode.methods.iterator();

            while (methodNodeIterator.hasNext()) {
                MethodNode methodNode = methodNodeIterator.next();
                MixinMethodInfo methodInfo = MixinMethodInfoImpl.of(info, methodNode);
                boolean tsuihoMethod = MixinEraserManager.shouldTsuiho(methodInfo);

                if (tsuihoMethod) {
                    methodNodeIterator.remove();
                    tsuiho = true;

                    if (MixinEnvironment.getCurrentEnvironment()
                            .getOption(MixinEnvironment.Option.DEBUG_EXPORT)) {
                        MagicLib.getLogger().warn("JikuTsuiho: {} {} {}", info.getClassName(),
                                methodInfo.getName(), methodInfo.getDesc());
                    }
                }
            }


            Iterator<FieldNode> fieldNodeIterator = classNode.fields.iterator();

            while (fieldNodeIterator.hasNext()) {
                FieldNode fieldNode = fieldNodeIterator.next();
                MixinFieldInfo fieldInfo = new MixinFieldInfoImpl(info, fieldNode);
                boolean tsuihoField = MixinEraserManager.shouldTsuiho(fieldInfo);

                if (tsuihoField) {
                    fieldNodeIterator.remove();
                    tsuiho = true;

                    if (MixinEnvironment.getCurrentEnvironment()
                            .getOption(MixinEnvironment.Option.DEBUG_EXPORT)) {
                        MagicLib.getLogger().warn("JikuTsuiho: {} {} {}", info.getClassName(),
                                fieldInfo.getName(), fieldInfo.getDesc());
                    }
                }
            }

            if (tsuiho && classNode.fields.isEmpty() && classNode.methods.isEmpty()) {
                mixinInfoIterator.remove();
                MagicLib.getLogger().warn("JikuTsuiho: {}", info.getClassName());
            }
        }
    }
}
