package top.hendrixshen.magiclib.untils.fabricloader;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.VersionParsingException;
import net.fabricmc.loader.api.metadata.CustomValue;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.fabricmc.loader.impl.util.version.VersionPredicateParser;
import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.untils.dependency.Dependency;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;

/**
 * To verify that the current runtime environment has the required mos loaded.
 */
@SuppressWarnings("unused")
public class DependencyValidator {
    // Fabric Loader 0.11 and below support
    private static Method legacyVersionPredicateParser;

    private final static FabricLoader fabricLoader = FabricLoader.getInstance();

    static {
        try {
            legacyVersionPredicateParser = Class.forName("net.fabricmc.loader.util.version.VersionPredicateParser").getMethod("matches", Version.class, String.class);
        } catch (ClassNotFoundException | NoSuchMethodException ignored) {
            // Ignored
        }
    }

    /**
     * Verify that the Fabric Loader has loaded the qualified mods.
     *
     * @param version     Version provided by the fabric loader.
     * @param versionExpr Semantic versioning expressions.
     * @return True if the Fabric Loader finds a matching mods from the list of
     * loaded mods, false otherwise.
     */
    public static boolean isModLoaded(Version version, String versionExpr) {
        try {
            if (legacyVersionPredicateParser != null) {
                return (boolean) legacyVersionPredicateParser.invoke(null, version, versionExpr);
            }
            return VersionPredicateParser.parse(versionExpr).test(version);
        } catch (VersionParsingException | InvocationTargetException | IllegalAccessException e) {
            MagicLib.getLogger().error(e);
            return false;
        }
    }

    /**
     * Verify that the Fabric Loader has loaded the qualified mods.
     *
     * @param modId       Mod Identifier.
     * @param versionExpr Semantic versioning expressions.
     * @param type        Dependency type.
     * @return True if the Fabric Loader finds a matching mods from the list of
     * loaded mods, false otherwise.
     */
    public static boolean isModLoaded(String modId, String versionExpr, Dependency.DependencyType type) {
        return isModLoaded(modId, versionExpr) && type != Dependency.DependencyType.CONFLICT;
    }

    /**
     * Verify that the Fabric Loader has loaded the qualified mods.
     *
     * @param modId       Mod Identifier.
     * @param versionExpr Semantic versioning expressions.
     * @return True if the Fabric Loader finds a matching mods from the list of
     * loaded mods, false otherwise.
     */
    public static boolean isModLoaded(String modId, String versionExpr) {
        Optional<ModContainer> modContainerOptional = FabricLoader.getInstance().getModContainer(modId);
        if (modContainerOptional.isPresent()) {
            ModContainer modContainer = modContainerOptional.get();
            return DependencyValidator.isModLoaded(modContainer.getMetadata().getVersion(), versionExpr);
        }
        return false;
    }

    /**
     * Interrupted only if the FabricLoader loads a mod version that does not
     * match the rules in the list.
     *
     * @param currentModId   Your Mod Identifier.
     * @param dependencyType Your fabric.mod.json custom key.
     */
    public static void customValidator(String currentModId, String dependencyType) {
        Optional<ModContainer> currentModContainer = fabricLoader.getModContainer(currentModId);
        if (currentModContainer.isPresent()) {
            String exceptionString = "";
            for (Map.Entry<String, CustomValue> customValue : currentModContainer.get().getMetadata().getCustomValue(dependencyType).getAsObject()) {
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
                FabricGui.displayCriticalError(new IllegalStateException(String.format("Mod resolution encountered an incompatible mod set!%s", exceptionString)));
            }
        }
    }
}
