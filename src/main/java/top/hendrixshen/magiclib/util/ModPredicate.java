package top.hendrixshen.magiclib.util;

import com.google.common.base.Joiner;
import top.hendrixshen.magiclib.api.dependencyValidator.annotation.Dependency;

import java.util.Arrays;
import java.util.List;

public class ModPredicate {
    public final String modId;
    public final List<String> versionPredicates;
    private final boolean loaded;

    private ModPredicate(String modId, List<String> versionPredicates) {
        this.modId = modId;
        this.versionPredicates = versionPredicates;
        this.loaded = FabricUtil.isModLoaded(this.modId) && FabricUtil.isModsLoaded(this.modId, this.versionPredicates);
    }

    public static ModPredicate of(Dependency dependency) {
        return new ModPredicate(dependency.value(), Arrays.asList(dependency.versionPredicates()));
    }

    public boolean isModLoaded() {
        return this.loaded;
    }

    public String getVersionPredicatesString() {
        return this.versionPredicates.isEmpty() ? "" : " " + Joiner.on(" || ").join(this.versionPredicates);
    }

    @Override
    public String toString() {
        return String.format("%s - %s", this.modId, this.getVersionPredicatesString());
    }
}
