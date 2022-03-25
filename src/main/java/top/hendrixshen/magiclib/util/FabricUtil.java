package top.hendrixshen.magiclib.util;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.VersionParsingException;
import net.fabricmc.loader.impl.gui.FabricGuiEntry;
import net.fabricmc.loader.impl.util.version.VersionPredicateParser;
import top.hendrixshen.magiclib.MagicLibReference;
import top.hendrixshen.magiclib.dependency.DepCheckException;
import top.hendrixshen.magiclib.dependency.Dependencies;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

public class FabricUtil {
    // Fabric Loader 0.11 and below support
    private static Method legacyVersionPredicateParser;
    private static Method legacyDisplayCriticalError;

    static {
        try {
            legacyVersionPredicateParser = Class.forName("net.fabricmc.loader.util.version.VersionPredicateParser").getMethod("matches", Version.class, String.class);
        } catch (ClassNotFoundException | NoSuchMethodException ignored) {
        }
        try {
            legacyDisplayCriticalError = Class.forName("net.fabricmc.loader.gui.FabricGuiEntry").getMethod("displayCriticalError", Throwable.class, boolean.class);
        } catch (ClassNotFoundException | NoSuchMethodException ignored) {
        }
    }

    /**
     * Verify that the Fabric Loader has loaded the qualified mods.
     *
     * @param version          Version provided by the fabric loader.
     * @param versionPredicate Semantic versioning expression.
     * @return True if the Fabric Loader finds a matching mods from the list of
     * loaded mods, false otherwise.
     */
    public static boolean isModLoaded(Version version, String versionPredicate) {
        try {
            if (legacyVersionPredicateParser != null) {
                try {
                    return (boolean) legacyVersionPredicateParser.invoke(null, version, versionPredicate);
                } catch (InvocationTargetException | IllegalAccessException e) {
                    MagicLibReference.LOGGER.error("Failed to invoke VersionPredicateParser#matches", e);
                }
            }
            return VersionPredicateParser.parse(versionPredicate).test(version);
        } catch (VersionParsingException e) {
            MagicLibReference.LOGGER.error("Failed to parse version or version predicate {} {}: {}", version.getFriendlyString(), versionPredicate, e);
            return false;
        }
    }

    /**
     * Verify that the Fabric Loader has loaded the qualified mods.
     *
     * @param modId Version provided by the fabric loader.
     * @return True if the Fabric Loader finds a matching mod from the list of
     * loaded mods, false otherwise.
     */
    public static boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    /**
     * Verify that the Fabric Loader has loaded the qualified mod.
     *
     * @param modId            Version provided by the fabric loader.
     * @param versionPredicate Semantic versioning expression.
     * @return True if the Fabric Loader finds a matching mod from the list of
     * loaded mods, false otherwise.
     */
    public static boolean isModLoaded(String modId, String versionPredicate) {
        Optional<ModContainer> modContainerOptional = FabricLoader.getInstance().getModContainer(modId);
        if (modContainerOptional.isPresent()) {
            ModContainer modContainer = modContainerOptional.get();
            return isModLoaded(modContainer.getMetadata().getVersion(), versionPredicate);
        }
        return false;
    }


    private static Map<String, Dependencies<Object>> getModInitDependencies(Class<?> clazz, String entryKey, String entryMethod) {
        Map<String, Dependencies<Object>> ret = new HashMap<>();
        FabricLoader.getInstance().getEntrypointContainers(entryKey, clazz).forEach(entrypointContainer -> {
            top.hendrixshen.magiclib.dependency.annotation.Dependencies dependenciesAnnotation;
            try {
                dependenciesAnnotation = entrypointContainer.getEntrypoint().getClass().getMethod(entryMethod).getAnnotation(top.hendrixshen.magiclib.dependency.annotation.Dependencies.class);
            } catch (NoSuchMethodException e) {
                MagicLibReference.LOGGER.error(e);
                return;
            }
            if (dependenciesAnnotation == null) {
                return;
            }
            ret.put(entrypointContainer.getProvider().getMetadata().getId(), Dependencies.of(dependenciesAnnotation, Object.class));
        });
        return ret;
    }

    /**
     * Interrupted only if the FabricLoader loads a mod version that does not
     * match the rules in the list.
     */
    public static void compatVersionCheck() {
        FabricLoader fabricLoader = FabricLoader.getInstance();

        StringBuilder result = new StringBuilder();
        BiConsumer<String, Dependencies<Object>> depCheckCallback = (modId, dependencies) -> {
            String depResult = dependencies.getCheckResult(null);
            if (!depResult.equals(Dependencies.SATISFIED)) {
                if (result.length() != 0) {
                    result.append("\n");
                }
                result.append(String.format("Mod %s compat version check failed.\n", modId));
                result.append(depResult);
            }
        };

        getModInitDependencies(ModInitializer.class, "main", "onInitialize").forEach(depCheckCallback);
        if (fabricLoader.getEnvironmentType() == EnvType.CLIENT) {
            getModInitDependencies(ClientModInitializer.class, "client", "onInitializeClient").forEach(depCheckCallback);
        } else {
            getModInitDependencies(DedicatedServerModInitializer.class, "server", "onInitializeServer").forEach(depCheckCallback);
        }

        if (result.length() != 0) {
            displayCriticalError(new DepCheckException(String.format("Mod resolution encountered an incompatible mod set!\n %s", result)));
        }
    }

    /**
     * Exposing the Fabric loader graphical exception display window method.
     *
     * @param exception Thrown exceptions.
     */
    public static void displayCriticalError(Throwable exception) {
        String nm = System.getProperty("java.awt.headless");
        if (Boolean.parseBoolean(nm)) {
            System.setProperty("java.awt.headless", "");
        }
        if (legacyDisplayCriticalError != null) {
            try {
                legacyDisplayCriticalError.invoke(null, exception, true);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        } else {
            FabricGuiEntry.displayCriticalError(exception, true);
        }
    }

    /**
     * @return True if started with Fabric Loom.
     */
    public static boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }
}
