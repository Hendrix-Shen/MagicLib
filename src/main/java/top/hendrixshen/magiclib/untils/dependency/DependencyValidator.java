package top.hendrixshen.magiclib.untils.dependency;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.VersionParsingException;
import net.fabricmc.loader.impl.util.version.VersionPredicateParser;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

public class DependencyValidator {
    private static Method oldMatchesMethod;

    static {
        try {
            oldMatchesMethod = Class.forName("net.fabricmc.loader.util.version.VersionPredicateParser").getMethod("matches", Version.class, String.class);
        } catch (ClassNotFoundException | NoSuchMethodException ignored) {
            // Ignored
        }
    }

    public static boolean checkDependency(Version version, String versionExpr) {
        try {
            if (oldMatchesMethod != null) {
                return (boolean) oldMatchesMethod.invoke(null, version, versionExpr);
            }
            return VersionPredicateParser.parse(versionExpr).test(version);
        } catch (VersionParsingException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean checkDependency(String modId, String version, Dependency.DependencyType type) {
        Optional<ModContainer> modContainerOptional = FabricLoader.getInstance().getModContainer(modId);
        if (modContainerOptional.isPresent()) {
            ModContainer modContainer = modContainerOptional.get();
            return DependencyValidator.checkDependency(modContainer.getMetadata().getVersion(), version) && type != Dependency.DependencyType.CONFLICT;
        }
        return false;
    }
}
