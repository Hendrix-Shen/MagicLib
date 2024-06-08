package top.hendrixshen.magiclib.util.mixin;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.transformer.IMixinTransformer;
import org.spongepowered.asm.mixin.transformer.ext.Extensions;
import org.spongepowered.asm.mixin.transformer.ext.IExtension;
import org.spongepowered.asm.mixin.transformer.ext.IExtensionRegistry;
import org.spongepowered.asm.mixin.transformer.ext.ITargetClassContext;
import top.hendrixshen.magiclib.util.ReflectionUtil;
import top.hendrixshen.magiclib.util.collect.ValueContainer;

import java.util.List;
import java.util.Map;
import java.util.SortedSet;

public class MixinInternals {
    public static Extensions getExtensions() {
        IMixinTransformer transformer = (IMixinTransformer) MixinEnvironment.getDefaultEnvironment()
                .getActiveTransformer();
        return (Extensions) transformer.getExtensions();
    }

    public static SortedSet<IMixinInfo> getMixins(ITargetClassContext context) {
        ValueContainer<SortedSet<IMixinInfo>> mixins = ReflectionUtil.getDeclaredFieldValue(context.getClass(),
                "mixins", context);
        return mixins.orElseThrow();
    }

    public static Object getState(IMixinInfo context) {
        ValueContainer<Object> state = ReflectionUtil.getDeclaredFieldValue(context.getClass(),
                "state", context);
        return state.orElseThrow();
    }

    public static ClassNode getStateClassNode(IMixinInfo context) {
        Object state = MixinInternals.getState(context);
        ValueContainer<ClassNode> classNode = ReflectionUtil.getDeclaredFieldValue(state.getClass(),
                "classNode", state);
        return classNode.orElseThrow();
    }

    public static String getName(IMixinInfo info) {
        ValueContainer<String> name = ReflectionUtil.getDeclaredFieldValue(info.getClass(),
                "name", info);
        return name.orElseThrow();
    }

    public static void registerExtension(IExtension extension) {
        MixinInternals.registerExtension(extension, false);
    }

    public static void registerExtension(IExtension extension, boolean highPriority) {
        Pair<List<IExtension>, Map<Class<? extends IExtension>, IExtension>> container = MixinInternals.getExtensionContainer();

        if (highPriority) {
            container.getLeft().add(0, extension);
        } else {
            container.getLeft().add(extension);
        }

        container.getRight().put(extension.getClass(), extension);
    }

    public static void unregisterExtension(IExtension extension) {
        Pair<List<IExtension>, Map<Class<? extends IExtension>, IExtension>> container = MixinInternals.getExtensionContainer();
        container.getLeft().remove(extension);
        container.getRight().remove(extension.getClass());
    }

    private static @NotNull Pair<List<IExtension>, Map<Class<? extends IExtension>, IExtension>> getExtensionContainer() {
        IExtensionRegistry extensions = MixinInternals.getExtensions();
        ValueContainer<List<IExtension>> extensionListContainer = ReflectionUtil.getDeclaredFieldValue(
                "org.spongepowered.asm.mixin.transformer.ext.Extensions",
                "extensions", extensions);
        ValueContainer<Map<Class<? extends IExtension>, IExtension>> extensionMapContainer = ReflectionUtil
                .getDeclaredFieldValue("org.spongepowered.asm.mixin.transformer.ext.Extensions",
                        "extensionMap", extensions);
        return ImmutablePair.of(extensionListContainer.orElseThrow(), extensionMapContainer.orElseThrow());
    }
}
