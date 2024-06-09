package top.hendrixshen.magiclib.util;

import com.google.common.collect.Lists;
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
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DependencyUtil {
    public static @NotNull List<AnnotationNode> parseDependencies(ClassNode classNode) {
        AnnotationNode compositeNode = Annotations.getVisible(classNode, CompositeDependencies.class);

        if (compositeNode == null) {
            AnnotationNode dependenciesNode = Annotations.getVisible(classNode, Dependencies.class);

            if (dependenciesNode != null) {
                return Lists.newArrayList(dependenciesNode);
            }
        } else {
            return Annotations.getValue(compositeNode, "value", true);
        }

        return Collections.emptyList();
    }

    public static @NotNull List<AnnotationNode> parseDependencies(MethodNode methodNode) {
        AnnotationNode compositeNode = Annotations.getVisible(methodNode, CompositeDependencies.class);

        if (compositeNode == null) {
            AnnotationNode dependenciesNode = Annotations.getVisible(methodNode, Dependencies.class);

            if (dependenciesNode != null) {
                return Lists.newArrayList(dependenciesNode);
            }
        } else {
            return Annotations.getValue(compositeNode, "value", true);
        }

        return Collections.emptyList();
    }

    public static @NotNull List<AnnotationNode> parseDependencies(FieldNode fieldNode) {
        AnnotationNode compositeNode = Annotations.getVisible(fieldNode, CompositeDependencies.class);

        if (compositeNode == null) {
            AnnotationNode dependenciesNode = Annotations.getVisible(fieldNode, Dependencies.class);

            if (dependenciesNode != null) {
                return Lists.newArrayList(dependenciesNode);
            }
        } else {
            return Annotations.getValue(compositeNode, "value", true);
        }

        return Collections.emptyList();
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
