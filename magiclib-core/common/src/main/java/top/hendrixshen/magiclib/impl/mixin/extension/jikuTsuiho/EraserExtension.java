package top.hendrixshen.magiclib.impl.mixin.extension.jikuTsuiho;

import org.objectweb.asm.tree.ClassNode;
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

import java.util.SortedSet;

public class EraserExtension extends EmptyExtension {
    @Override
    public boolean checkActive(MixinEnvironment environment) {
        return true;
    }

    @Override
    public void preApply(ITargetClassContext context) {
        SortedSet<IMixinInfo> mixins = MixinInternals.getMixins(context);

        mixins.removeIf(info -> {
            MixinClassInfoImpl mixinClassInfo = new MixinClassInfoImpl(info, context.getClassNode());
            boolean tsuihoClass = MixinEraserManager.shouldTsuiho(mixinClassInfo);

            if (tsuihoClass) {
                if (MixinEnvironment.getCurrentEnvironment()
                        .getOption(MixinEnvironment.Option.DEBUG_EXPORT)) {
                    MagicLib.getLogger().warn("JikuTsuiho: {}", info.getClassName());
                }

                return true;
            }

            ClassNode classNode = MixinInternals.getStateClassNode(info);
            classNode.methods.removeIf(methodNode -> {
                MixinMethodInfo methodInfo = MixinMethodInfoImpl.of(info, methodNode);
                boolean tsuihoMethod = MixinEraserManager.shouldTsuiho(methodInfo);

                if (tsuihoMethod && MixinEnvironment.getCurrentEnvironment()
                        .getOption(MixinEnvironment.Option.DEBUG_EXPORT)) {
                    MagicLib.getLogger().warn("JikuTsuiho: {} {} {}", info.getClassName(),
                            methodInfo.getName(), methodInfo.getDesc());
                }

                return tsuihoMethod;
            });

            classNode.fields.removeIf(fieldNode -> {
                MixinFieldInfo fieldInfo = new MixinFieldInfoImpl(info, fieldNode);
                boolean tsuihoField = MixinEraserManager.shouldTsuiho(fieldInfo);

                if (tsuihoField && MixinEnvironment.getCurrentEnvironment()
                        .getOption(MixinEnvironment.Option.DEBUG_EXPORT)) {
                    MagicLib.getLogger().warn("JikuTsuiho: {} {} {}", info.getClassName(),
                            fieldInfo.getName(), fieldInfo.getDesc());
                }

                return tsuihoField;
            });

            tsuihoClass = classNode.methods.isEmpty() && classNode.fields.isEmpty();

            if (tsuihoClass) {
                MagicLib.getLogger().warn("JikuTsuiho: {}", info.getClassName());
            }

            return classNode.methods.isEmpty() && classNode.fields.isEmpty();
        });
    }
}
