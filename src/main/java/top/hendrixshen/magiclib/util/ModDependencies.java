package top.hendrixshen.magiclib.util;

import top.hendrixshen.magiclib.api.dependencyValidator.annotation.Dependencies;
import top.hendrixshen.magiclib.api.dependencyValidator.annotation.Dependency;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ModDependencies {
    public final List<ModPredicate> requirements;
    public final List<ModPredicate> conflicts;
    public final boolean requirementsSatisfied;
    public final boolean noConflicts;
    public final boolean satisfied;

    private ModDependencies(Dependencies dependencies, Object obj) {
        this.requirements = generateRequirement(dependencies.require(), obj);
        this.conflicts = generateRequirement(dependencies.conflict(), obj);
        this.requirementsSatisfied = this.requirements.isEmpty() || this.requirements.stream().allMatch(modPredicate -> modPredicate.satisfied);
        this.noConflicts = this.conflicts.isEmpty() || this.conflicts.stream().noneMatch(modPredicate -> modPredicate.satisfied);
        this.satisfied = this.requirementsSatisfied && this.noConflicts;
    }

    public static ModDependencies of(Dependencies dependencies, @Nullable Object obj) {
        return new ModDependencies(dependencies, obj);
    }

    private static List<ModPredicate> generateRequirement(Dependency[] dependencies, @Nullable Object obj) {
        return Arrays.stream(dependencies).map(dependency -> ModPredicate.of(dependency, obj)).collect(Collectors.toList());
    }

}
