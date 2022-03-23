package top.hendrixshen.magiclib.dependency;

import org.objectweb.asm.tree.AnnotationNode;
import org.spongepowered.asm.util.Annotations;
import top.hendrixshen.magiclib.util.FabricUtil;

public class Dependency {
    public final String modId;
    public final String versionPredicate;
    public final boolean satisfied;

    private Dependency(String modId, String versionPredicate) {
        this.modId = modId;
        this.versionPredicate = versionPredicate;
        this.satisfied = FabricUtil.isModLoaded(modId, versionPredicate);
    }

    public static Dependency of(top.hendrixshen.magiclib.dependency.annotation.Dependency dependency) {
        String modId = dependency.value();
        String versionPredicate = dependency.versionPredicate();
        return new Dependency(modId, versionPredicate);
    }

    public static Dependency of(AnnotationNode dependencyNode) {
        return new Dependency(Annotations.getValue(dependencyNode, "value", top.hendrixshen.magiclib.dependency.annotation.Dependency.class),
                Annotations.getValue(dependencyNode, "versionPredicate", top.hendrixshen.magiclib.dependency.annotation.Dependency.class));
    }

    @Override
    public String toString() {
        return String.format("Dependency{modId: %s, versionPredicate: %s,satisfied: %s}", this.modId, this.versionPredicate, this.satisfied);
    }
}
