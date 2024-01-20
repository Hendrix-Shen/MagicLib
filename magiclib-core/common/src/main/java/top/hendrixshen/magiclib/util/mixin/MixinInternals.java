package top.hendrixshen.magiclib.util.mixin;

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

    public static void registerExtension(IExtension extension) {
        MixinInternals.registerExtension(extension, false);
    }

    public static void registerExtension(IExtension extension, boolean highPriority) {
        IExtensionRegistry extensions = MixinInternals.getExtensions();
        ValueContainer<List<IExtension>> extensionListContainer = ReflectionUtil.getDeclaredFieldValue(
                "org.spongepowered.asm.mixin.transformer.ext.Extensions",
                "extensions", extensions);
        ValueContainer<Map<Class<? extends IExtension>, IExtension>> extensionMapContainer = ReflectionUtil
                .getDeclaredFieldValue("org.spongepowered.asm.mixin.transformer.ext.Extensions",
                "extensionMap", extensions);
        List<IExtension> extensionsList = extensionListContainer.orElseThrow();
        Map<Class<? extends IExtension>, IExtension> extensionsMap = extensionMapContainer.orElseThrow();

        if (highPriority) {
            extensionsList.add(0, extension);
        } else {
            extensionsList.add(extension);
        }

        extensionsMap.put(extension.getClass(), extension);
    }

    public static void unregisterExtension(IExtension extension) {
        IExtensionRegistry extensions = MixinInternals.getExtensions();
        ValueContainer<List<IExtension>> extensionListContainer = ReflectionUtil.getDeclaredFieldValue(
                "org.spongepowered.asm.mixin.transformer.ext.Extensions",
                "extensions", extensions);
        ValueContainer<Map<Class<? extends IExtension>, IExtension>> extensionMapContainer = ReflectionUtil
                .getDeclaredFieldValue("org.spongepowered.asm.mixin.transformer.ext.Extensions",
                        "extensionMap", extensions);
        List<IExtension> extensionsList = extensionListContainer.orElseThrow();
        Map<Class<? extends IExtension>, IExtension> extensionsMap = extensionMapContainer.orElseThrow();
        extensionsList.remove(extension);
        extensionsMap.remove(extension.getClass());
    }
}
