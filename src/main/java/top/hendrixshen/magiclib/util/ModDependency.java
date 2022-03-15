package top.hendrixshen.magiclib.util;

import top.hendrixshen.magiclib.api.dependencyValidator.annotation.Dependencies;
import top.hendrixshen.magiclib.api.dependencyValidator.annotation.Dependency;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ModDependency {
    private final List<ModPredicate> requirements;
    private final List<ModPredicate> conflicts;
    private final boolean requirementsSatisfied;
    private final boolean noConflicts;

    private ModDependency(Dependencies dependencies, Predicate<Dependency> dependencyPredicate) {
        this.requirements = generateRequirement(dependencies.require(), dependencyPredicate);
        this.conflicts = generateRequirement(dependencies.conflict(), dependencyPredicate);
        this.requirementsSatisfied = this.requirements.stream().allMatch(ModPredicate::isModLoaded);
        this.noConflicts = this.conflicts.stream().noneMatch(ModPredicate::isModLoaded);
    }

    public static ModDependency of(Dependencies dependencies, Predicate<Dependency> dependencyPredicate) {
        return new ModDependency(dependencies, dependencyPredicate);
    }

    public static ModDependency of(Dependencies dependencies) {
        return new ModDependency(dependencies, c -> true);
    }

    private static List<ModPredicate> generateRequirement(Dependency[] dependencies, Predicate<Dependency> dependencyPredicate) {
        return Arrays.stream(dependencies).
                filter(dependencyPredicate).
                map(ModPredicate::of).
                collect(Collectors.toList());
    }

    public boolean isRequirementsSatisfied() {
        return this.requirementsSatisfied;
    }

    public boolean isNoConflicts() {
        return this.noConflicts;
    }

    public boolean isSatisfied() {
        return this.isRequirementsSatisfied() && this.isNoConflicts();
    }

    public List<ModPredicate> getRequirements() {
        return this.requirements;
    }

    public List<ModPredicate> getConflicts() {
        return this.conflicts;
    }
}
