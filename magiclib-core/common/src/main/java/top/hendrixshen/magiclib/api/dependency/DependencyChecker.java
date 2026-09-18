package top.hendrixshen.magiclib.api.dependency;

import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import top.hendrixshen.magiclib.impl.dependency.DependenciesContainer;
import top.hendrixshen.magiclib.impl.dependency.DependencyCheckResult;
import top.hendrixshen.magiclib.util.DependencyUtil;
import top.hendrixshen.magiclib.util.MiscUtil;
import top.hendrixshen.magiclib.util.collect.InfoNode;

import java.lang.reflect.Field;
import java.util.List;

/**
 * The runtime facade of the dependency check system.
 *
 * <p>
 * It checks the {@code @Dependencies} / {@code @CompositeDependencies} annotations declared on a class, method
 * or field at runtime. The annotated element is satisfied when any of its dependency groups passes (logical or
 * across the groups, logical and inside a group), which is the same rule used by the mixin and entry point
 * checkers.
 * </p>
 */
public class DependencyChecker {
    /**
     * Checks the dependencies declared on the given annotated class.
     *
     * @param annotatedClass The class to check.
     * @param obj The object passed to {@code PREDICATE} type dependencies, or {@code null}.
     * @param <T> The type of the checked object.
     * @return The check result.
     */
    public static <T> DependencyCheckResult check(ClassNode annotatedClass, T obj) {
        return DependencyChecker.check(DependencyUtil.parseDependencies(annotatedClass, obj));
    }

    /**
     * Checks the dependencies declared on the given annotated method.
     *
     * @param annotatedMethod The method to check.
     * @param obj The object passed to {@code PREDICATE} type dependencies, or {@code null}.
     * @param <T> The type of the checked object.
     * @return The check result.
     */
    public static <T> DependencyCheckResult check(MethodNode annotatedMethod, T obj) {
        return DependencyChecker.check(DependencyUtil.parseDependencies(annotatedMethod, obj));
    }

    /**
     * Checks the dependencies declared on the given annotated field.
     *
     * @param annotatedField The field to check.
     * @param obj The object passed to {@code PREDICATE} type dependencies, or {@code null}.
     * @param <T> The type of the checked object.
     * @return The check result.
     */
    public static <T> DependencyCheckResult check(FieldNode annotatedField, T obj) {
        return DependencyChecker.check(DependencyUtil.parseDependencies(annotatedField, obj));
    }

    /**
     * Checks the dependencies declared on the given annotated field.
     *
     * @param annotatedField The field to check.
     * @param obj The object passed to {@code PREDICATE} type dependencies, or {@code null}.
     * @param <T> The type of the checked object.
     * @return The check result.
     */
    public static <T> DependencyCheckResult check(Field annotatedField, T obj) {
        return DependencyChecker.check(DependencyUtil.parseDependencies(annotatedField, obj));
    }

    private static <T> DependencyCheckResult check(@NotNull List<DependenciesContainer<T>> dependencies) {
        if (dependencies.isEmpty() || dependencies.stream().anyMatch(DependenciesContainer::isSatisfied)) {
            return new DependencyCheckResult(true, null);
        }

        InfoNode rootNode = new InfoNode(null, "");
        MiscUtil.generateDependencyCheckMessage(dependencies, rootNode);
        return new DependencyCheckResult(false, rootNode.toString());
    }
}
