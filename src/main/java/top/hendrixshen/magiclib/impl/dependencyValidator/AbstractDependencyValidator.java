package top.hendrixshen.magiclib.impl.dependencyValidator;

import com.google.common.collect.Lists;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.Version;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.service.MixinService;
import org.spongepowered.asm.util.Annotations;
import top.hendrixshen.magiclib.api.dependencyValidator.annotation.Dependencies;
import top.hendrixshen.magiclib.api.dependencyValidator.annotation.MixinDependencyPredicate;
import top.hendrixshen.magiclib.api.dependencyValidator.mixin.DependencyValidateFailureCallback;
import top.hendrixshen.magiclib.api.dependencyValidator.mixin.DependencyValidator;
import top.hendrixshen.magiclib.util.FabricUtil;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class AbstractDependencyValidator implements DependencyValidator {
    private DependencyValidateFailureCallback failureCallback = null;

    private static ClassNode loadClassNode(String className) {
        ClassNode classNode;
        try {
            classNode = MixinService.getService().getBytecodeProvider().getClassNode(className);
        } catch (ClassNotFoundException | IOException e) {
            throw new IllegalStateException(String.format("load ClassNode: %s fail.", className));
        }
        return classNode;
    }

    @Override
    public boolean checkDependency(String targetClassName, String mixinClassName) {
        AnnotationNode validator = getDependencyAnnotation(mixinClassName);
        if (validator != null) {
            List<AnnotationNode> applyDependencies = Annotations.getValue(validator, "require", true);
            for (Result result : this.validateDependencies(applyDependencies)) {
                if (!result.success) {
                    this.onDependencyFailure(mixinClassName, result.reason);
                    return false;
                }
            }
            List<AnnotationNode> revokeDependencies = Annotations.getValue(validator, "conflict", true);
            for (Result result : this.validateDependencies(revokeDependencies)) {
                if (result.success) {
                    this.onDependencyFailure(mixinClassName, result.reason);
                    return false;
                }
            }
            List<Type> customPredicates = Annotations.getValue(validator, "predicate", true);
            for (Result result : this.validateCustomPredicates(targetClassName, customPredicates)) {
                if (!result.success) {
                    this.onDependencyFailure(mixinClassName, result.reason);
                    return false;
                }
            }
        }
        return true;
    }

    private List<Result> validateCustomPredicates(String targetClassName, List<Type> customPredicates) {
        List<Result> results = Lists.newArrayList();
        ClassNode targetClassNode = loadClassNode(targetClassName);
        for (Type predicateType : customPredicates) {
            try {
                MixinDependencyPredicate predicate = Class.forName(predicateType.getClassName()).asSubclass(MixinDependencyPredicate.class).getDeclaredConstructor().newInstance();
                if (!predicate.test(targetClassNode)) {
                    results.add(new Result(false, String.format("Predicate test not passed: %s", predicateType)));
                }
            } catch (ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
                throw new RuntimeException(e);
                //results.add(new Result(false, String.format("Predicate test exception: %s", predicateType)));
            }
        }
        return results;
    }

    private List<Result> validateDependencies(List<AnnotationNode> dependencies) {
        List<Result> results = Lists.newArrayList();
        for (AnnotationNode dependency : dependencies) {
            String modId = Annotations.getValue(dependency, "value");
            Objects.requireNonNull(modId);
            Optional<ModContainer> modContainer = FabricLoader.getInstance().getModContainer(modId);
            if (!modContainer.isPresent()) {
                results.add(new Result(false, String.format("required mod %s not found", modId)));
                continue;
            }
            Version modVersion = modContainer.get().getMetadata().getVersion();
            List<String> versionPredicates = Lists.newArrayList(Annotations.getValue(dependency, "versionPredicates", Lists.newArrayList()));
            if (!FabricUtil.isModsLoaded(modVersion, versionPredicates)) {
                results.add(new Result(false, String.format("mod %s@%s does not matches version predicates %s", modId, modVersion.getFriendlyString(), versionPredicates)));
                continue;
            }
            results.add(new Result(true, String.format("conflicted/unsupported mod %s@%s found", modId, modVersion.getFriendlyString())));
        }
        return results;
    }

    @Override
    public void setFailureCallback(DependencyValidateFailureCallback failureCallback) {
        this.failureCallback = failureCallback;
    }

    @Nullable
    private AnnotationNode getDependencyAnnotation(String className) {
        try {
            ClassNode classNode = MixinService.getService().getBytecodeProvider().getClassNode(className);
            return Annotations.getVisible(classNode, Dependencies.class);
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void onDependencyFailure(String mixinClassName, String reason) {
        if (this.failureCallback != null) {
            this.failureCallback.callback(mixinClassName, reason);
        }
    }

    private static class Result {
        public final boolean success;
        public final String reason;

        private Result(boolean success, String reason) {
            this.success = success;
            this.reason = reason;
        }
    }
}
