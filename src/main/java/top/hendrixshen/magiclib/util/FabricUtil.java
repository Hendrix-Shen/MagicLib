package top.hendrixshen.magiclib.util;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.VersionParsingException;
import net.fabricmc.loader.api.metadata.CustomValue;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.fabricmc.loader.impl.gui.FabricGuiEntry;
import net.fabricmc.loader.impl.util.version.VersionPredicateParser;
import top.hendrixshen.magiclib.MagicLib;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;

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
                    MagicLib.getLogger().error("Failed to invoke VersionPredicateParser#matches", e);
                }
            }
            return VersionPredicateParser.parse(versionPredicate).test(version);
        } catch (VersionParsingException e) {
            MagicLib.getLogger().error("Failed to parse version or version predicate {} {}: {}", version.getFriendlyString(), versionPredicate, e);
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

    /**
     * Interrupted only if the FabricLoader loads a mod version that does not
     * match the rules in the list.
     *
     * @param currentModId Your Mod Identifier.
     */
    public static void compatVersionCheck(String currentModId) {
        FabricLoader fabricLoader = FabricLoader.getInstance();
        Optional<ModContainer> currentModContainer = fabricLoader.getModContainer(currentModId);
        if (currentModContainer.isPresent()) {
            StringBuilder exceptionString = new StringBuilder();
            CustomValue customValue = currentModContainer.get().getMetadata().getCustomValue("compat");
            if (customValue == null) {
                return;
            }
            for (Map.Entry<String, CustomValue> customValueEntry : customValue.getAsObject()) {
                if (isModLoaded(customValueEntry.getKey()) && !isModLoaded(customValueEntry.getKey(), customValueEntry.getValue().getAsString())) {
                    fabricLoader.getModContainer(customValueEntry.getKey()).ifPresent(modContainer -> {
                        ModMetadata metadata = modContainer.getMetadata();
                        String targetModId = metadata.getId();
                        String targetModName = metadata.getName();
                        String targetModVersion = metadata.getVersion().getFriendlyString();
                        exceptionString.append(String.format("\n\tMod %s (%s) detected. Requires [%s], but found %s!",
                                targetModName, targetModId, customValueEntry.getValue().getAsString(), targetModVersion));
                    });
                }
            }
            if (!exceptionString.toString().isEmpty()) {
                displayCriticalError(new IllegalStateException(String.format("Mod resolution encountered an incompatible mod set!%s", exceptionString)));
            }

        }
    }

    /**
     * Exposing the Fabric loader graphical exception display window method.
     *
     * @param exception Thrown exceptions.
     */
    public static void displayCriticalError(Throwable exception) {
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
