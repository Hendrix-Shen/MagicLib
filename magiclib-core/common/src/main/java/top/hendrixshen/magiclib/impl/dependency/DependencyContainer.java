package top.hendrixshen.magiclib.impl.dependency;

import com.google.common.collect.Lists;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.spongepowered.asm.util.Annotations;
import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.api.dependency.*;
import top.hendrixshen.magiclib.api.dependency.annotation.Dependency;
import top.hendrixshen.magiclib.api.platform.IPlatform;
import top.hendrixshen.magiclib.api.dependency.SimplePredicate;
import top.hendrixshen.magiclib.util.VersionUtil;
import top.hendrixshen.magiclib.util.collect.ValueContainer;

import java.util.List;

@Getter
public class DependencyContainer<T> {
    private final String value;
    private final DependencyType dependencyType;
    private final DistType distType;
    private final List<String> versionPredicates;
    @Nullable
    private final SimplePredicate<T> predicate;
    private final boolean optional;
    private final T obj;

    private DependencyContainer(String value, DependencyType dependencyType, DistType distType,
                                List<String> versionPredicates, @Nullable SimplePredicate<T> predicate,
                                boolean optional, T obj) {
        this.value = value;
        this.dependencyType = dependencyType;
        this.distType = distType;
        this.versionPredicates = versionPredicates;
        this.predicate = predicate;
        this.optional = optional;
        this.obj = obj;
    }

    @SuppressWarnings("unchecked")
    public static <T> @NotNull DependencyContainer<T> of(@NotNull Dependency dependency, T obj) {
        SimplePredicate<T> predicate = null;

        if (dependency.dependencyType() == DependencyType.PREDICATE) {
            try {
                Class<?> clazz = Class.forName(dependency.predicate().getName());

                if (clazz.isInterface()) {
                    MagicLib.getLogger().error("Predicate class {} is a interface, excepted implementation class.",
                            clazz.getName());
                } else {
                    predicate = (SimplePredicate<T>) clazz.getConstructor().newInstance();
                }
            } catch (Exception e) {
                MagicLib.getLogger().error("Failed to instantiate a Predicate from class {}: {}",
                        dependency.predicate().getName(), e);
            }
        }

        return new DependencyContainer<>(dependency.value(), dependency.dependencyType(), dependency.distType(),
                Lists.newArrayList(dependency.versionPredicates()), predicate, dependency.optional(), obj);
    }

    @SuppressWarnings("unchecked")
    public static <T> @NotNull DependencyContainer<T> of(AnnotationNode annotationNode, T obj) {
        Type type = Annotations.getValue(annotationNode, "predicate");
        SimplePredicate<T> predicate = null;
        DependencyType dependencyType = Annotations.getValue(annotationNode, "dependencyType",
                DependencyType.class, DependencyType.MOD_ID);

        if (dependencyType == DependencyType.PREDICATE) {
            try {
                Class<?> clazz = Class.forName(type.getClassName());

                if (clazz.isInterface()) {
                    MagicLib.getLogger()
                            .error("Predicate class {} is a interface, excepted implementation class.",
                                    clazz.getName());
                } else {
                    predicate = (SimplePredicate<T>) clazz.getConstructor().newInstance();
                }
            } catch (Exception e) {
                MagicLib.getLogger().error("Failed to instantiate a Predicate from class {}: {}",
                        type.getClassName(), e);
            }
        }

        return new DependencyContainer<>(
                Annotations.getValue(annotationNode, "value"),
                dependencyType,
                Annotations.getValue(annotationNode, "distType", DistType.class, DistType.ANY),
                Lists.newArrayList(Annotations.getValue(annotationNode, "versionPredicates", Lists.newArrayList())),
                predicate,
                Annotations.getValue(annotationNode, "optional", Dependency.class),
                obj
        );
    }

    public ValueContainer<DependencyCheckResult> check() {
        IPlatform platform = MagicLib.getInstance().getPlatformManage().getCurrentPlatform();

        switch (this.dependencyType) {
            case DIST:
                DistType currentdistType = platform.getCurrentDistType();

                if (this.distType.matches(currentdistType)) {
                    return ValueContainer.of(new DependencyCheckResult(true, String.format(
                            "Running on unexpected side %s.", currentdistType)));
                }

                return ValueContainer.of(new DependencyCheckResult(false, String.format(
                        "Running on unexpected side, expected %s, but found %s.", this.distType, currentdistType)));
            case MIXIN:


                break;
            case MOD_ID:
                if (!platform.isModLoaded(this.value) && !this.optional) {
                    return ValueContainer.of(new DependencyCheckResult(false, String.format(
                            "Mod %s not found. Requires %s!", this.value,
                            this.versionPredicates.isEmpty() ? "[*]" : this.versionPredicates)));
                }

                String loadedVersion = platform.getModVersion(this.value);

                if (!VersionUtil.isVersionSatisfyPredicates(loadedVersion, this.versionPredicates)) {
                    return ValueContainer.of(new DependencyCheckResult(false, String.format(
                            "Mod %s (%s) detected. Requires %s, but found %s!",
                            platform.getModName(this.value), this.value,
                            this.versionPredicates.isEmpty() ? "[*]" : this.versionPredicates, loadedVersion)));
                }

                return ValueContainer.of(new DependencyCheckResult(true, String.format(
                        "Conflicted Mod %s (%s)@%s detected.",
                        platform.getModName(this.value), this.value, loadedVersion)));
            case PREDICATE:
                if (this.predicate != null) {
                    boolean testResult = this.predicate.test(this.obj);
                    return ValueContainer.of(new DependencyCheckResult(testResult, String.format(
                            "Predicate test result = %s", testResult)));
                }
        }

        return ValueContainer.empty();
    }

    public boolean isSatisfied() {
        return this.check().isEmpty() || this.check().get().isSuccess();
    }
}
