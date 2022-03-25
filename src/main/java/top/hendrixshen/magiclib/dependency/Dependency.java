package top.hendrixshen.magiclib.dependency;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;
import org.objectweb.asm.tree.AnnotationNode;
import org.spongepowered.asm.util.Annotations;
import top.hendrixshen.magiclib.util.FabricUtil;

public class Dependency {
    public static final String SATISFIED = "Satisfied!";
    public final String modId;
    public final String versionPredicate;
    public final boolean optional;
    public final boolean satisfied;

    private Dependency(String modId, String versionPredicate, boolean optional) {
        this.modId = modId;
        this.versionPredicate = versionPredicate;
        this.optional = optional;
        this.satisfied = optional ?
                !FabricUtil.isModLoaded(modId) || FabricUtil.isModLoaded(modId, versionPredicate) :
                FabricUtil.isModLoaded(modId, versionPredicate);
    }

    public static Dependency of(top.hendrixshen.magiclib.dependency.annotation.Dependency dependency) {
        return new Dependency(dependency.value(), dependency.versionPredicate(), dependency.optional());
    }

    public static Dependency of(AnnotationNode dependencyNode) {
        return new Dependency(Annotations.getValue(dependencyNode, "value", top.hendrixshen.magiclib.dependency.annotation.Dependency.class),
                Annotations.getValue(dependencyNode, "versionPredicate", top.hendrixshen.magiclib.dependency.annotation.Dependency.class),
                Annotations.getValue(dependencyNode, "optional", top.hendrixshen.magiclib.dependency.annotation.Dependency.class)
        );
    }

    @Override
    public String toString() {
        return String.format("Dependency{modId: %s, versionPredicate: %s, optional: %s, satisfied: %s}", this.modId, this.versionPredicate, this.optional, this.satisfied);
    }

    public String getCheckResult() {
        if (!this.satisfied) {
            if (FabricUtil.isModLoaded(this.modId)) {
                return FabricLoader.getInstance().getModContainer(this.modId).map(modContainer -> {
                    ModMetadata metadata = modContainer.getMetadata();
                    String modName = metadata.getName();
                    String modVersion = metadata.getVersion().getFriendlyString();
                    return String.format("Mod %s (%s) detected. Requires [%s], but found %s!", modName, this.modId, this.versionPredicate, modVersion);
                }).orElse(String.format("Get %s data failed!", this.modId));
            } else {
                return String.format("Mod %s not found. Requires [%s]!", this.modId, this.versionPredicate);

            }
        } else {
            return SATISFIED;
        }
    }
}
