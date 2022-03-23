package top.hendrixshen.magiclib.dependency;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.service.MixinService;
import org.spongepowered.asm.util.Annotations;
import top.hendrixshen.magiclib.dependency.mixin.DepCheckFailureCallback;

import javax.annotation.Nullable;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Dependencies<T> {
    public final List<Dependency> andRequirements;
    public final List<Dependency> conflicts;
    public final List<Dependency> orRequirements;

    public final boolean andRequirementsSatisfied;
    public final boolean orRequirementsSatisfied;
    public final boolean noConflicts;
    public Predicate<?> predicate;

    private Dependencies(List<Dependency> andRequirements, List<Dependency> conflicts, List<Dependency> orRequirements, Predicate<?> predicate) {
        this.andRequirements = andRequirements;
        this.orRequirements = orRequirements;
        this.conflicts = conflicts;
        this.andRequirementsSatisfied = this.andRequirements.isEmpty() || this.andRequirements.stream().allMatch(modPredicate -> modPredicate.satisfied);
        this.orRequirementsSatisfied = this.orRequirements.isEmpty() || this.andRequirements.stream().allMatch(modPredicate -> modPredicate.satisfied);
        this.noConflicts = this.conflicts.isEmpty() || this.conflicts.stream().noneMatch(modPredicate -> modPredicate.satisfied);
        this.predicate = predicate;
    }

    private Dependencies(top.hendrixshen.magiclib.dependency.annotation.Dependencies dependencies, Class<T> clazz) {
        this(generateRequirement(dependencies.and()), generateRequirement(dependencies.or()), generateRequirement(dependencies.not()),
                new top.hendrixshen.magiclib.dependency.annotation.Dependencies.DefaultPredicate());
        try {
            this.predicate = dependencies.predicate().getDeclaredConstructor().newInstance();
            if (!(this.predicate instanceof top.hendrixshen.magiclib.dependency.annotation.Dependencies.DefaultPredicate)) {
                // 确保类型正确
                this.predicate.getClass().getMethod("test", clazz);
            }
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    @SuppressWarnings("unchecked")
    private static <T> boolean testHelper(Predicate<T> l, Object obj) {
        return l.test((T) obj);
    }

    public static <T> Dependencies<T> of(top.hendrixshen.magiclib.dependency.annotation.Dependencies dependencies, Class<T> clazz) {
        return new Dependencies<>(dependencies, clazz);
    }

//    public DepCheckException satisfiedException(@Nullable T obj) {
//        return this.andRequirementsSatisfied && this.orRequirementsSatisfied && this.noConflicts && testHelper(this.predicate, obj);
//    }

    public static <T> Dependencies<T> of(String mixinClassName, Class<T> clazz) {
        @Nullable
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
                // 确保类型正确
                predicate.getClass().getMethod("test", clazz);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
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
        ClassNode targetClassNode;
        try {
            targetClassNode = loadClassNode(targetClassName);
        } catch (IOException | ClassNotFoundException e) {
            DepCheckException depCheckException = new DepCheckException(e);
            depCheckFailureCallback.callback(targetClassName, mixinClassName, depCheckException);
            throw depCheckException;
        }
        Dependencies<ClassNode> dependencies = of(mixinClassName, ClassNode.class);
        if (dependencies == null) {
            return true;
        }
        boolean ret = dependencies.satisfied(targetClassNode);
        if (!ret) {
            depCheckFailureCallback.callback(targetClassName, mixinClassName, new DepCheckException(""));
            return false;
        }
        return true;

    }

    public boolean satisfied(@Nullable T obj) {
        return this.andRequirementsSatisfied && this.orRequirementsSatisfied && this.noConflicts && testHelper(this.predicate, obj);
    }


}
