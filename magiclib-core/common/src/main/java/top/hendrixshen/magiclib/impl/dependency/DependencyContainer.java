package top.hendrixshen.magiclib.impl.dependency;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.spongepowered.asm.util.Annotations;
import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.api.dependency.DependencyType;
import top.hendrixshen.magiclib.api.dependency.annotation.Dependency;
import top.hendrixshen.magiclib.api.i18n.I18n;
import top.hendrixshen.magiclib.api.platform.DistType;
import top.hendrixshen.magiclib.api.platform.Platform;
import top.hendrixshen.magiclib.api.platform.PlatformType;
import top.hendrixshen.magiclib.util.VersionUtil;
import top.hendrixshen.magiclib.util.collect.SimplePredicate;
import top.hendrixshen.magiclib.util.collect.ValueContainer;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Getter
public class DependencyContainer<T> {
    private final String value;
    private final DependencyType dependencyType;
    private final DistType distType;
    private final PlatformType platformType;
    private final List<String> versionPredicates;
    private final SimplePredicate<T> predicate;
    private final boolean optional;
    private final T obj;

    private DependencyContainer(String value, DependencyType dependencyType, DistType distType,
                                PlatformType platformType, List<String> versionPredicates,
                                SimplePredicate<T> predicate, boolean optional, T obj) {
        this.value = value;
        this.dependencyType = dependencyType;
        this.distType = distType;
        this.platformType = platformType;
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
                    throw new IllegalStateException(String.format("Predicate class %s is a interface, excepted implementation class.",
                            clazz.getName()));
                } else {
                    predicate = (SimplePredicate<T>) clazz.getConstructor().newInstance();
                }
            } catch (IllegalStateException e) {
                throw e;
            } catch (Exception e) {
                throw new IllegalStateException(String.format("Failed to instantiate a Predicate from class %s: %s",
                        dependency.predicate().getName(), e));
            }
        }

        // TODO: Remove this in the future
        // For backward compatibility
        PlatformType platformType;

        try {
            platformType = dependency.platformType();
        } catch (NoSuchMethodError e) {
            platformType = PlatformType.ANY;
        }

        return new DependencyContainer<>(dependency.value(), dependency.dependencyType(), dependency.distType(),
                platformType, Lists.newArrayList(dependency.versionPredicates()),
                predicate, dependency.optional(), obj);
    }

    @SuppressWarnings("unchecked")
    public static <T> @NotNull DependencyContainer<T> of(AnnotationNode annotationNode, T obj) {
        SimplePredicate<T> predicate = null;
        DependencyType dependencyType = Annotations.getValue(annotationNode, "dependencyType",
                DependencyType.class, DependencyType.MOD_ID);

        if (dependencyType == DependencyType.PREDICATE) {
            Type type = Annotations.getValue(annotationNode, "predicate");
            Objects.requireNonNull(type,
                    "Dependency type is set to PREDICATE mode, which requires the predicate field to be specified!");

            try {
                Class<?> clazz = Class.forName(type.getClassName());

                if (clazz.isInterface()) {
                    throw new IllegalStateException(String.format("Predicate class %s is a interface, excepted implementation class.",
                            clazz.getName()));
                } else {
                    predicate = (SimplePredicate<T>) clazz.getConstructor().newInstance();
                }
            } catch (IllegalStateException e) {
                throw e;
            } catch (Exception e) {
                throw new IllegalStateException(String.format("Failed to instantiate a Predicate from class %s",
                        type.getClassName()), e);
            }
        }

        return new DependencyContainer<>(
                Annotations.getValue(annotationNode, "value"),
                dependencyType,
                Annotations.getValue(annotationNode, "distType", DistType.class, DistType.ANY),
                Annotations.getValue(annotationNode, "platformType", PlatformType.class, PlatformType.ANY),
                Lists.newArrayList(Annotations.getValue(annotationNode, "versionPredicates", Lists.newArrayList())),
                predicate,
                Annotations.getValue(annotationNode, "optional", Dependency.class),
                obj
        );
    }

    public ValueContainer<DependencyCheckResult> checkAsRequire() {
        Platform platform = MagicLib.getInstance().getCurrentPlatform();

        switch (this.dependencyType) {
            case DIST:
                DistType currentdistType = platform.getCurrentDistType();

                if (this.distType.matches(currentdistType)) {
                    return ValueContainer.of(new DependencyCheckResult(true,
                            I18n.tr("magiclib.dependency.result.dist.require.success", currentdistType)));
                }

                return ValueContainer.of(new DependencyCheckResult(false,
                        I18n.tr("magiclib.dependency.result.dist.require.fail", this.distType, currentdistType)));
            case MOD_ID:
                Objects.requireNonNull(this.value,
                        "Dependency type is set to MOD_ID mode and requires mod id as value");

                if (platform.isModExist(this.value)) {
                    String loadedVersion = platform.getModVersion(this.value);

                    if (VersionUtil.isVersionSatisfyPredicates(loadedVersion, this.versionPredicates)) {
                        return ValueContainer.of(new DependencyCheckResult(true,
                                I18n.tr("magiclib.dependency.result.mod_id." + (this.optional ?
                                                "optional.success.found" : "require.success"),
                                        platform.getModName(this.value), this.value, this.versionPredicates.isEmpty() ?
                                                "[*]" : this.versionPredicates)));
                    }

                    return ValueContainer.of(new DependencyCheckResult(false,
                            I18n.tr("magiclib.dependency.result.mod_id." + (this.optional ?
                                            "optional.fail" : "require.fail.unsatisfied"), platform.getModName(this.value),
                                    this.value, this.versionPredicates.isEmpty() ? "[*]" : this.versionPredicates,
                                    loadedVersion)));
                }

                return ValueContainer.of(new DependencyCheckResult(this.optional,
                        I18n.tr("magiclib.dependency.result.mod_id." + (this.optional ?
                                "optional.success" : "require.fail") + ".not_found", this.value,
                                this.versionPredicates.isEmpty() ? "[*]" : this.versionPredicates)));
            case PLATFORM:
                PlatformType currentPlatformType = platform.getPlatformType();

                if (this.platformType.matches(currentPlatformType)) {
                    return ValueContainer.of(new DependencyCheckResult(true,
                            I18n.tr("magiclib.dependency.result.platform.require.success", currentPlatformType)));
                }

                return ValueContainer.of(new DependencyCheckResult(false,
                        I18n.tr("magiclib.dependency.result.platform.require.fail",
                                this.platformType, currentPlatformType)));
            case PREDICATE:
                boolean testResult = this.predicate.test(this.obj);
                return ValueContainer.of(new DependencyCheckResult(testResult, I18n.tr(
                        "magiclib.dependency.result.predicate.message",
                        this.predicate.getClass().getName(), testResult)));
        }

        return ValueContainer.empty();
    }

    public ValueContainer<DependencyCheckResult> checkAsConflict() {
        Platform platform = MagicLib.getInstance().getCurrentPlatform();

        switch (this.dependencyType) {
            case DIST:
                DistType currentdistType = platform.getCurrentDistType();

                if (this.distType.matches(currentdistType)) {
                    return ValueContainer.of(new DependencyCheckResult(false,
                            I18n.tr("magiclib.dependency.result.dist.conflict.fail", this.distType)));
                }

                return ValueContainer.of(new DependencyCheckResult(true,
                        I18n.tr("magiclib.dependency.result.dist.conflict.success", this.distType)));
            case MOD_ID:
                Objects.requireNonNull(this.value,
                        "Dependency type is set to MOD_ID mode and requires mod id as value");

                if (platform.isModExist(this.value)) {
                    String loadedVersion = platform.getModVersion(this.value);

                    if (VersionUtil.isVersionSatisfyPredicates(loadedVersion, this.versionPredicates)) {
                        return ValueContainer.of(new DependencyCheckResult(false,
                                I18n.tr("magiclib.dependency.result.mod_id.conflict.fail",
                                        platform.getModName(this.value), this.value, loadedVersion,
                                        this.versionPredicates.isEmpty() ? "[*]" : this.versionPredicates)));
                    }

                    return ValueContainer.of(new DependencyCheckResult(true,
                            I18n.tr("magiclib.dependency.result.mod_id.conflict.success.unsatisfied",
                                    platform.getModName(this.value), this.value, loadedVersion,
                                    this.versionPredicates.isEmpty() ? "[*]" : this.versionPredicates)));
                }

                return ValueContainer.of(new DependencyCheckResult(true,
                        I18n.tr("magiclib.dependency.result.mod_id.conflict.success.not_found",
                                this.value)));
            case PLATFORM:
                PlatformType currentPlatformType = platform.getPlatformType();

                if (this.platformType.matches(currentPlatformType)) {
                    return ValueContainer.of(new DependencyCheckResult(false,
                            I18n.tr("magiclib.dependency.result.platform.conflict.fail", this.platformType)));
                }

                return ValueContainer.of(new DependencyCheckResult(true,
                        I18n.tr("magiclib.dependency.result.platform.conflict.success", this.platformType)));
            case PREDICATE:
                boolean testResult = this.predicate.test(this.obj);
                return ValueContainer.of(new DependencyCheckResult(!testResult,
                        I18n.tr("magiclib.dependency.result.predicate.message",
                                this.predicate.getClass().getName(), testResult)));
        }

        return ValueContainer.empty();
    }

    public boolean isSatisfied(@NotNull DependencyStyle dependencyStyle) {
        ValueContainer<DependencyCheckResult> result = dependencyStyle.function.apply(this);
        return result.isEmpty() || result.get().isSuccess();
    }

    @AllArgsConstructor
    public enum DependencyStyle {
        CONFLICT(DependencyContainer::checkAsConflict),
        REQUIRE(DependencyContainer::checkAsRequire);

        private final Function<DependencyContainer<?>, ValueContainer<DependencyCheckResult>> function;
    }
}
