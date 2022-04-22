package top.hendrixshen.magiclib.dependency;

import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.service.MixinService;
import org.spongepowered.asm.util.Annotations;
import top.hendrixshen.magiclib.MagicLibReference;
import top.hendrixshen.magiclib.dependency.mixin.DepCheckFailureCallback;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Dependencies<T> {
    public static final String SATISFIED = "Satisfied!";
    public final List<Dependency> andRequirements;
    public final List<Dependency> conflicts;
    public final List<Dependency> orRequirements;
    public final boolean andRequirementsSatisfied;
    public final boolean orRequirementsSatisfied;
    public final boolean noConflicts;
    public Predicate<?> predicate;

    private Dependencies(List<Dependency> andRequirements, List<Dependency> orRequirements, List<Dependency> conflicts, Predicate<?> predicate) {
        this.andRequirements = andRequirements;
        this.orRequirements = orRequirements;
        this.conflicts = conflicts;
        this.andRequirementsSatisfied = this.andRequirements.isEmpty() || this.andRequirements.stream().allMatch(modPredicate -> modPredicate.satisfied);
        this.orRequirementsSatisfied = this.orRequirements.isEmpty() || this.orRequirements.stream().anyMatch(modPredicate -> modPredicate.satisfied);
        this.noConflicts = this.conflicts.isEmpty() || this.conflicts.stream().noneMatch(modPredicate -> modPredicate.satisfied);
        this.predicate = predicate;
    }

    private Dependencies(top.hendrixshen.magiclib.dependency.annotation.Dependencies dependencies, Class<T> clazz) {
        this(generateRequirement(dependencies.and()), generateRequirement(dependencies.or()), generateRequirement(dependencies.not()),
                new top.hendrixshen.magiclib.dependency.annotation.Dependencies.DefaultPredicate());
        try {
            this.predicate = dependencies.predicate().getDeclaredConstructor().newInstance();
            // Ensure that the input type is correct.
            if (!(this.predicate instanceof top.hendrixshen.magiclib.dependency.annotation.Dependencies.DefaultPredicate)) {
                this.predicate.getClass().getMethod("test", clazz);
            }
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            MagicLibReference.LOGGER.error(e);
        }
    }


    @SuppressWarnings("unchecked")
    private static <T> boolean testHelper(Predicate<T> l, Object obj) {
        return l.test((T) obj);
    }

    public static <T> Dependencies<T> of(top.hendrixshen.magiclib.dependency.annotation.Dependencies dependencies, Class<T> clazz) {
        return new Dependencies<>(dependencies, clazz);
    }


    @Nullable
    public static Dependencies<Object> getFabricEntrypointDependencies(String entrypointClassName, String methodName) {
        @Nullable
        AnnotationNode dependencyAnnotation = null;
        try {
            ClassNode classNode = MixinService.getService().getBytecodeProvider().getClassNode(entrypointClassName);
            for (MethodNode methodNode : classNode.methods) {
                if (methodNode.name.equals(methodName) && methodNode.desc.equals("()V")) {
                    dependencyAnnotation = Annotations.getVisible(methodNode, top.hendrixshen.magiclib.dependency.annotation.Dependencies.class);
                    break;
                }
            }
        } catch (ClassNotFoundException | IOException e) {
            return null;
        }
        if (dependencyAnnotation == null) {
            return null;
        }
        return of(dependencyAnnotation, Object.class);
    }

    @Nullable
    public static Dependencies<ClassNode> getMixinClassDependencies(String mixinClassName) {
        AnnotationNode dependencyAnnotation;
        try {
            ClassNode classNode = MixinService.getService().getBytecodeProvider().getClassNode(mixinClassName);
            dependencyAnnotation = Annotations.getVisible(classNode, top.hendrixshen.magiclib.dependency.annotation.Dependencies.class);
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
        if (dependencyAnnotation == null) {
            return null;
        }
        return of(dependencyAnnotation, ClassNode.class);
    }

    public static <T> Dependencies<T> of(AnnotationNode dependencyAnnotation, Class<T> clazz) {

        List<Dependency> andDependencies = getDependencyList(dependencyAnnotation, "and");
        List<Dependency> orDependencies = getDependencyList(dependencyAnnotation, "or");
        List<Dependency> notDependencies = getDependencyList(dependencyAnnotation, "not");

        Type predicateType = Annotations.getValue(dependencyAnnotation, "predicate");
        Predicate<?> predicate;
        if (predicateType == null) {
            predicate = new top.hendrixshen.magiclib.dependency.annotation.Dependencies.DefaultPredicate();
        } else {
            try {
                predicate = (Predicate<?>) Class.forName(predicateType.getClassName()).getDeclaredConstructor().newInstance();
                // Ensure that the input type is correct.
                predicate.getClass().getMethod("test", clazz);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }


        return new Dependencies<>(andDependencies, orDependencies, notDependencies, predicate);

    }

    private static List<Dependency> generateRequirement(top.hendrixshen.magiclib.dependency.annotation.Dependency[] dependencies) {
        return Arrays.stream(dependencies).map(Dependency::of).collect(Collectors.toList());
    }

    private static ClassNode loadClassNode(String className) throws IOException, ClassNotFoundException {
        return MixinService.getService().getBytecodeProvider().getClassNode(className);
    }

    private static List<Dependency> getDependencyList(AnnotationNode annotationNode, String key) {
        ArrayList<Dependency> ret = new ArrayList<>();
        List<AnnotationNode> dependenciesNode = Annotations.getValue(annotationNode, key, true);
        for (AnnotationNode dependencyNode : dependenciesNode) {
            ret.add(Dependency.of(dependencyNode));
        }
        return ret;
    }

    public static boolean checkDependency(String targetClassName, String mixinClassName, DepCheckFailureCallback depCheckFailureCallback) {
        ClassNode targetClassNode = null;
        try {
            targetClassNode = loadClassNode(targetClassName);
        } catch (IOException | ClassNotFoundException ignored) {
        }
        Dependencies<ClassNode> dependencies = getMixinClassDependencies(mixinClassName);
        if (dependencies == null) {
            return true;
        }
        String result = dependencies.getCheckResult(targetClassNode);
        if (!result.equals(Dependencies.SATISFIED)) {
            depCheckFailureCallback.callback(targetClassName, mixinClassName, new DepCheckException(result));
            return false;
        }
        return true;

    }

    public String getCheckResult(@Nullable T obj) {
        StringBuilder result = new StringBuilder();
        if (!this.andRequirementsSatisfied) {
            result.append("Requirements:");
            this.andRequirements.forEach(dependency -> result.append(dependency.satisfied ? "" : "\n\t" + dependency.getCheckResult()));
        }
        if (!this.orRequirementsSatisfied) {
            if (result.length() != 0) {
                result.append("\n");
            }
            result.append("Optional Requirements:");
            this.orRequirements.forEach(dependency -> result.append(dependency.satisfied ? "" : "\n\t" + dependency.getCheckResult()));
        }

        if (!this.noConflicts) {
            if (result.length() != 0) {
                result.append("\n");
            }
            result.append("Conflicts:");
            this.conflicts.forEach(dependency -> result.append(dependency.satisfied ? String.format("\n\tMod %s [%s] detected.", dependency.modId, dependency.versionPredicate) : ""));
        }
        if (!testHelper(this.predicate, obj)) {
            if (result.length() != 0) {
                result.append("\n");
            }
            result.append(String.format("Predicate %s check {%s} failed.", this.predicate, obj));
        }
        String ret = result.toString();
        if (ret.isEmpty()) {
            ret = SATISFIED;
        }
        return ret;
    }

    public boolean satisfied(@Nullable T obj) {
        return this.andRequirementsSatisfied && this.orRequirementsSatisfied && this.noConflicts && testHelper(this.predicate, obj);
    }
}
