package top.hendrixshen.magiclib.untils.FabricLoader;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.VersionParsingException;
import net.fabricmc.loader.impl.util.version.VersionPredicateParser;
import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.untils.dependency.Dependency;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 * To verify that the current runtime environment has the required mos loaded.
 */
public class DependencyValidator {
    // Fabric Loader 0.11 and below support
    private static Method legacyVersionPredicateParser;

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
     * @param version Version provided by the fabric loader.
     * @param versionExpr Semantic versioning expressions.
     *
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
     * @param modId Mod Identifier.
     * @param versionExpr Semantic versioning expressions.
     * @param type Dependency type.
     *
     * @return True if the Fabric Loader finds a matching mods from the list of
     * loaded mods, false otherwise.
     */
    public static boolean isModLoaded(String modId, String versionExpr, Dependency.DependencyType type) {
        Optional<ModContainer> modContainerOptional = FabricLoader.getInstance().getModContainer(modId);
        if (modContainerOptional.isPresent()) {
            ModContainer modContainer = modContainerOptional.get();
            return DependencyValidator.isModLoaded(modContainer.getMetadata().getVersion(), versionExpr) && type != Dependency.DependencyType.CONFLICT;
        }
        return false;
    }
}
