package top.hendrixshen.magiclib.untils.mixin;

import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.untils.dependency.CustomDependencyPredicate;
import top.hendrixshen.magiclib.untils.dependency.Dependencies;
import top.hendrixshen.magiclib.untils.dependency.Dependency;
import top.hendrixshen.magiclib.untils.fabricloader.DependencyValidator;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.service.MixinService;
import org.spongepowered.asm.util.Annotations;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@SuppressWarnings("unused")
public class MagicMixinPlugin extends EmptyMixinPlugin {
    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        ClassNode mixinClassNode = loadClassNode(mixinClassName);
        return checkDependencies(mixinClassNode, targetClassName);
    }

    private static ClassNode loadClassNode(String className) {
        ClassNode classNode;
        try {
            classNode = MixinService.getService().getBytecodeProvider().getClassNode(className);
        } catch (ClassNotFoundException | IOException e) {
            throw new IllegalStateException(String.format("Cant load ClassNode(%s).", className));
        }
        return classNode;
    }

    private static boolean checkDependencies(ClassNode mixinClassNode, String targetClassName) {
        AnnotationNode dependencies = Annotations.getInvisible(mixinClassNode, Dependencies.class);
        if (Annotations.getInvisible(mixinClassNode, Dependencies.class) != null) {
            List<AnnotationNode> dependencyArray = Annotations.getValue(dependencies, "dependencyList");
            for (AnnotationNode dependency : dependencyArray) {
                if (!checkDependency(targetClassName, dependency)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean checkDependency(String targetClassName, AnnotationNode dependency) {
        String modId = Annotations.getValue(dependency, "modid");
        List<String> versionList = Annotations.getValue(dependency, "version");
        Dependency.DependencyType type = Annotations.getValue(dependency, "dependencyType", Dependency.DependencyType.class, Dependency.DependencyType.NORMAL);

        for (String version : versionList) {
            if (!DependencyValidator.isModLoaded(modId, version, type)) {
                return false;
            }
        }

        ClassNode targetClassNode = loadClassNode(targetClassName);
        List<Type> predicateList = Annotations.getValue(dependency, "predicate");
        if (predicateList != null) {
            for (Type predicateType : predicateList) {
                try {
                    CustomDependencyPredicate predicate = Class.forName(predicateType.getClassName()).asSubclass(CustomDependencyPredicate.class).getDeclaredConstructor().newInstance();
                    if (!predicate.test(targetClassNode)) {
                        return false;
                    }
                } catch (ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
                    MagicLib.getLogger().error(e);
                    throw new IllegalStateException("Cant get CustomDependencyPredicate!");
                }
            }
        }
        return true;
    }
}
