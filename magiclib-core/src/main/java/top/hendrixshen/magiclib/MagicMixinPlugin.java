package top.hendrixshen.magiclib;

import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.transformer.ext.Extensions;
import top.hendrixshen.magiclib.compat.MagicExtension;
import top.hendrixshen.magiclib.dependency.Dependencies;
import top.hendrixshen.magiclib.dependency.mixin.DepCheckFailureCallback;
import top.hendrixshen.magiclib.util.FabricUtil;
import top.hendrixshen.magiclib.util.MagicStreamHandler;
import top.hendrixshen.magiclib.util.MixinUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Optional;

public class MagicMixinPlugin extends EmptyMixinPlugin {
    private static boolean compatVersionChecked = false;

    private DepCheckFailureCallback depCheckFailureCallback =
            (targetClassName, mixinClassName, reason) -> {
                if (MixinEnvironment.getCurrentEnvironment().getOption(MixinEnvironment.Option.DEBUG_EXPORT)) {
                    MagicLibReference.LOGGER.warn("{}: \nMixin {} can't apply to {} because: \n{}",
                            Optional.ofNullable(reason.getCause()).orElse(reason).getClass().getSimpleName(),
                            mixinClassName, targetClassName, reason.getMessage());
                }
            };

    public void setDepCheckFailureCallback(DepCheckFailureCallback depCheckFailureCallback) {
        this.depCheckFailureCallback = depCheckFailureCallback;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        // 不放进 extension 是因为如果放进去的话，就算在 preApply 中移除 mixin，日志里仍然会喷射出一堆 warning
        return Dependencies.checkDependency(targetClassName, mixinClassName, this.depCheckFailureCallback);
    }

    @Override
    public void onLoad(String mixinPackage) {
        if (!compatVersionChecked) {
            compatVersionChecked = true;
            FabricUtil.compatVersionCheck();

            try {
                Object transformer = MixinEnvironment.getCurrentEnvironment().getActiveTransformer();
                if (transformer == null) {
                    throw new IllegalStateException("Not running with a transformer?");
                }
                Field extensionsField = transformer.getClass().getDeclaredField("extensions");
                extensionsField.setAccessible(true);
                Extensions extensions = (Extensions) extensionsField.get(transformer);
                extensions.add(new MagicExtension());
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }


            Object urlLoader = Thread.currentThread().getContextClassLoader();
            Class<?> knotClassLoader;
            try {
                knotClassLoader = Class.forName("net.fabricmc.loader.impl.launch.knot.KnotClassLoader");

            } catch (ClassNotFoundException e) {
                try {
                    knotClassLoader = Class.forName("net.fabricmc.loader.launch.knot.KnotClassLoader");
                } catch (ClassNotFoundException e1) {
                    throw new RuntimeException(e1);
                }
            }

            try {
                Method method = knotClassLoader.getDeclaredMethod("addURL", URL.class);
                method.setAccessible(true);
                method.invoke(urlLoader, MagicStreamHandler.getMemoryClassLoaderUrl());
            } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        MixinUtil.commitMagicClass();
    }
}
