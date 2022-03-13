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
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public class FabricUtil {
    // Fabric Loader 0.11 and below support
    private static Method legacyVersionPredicateParser;
    private static Method legacyDisplayCriticalError;

    private final static FabricLoader fabricLoader = FabricLoader.getInstance();

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
     * @param version           Version provided by the fabric loader.
     * @param versionPredicates Semantic versioning expressions collection.
     * @return True if the Fabric Loader finds a matching mods from the list of
     * loaded mods, false otherwise.
     */
    public static boolean isModsLoaded(Version version, Collection<String> versionPredicates) {
        return versionPredicates.isEmpty() || versionPredicates.stream().anyMatch(vp -> isModLoaded(version, vp));
    }

    /**
     * Verify that the Fabric Loader has loaded the qualified mod.
     *
     * @param modId Version provided by the fabric loader.
     * @param versionPredicates versionPredicates – Semantic versioning expressions.
     * @return True if the Fabric Loader finds a matching mod from the list of
     * loaded mods, false otherwise.
     */
    public static boolean isModsLoaded(String modId, Collection<String> versionPredicates) {
        return FabricLoader.getInstance().getModContainer(modId).
                map(mod -> isModsLoaded(mod.getMetadata().getVersion(), versionPredicates)).
                orElse(false);
    }

    /**
     * Verify that the Fabric Loader has loaded the qualified mods.
     *
     * @param version           Version provided by the fabric loader.
     * @param versionPredicates Semantic versioning expressions.
     * @return True if the Fabric Loader finds a matching mods from the list of
     * loaded mods, false otherwise.
     */
    public static boolean isModLoaded(Version version, String versionPredicates) {
        try {
            if (legacyVersionPredicateParser != null) {
                return (boolean) legacyVersionPredicateParser.invoke(null, version, versionPredicates);
            }
            return VersionPredicateParser.parse(versionPredicates).test(version);
        } catch (VersionParsingException | InvocationTargetException | IllegalAccessException e) {
            MagicLib.getLogger().error(e);
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
     * @param modId Version provided by the fabric loader.
     * @return True if the Fabric Loader finds a matching mod from the list of
     * loaded mods, false otherwise.
     */
    public static boolean isModLoaded(String modId, String versionPredicates) {
        Optional<ModContainer> modContainerOptional = FabricLoader.getInstance().getModContainer(modId);
        if (modContainerOptional.isPresent()) {
            ModContainer modContainer = modContainerOptional.get();
            return isModLoaded(modContainer.getMetadata().getVersion(), versionPredicates);
        }
        return false;
    }

    /**
     * Interrupted only if the FabricLoader loads a mod version that does not
     * match the rules in the list.
     *
     * @param currentModId   Your Mod Identifier.
     */
    public static void customValidator(String currentModId) {
        Optional<ModContainer> currentModContainer = fabricLoader.getModContainer(currentModId);
        if (currentModContainer.isPresent()) {
            String exceptionString = "";
            for (Map.Entry<String, CustomValue> customValue : currentModContainer.get().getMetadata().getCustomValue("compat").getAsObject()) {
                if (FabricLoader.getInstance().isModLoaded(customValue.getKey()) && !isModLoaded(customValue.getKey(), customValue.getValue().getAsString())) {
                    if (fabricLoader.getModContainer(customValue.getKey()).isPresent()) {
                        ModMetadata metadata = fabricLoader.getModContainer(customValue.getKey()).get().getMetadata();
                        String targetModId = metadata.getId();
                        String targetModName = metadata.getName();
                        String targetModVersion = metadata.getVersion().getFriendlyString();
                        exceptionString = String.format("%s\n\tMod %s (%s) detected. Requires [%s], but found %s!", exceptionString, targetModName, targetModId, customValue.getValue().getAsString(), targetModVersion);
                    }
                }
            }
            if (!exceptionString.contentEquals("")) {
                displayCriticalError(new IllegalStateException(String.format("Mod resolution encountered an incompatible mod set!%s", exceptionString)));
            }
        }
    }

    /**
     * Exposing the Fabric loader graphical exception display window method.
     * @param exception Thrown exceptions.
     */
    public static void displayCriticalError(Throwable exception) {
        if (legacyDisplayCriticalError != null) {
            try {
                legacyDisplayCriticalError.invoke(null, exception, true);
            } catch (IllegalAccessException | InvocationTargetException e) {
                MagicLib.getLogger().error(e);
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
