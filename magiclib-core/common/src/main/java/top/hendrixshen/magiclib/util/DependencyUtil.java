package top.hendrixshen.magiclib.util;

import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.util.Annotations;
import top.hendrixshen.magiclib.api.dependency.annotation.CompositeDependencies;
import top.hendrixshen.magiclib.api.dependency.annotation.Dependencies;
import top.hendrixshen.magiclib.impl.dependency.DependenciesContainer;
import top.hendrixshen.magiclib.util.collect.ValueContainer;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DependencyUtil {
    public static <T> List<DependenciesContainer<T>> parseDependencies(ClassNode classNode, T instance) {
        List<DependenciesContainer<T>> composite = DependencyUtil.convertCompositeDependencies(ValueContainer
                .ofNullable(Annotations.getVisible(classNode, CompositeDependencies.class)), instance);

        if (!composite.isEmpty()) {
            return composite;
        }

        return DependencyUtil.convertDependencies(ValueContainer.ofNullable(Annotations.getVisible(classNode,
                Dependencies.class)), instance);
    }

    public static @NotNull <T> List<DependenciesContainer<T>> parseDependencies(MethodNode methodNode, T instance) {
        List<DependenciesContainer<T>> composite = DependencyUtil.convertCompositeDependencies(ValueContainer
                .ofNullable(Annotations.getVisible(methodNode, CompositeDependencies.class)), instance);

        if (!composite.isEmpty()) {
            return composite;
        }

        return DependencyUtil.convertDependencies(ValueContainer.ofNullable(Annotations.getVisible(methodNode,
                Dependencies.class)), instance);
    }

    public static @NotNull <T> List<DependenciesContainer<T>> parseDependencies(FieldNode fieldNode, T instance) {
        List<DependenciesContainer<T>> composite = DependencyUtil.convertCompositeDependencies(ValueContainer
                .ofNullable(Annotations.getVisible(fieldNode, CompositeDependencies.class)), instance);

        if (!composite.isEmpty()) {
            return composite;
        }

        return DependencyUtil.convertDependencies(ValueContainer.ofNullable(Annotations.getVisible(fieldNode,
                Dependencies.class)), instance);
    }

    private static <T> List<DependenciesContainer<T>> convertCompositeDependencies(
            @NotNull ValueContainer<AnnotationNode> composite, T instance) {
        return composite.map(compositeNode -> Annotations.getValue(compositeNode, "value", true))
                .orElse(Collections.emptyList())
                .stream()
                .map(dependenciesNode -> DependenciesContainer.of((AnnotationNode) dependenciesNode, instance))
                .collect(Collectors.toList());
    }

    private static <T> List<DependenciesContainer<T>> convertDependencies(
            @NotNull ValueContainer<AnnotationNode> dependencies, T instance) {
        return dependencies.stream()
                .map(d -> DependenciesContainer.of(d, instance))
                .collect(Collectors.toList());
    }

    public static <T> @NotNull List<DependenciesContainer<T>> parseDependencies(@NotNull Field field, T instance) {
        List<DependenciesContainer<T>> composite = ReflectionUtil.getFieldAnnotation(field, CompositeDependencies.class)
                .map(CompositeDependencies::value)
                .map(Arrays::stream)
                .orElse(Stream.empty())
                .map(dependencies -> DependenciesContainer.of(dependencies, instance))
                .collect(Collectors.toList());

        if (!composite.isEmpty()) {
            return composite;
        }

        return ReflectionUtil.getFieldAnnotation(field, Dependencies.class)
                .stream()
                .map(dependencies -> DependenciesContainer.of(dependencies, instance))
                .collect(Collectors.toList());
    }
}
