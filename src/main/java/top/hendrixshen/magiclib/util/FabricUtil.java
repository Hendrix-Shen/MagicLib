package top.hendrixshen.magiclib.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.VersionParsingException;
import net.fabricmc.loader.impl.gui.FabricGuiEntry;
import net.fabricmc.loader.impl.util.LoaderUtil;
import net.fabricmc.loader.impl.util.version.VersionPredicateParser;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import top.hendrixshen.magiclib.MagicLibReference;
import top.hendrixshen.magiclib.dependency.DepCheckException;
import top.hendrixshen.magiclib.dependency.Dependencies;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.CodeSource;
import java.security.SecureClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

public class FabricUtil {
    private static final Object knotClassLoaderObj;
    // Fabric Loader 0.11 and below support
    private static Method legacyVersionPredicateParser;
    private static Method legacyDisplayCriticalError;
    private static Method getMetadataMethod;
    private static Method getResourceMethod;
    private static Method defineClassFwdMethod;
    private static Object knotClassDelegateObj;
    private static Field codeSourceField;


    static {
        try {
            legacyVersionPredicateParser = Class.forName("net.fabricmc.loader.util.version.VersionPredicateParser").getMethod("matches", Version.class, String.class);
            legacyDisplayCriticalError = Class.forName("net.fabricmc.loader.gui.FabricGuiEntry").getMethod("displayCriticalError", Throwable.class, boolean.class);
        } catch (ClassNotFoundException | NoSuchMethodException ignored) {
        }
        knotClassLoaderObj = Thread.currentThread().getContextClassLoader();


        try {
            Class<?> knotClassLoader;
            Class<?> knotClassDelegate;
            Class<?> metadata;

            Field delegateField;

            if (legacyVersionPredicateParser == null) {
                knotClassLoader = Class.forName("net.fabricmc.loader.impl.launch.knot.KnotClassLoader");
                defineClassFwdMethod = knotClassLoader.getMethod("defineClassFwd", String.class, byte[].class, int.class, int.class, CodeSource.class);
                defineClassFwdMethod.setAccessible(true);
                getResourceMethod = knotClassLoader.getMethod("getResource", String.class);
                getResourceMethod.setAccessible(true);

                knotClassDelegate = Class.forName("net.fabricmc.loader.impl.launch.knot.KnotClassDelegate");
                getMetadataMethod = knotClassDelegate.getDeclaredMethod("getMetadata", String.class, URL.class);
                getMetadataMethod.setAccessible(true);

                metadata = Class.forName("net.fabricmc.loader.impl.launch.knot.KnotClassDelegate$Metadata");
                delegateField = knotClassLoader.getDeclaredField("delegate");
                delegateField.setAccessible(true);
                codeSourceField = metadata.getDeclaredField("codeSource");
                codeSourceField.setAccessible(true);

                knotClassDelegateObj = delegateField.get(knotClassLoaderObj);


            } else {

                knotClassLoader = Class.forName("net.fabricmc.loader.launch.knot.KnotClassLoader");
                defineClassFwdMethod = SecureClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class, CodeSource.class);
                defineClassFwdMethod.setAccessible(true);
                getResourceMethod = knotClassLoader.getMethod("getResource", String.class);
                getResourceMethod.setAccessible(true);

                knotClassDelegate = Class.forName("net.fabricmc.loader.launch.knot.KnotClassDelegate");
                getMetadataMethod = knotClassDelegate.getDeclaredMethod("getMetadata", String.class, URL.class);
                getMetadataMethod.setAccessible(true);

                metadata = Class.forName("net.fabricmc.loader.launch.knot.KnotClassDelegate$Metadata");
                delegateField = knotClassLoader.getDeclaredField("delegate");
                delegateField.setAccessible(true);
                codeSourceField = metadata.getDeclaredField("codeSource");
                codeSourceField.setAccessible(true);

                knotClassDelegateObj = delegateField.get(knotClassLoaderObj);
//                knotClassLoader = Class.forName("net.fabricmc.loader.launch.knot.KnotClassLoader");
//                knotClassDelegate = Class.forName("net.fabricmc.loader.launch.knot.KnotClassDelegate");
//                knotClassLoaderInterface = Class.forName("net.fabricmc.loader.impl.launch.knot.KnotClassLoaderInterface");
            }


        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
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


    @SuppressWarnings("deprecation")
    private static Map<String, Dependencies<Object>> getModInitDependencies(String entryKey, String entryMethod) {
        Map<String, Dependencies<Object>> ret = new HashMap<>();
        FabricLoader.getInstance().getAllMods().forEach(modContainer ->
                ((net.fabricmc.loader.ModContainer) modContainer).getInfo().getEntrypoints(entryKey).forEach(
                        entrypointMetadata -> {
                            Dependencies<Object> dependencies = Dependencies.getFabricEntrypointDependencies(entrypointMetadata.getValue(), entryMethod);
                            if (dependencies != null) {
                                ret.put(modContainer.getMetadata().getId(), dependencies);
                            }
                        }
                ));
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

        getModInitDependencies("main", "onInitialize").forEach(depCheckCallback);
        if (fabricLoader.getEnvironmentType() == EnvType.CLIENT) {
            getModInitDependencies("client", "onInitializeClient").forEach(depCheckCallback);
        } else {
            getModInitDependencies("server", "onInitializeServer").forEach(depCheckCallback);
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


    public static void loadClass(String name, ClassNode classNode) {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(cw);
        byte[] classNodeBytes = cw.toByteArray();
        Object metaDataObj;
        try {
            metaDataObj = getMetadataMethod.invoke(knotClassDelegateObj, name, getResourceMethod.invoke(knotClassLoaderObj, LoaderUtil.getClassFileName(name)));
            defineClassFwdMethod.invoke(knotClassLoaderObj, classNode.name.replace("/", "."), classNodeBytes, 0, classNodeBytes.length, codeSourceField.get(metaDataObj));
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
