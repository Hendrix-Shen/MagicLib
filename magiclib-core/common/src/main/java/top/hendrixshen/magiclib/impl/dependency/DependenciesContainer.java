package top.hendrixshen.magiclib.impl.dependency;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.tree.AnnotationNode;
import org.spongepowered.asm.util.Annotations;
import top.hendrixshen.magiclib.api.dependency.annotation.Dependencies;
import top.hendrixshen.magiclib.api.dependency.annotation.Dependency;
import top.hendrixshen.magiclib.util.collect.ValueContainer;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DependenciesContainer<T> {
    private final List<DependencyContainer<T>> requireDependencies;
    private final List<DependencyContainer<T>> conflictDependencies;

    private DependenciesContainer(List<DependencyContainer<T>> requireDependencies,
                                  List<DependencyContainer<T>> conflictDependencies) {
        this.requireDependencies = requireDependencies;
        this.conflictDependencies = conflictDependencies;
    }

    @Contract("_, _ -> new")
    public static <T> @NotNull DependenciesContainer<T> of(@NotNull Dependencies dependencies, T obj) {
        return new DependenciesContainer<>(
                DependenciesContainer.generateDependencyList(dependencies.require(), obj),
                DependenciesContainer.generateDependencyList(dependencies.conflict(), obj)
        );
    }

    public static <T> @NotNull DependenciesContainer<T> of(AnnotationNode annotationNode, T obj) {
        List<AnnotationNode> requireNode = Annotations.getValue(annotationNode, "require", true);
        List<AnnotationNode> conflictNode = Annotations.getValue(annotationNode, "conflict", true);
        return new DependenciesContainer<>(
                requireNode
                        .stream()
                        .map(node -> DependencyContainer.of(node, obj))
                        .collect(Collectors.toList()),
                conflictNode
                        .stream()
                        .map(node -> DependencyContainer.of(node, obj))
                        .collect(Collectors.toList())
        );
    }

    public List<DependencyCheckResult> checkRequire() {
        return this.requireDependencies.stream()
                .map(DependencyContainer::checkAsRequire)
                .filter(ValueContainer::isPresent)
                .map(ValueContainer::get)
                .collect(Collectors.toList());
    }

    public List<DependencyCheckResult> checkConflict() {
        return this.conflictDependencies.stream()
                .map(DependencyContainer::checkAsConflict)
                .filter(ValueContainer::isPresent)
                .map(ValueContainer::get)
                .collect(Collectors.toList());
    }

    public boolean isSatisfied() {
        return this.isConflictSatisfied() && this.isRequireSatisfied();
    }

    public boolean isRequireSatisfied() {
        return this.requireDependencies.stream()
                .allMatch(dependency -> dependency.isSatisfied(DependencyContainer.DependencyStyle.REQUIRE));
    }

    public boolean isConflictSatisfied() {
        return this.conflictDependencies.stream()
                .allMatch(dependency -> dependency.isSatisfied(DependencyContainer.DependencyStyle.CONFLICT));
    }

    private static <T> List<DependencyContainer<T>> generateDependencyList(Dependency[] dependencies, T type) {
        return Arrays.stream(dependencies)
                .map(dependency -> DependencyContainer.of(dependency, type))
                .collect(Collectors.toList());
    }
}
