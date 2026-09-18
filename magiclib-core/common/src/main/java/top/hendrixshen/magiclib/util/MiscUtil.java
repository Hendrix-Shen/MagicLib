package top.hendrixshen.magiclib.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import top.hendrixshen.magiclib.api.i18n.I18n;
import top.hendrixshen.magiclib.impl.dependency.DependenciesContainer;
import top.hendrixshen.magiclib.impl.dependency.DependencyCheckResult;
import top.hendrixshen.magiclib.util.collect.InfoNode;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class MiscUtil {
    public static @NotNull String getSystemLanguageCode() {
        return Locale.getDefault().toString().toLowerCase();
    }

    @SuppressWarnings("unchecked")
    public static <T> T cast(Object obj) {
        return (T) obj;
    }

    public static <T> void generateDependencyCheckMessage(@NotNull List<DependenciesContainer<T>> dependencies,
                                                          InfoNode rootNode) {
        MiscUtil.generateDependencyCheckMessage(dependencies, rootNode, false,
                text -> text, result -> result.getReason());
    }

    /**
     * Builds the dependency check failure message tree.
     *
     * <p>
     * Every {@link DependenciesContainer} in the list is an alternative (OR), while the require and conflict
     * dependencies inside one container must all pass (AND). Each container is only evaluated once, and its
     * check results are rendered as a tree of {@code composite / or / require / conflict} nodes under the
     * given root node.
     * </p>
     *
     * <p>
     * A section is only rendered when the container declares the corresponding dependencies
     * ({@code showSatisfiedDependencies == true}, e.g. the config GUI) or when at least one of its checks
     * failed ({@code showSatisfiedDependencies == false}, e.g. failure messages).
     * </p>
     *
     * @param dependencies The dependency containers to render.
     * @param rootNode The root node of the message tree.
     * @param showSatisfiedDependencies Whether to render sections whose checks all passed.
     * @param labelDecorator Decorates section labels, e.g. with GUI color codes.
     * @param resultDecorator Decorates each check result line.
     * @param <T> The type of the checked object.
     */
    public static <T> void generateDependencyCheckMessage(@NotNull List<DependenciesContainer<T>> dependencies,
                                                          InfoNode rootNode,
                                                          boolean showSatisfiedDependencies,
                                                          @NotNull Function<String, String> labelDecorator,
                                                          @NotNull Function<DependencyCheckResult, String> resultDecorator) {
        boolean firstRendered = true;
        boolean composite = false;
        InfoNode compositeNode;

        for (DependenciesContainer<?> container : dependencies) {
            List<DependencyCheckResult> conflictResults = container.checkConflict();
            List<DependencyCheckResult> requireResults = container.checkRequire();
            boolean renderConflict = MiscUtil.shouldRender(showSatisfiedDependencies, conflictResults);
            boolean renderRequire = MiscUtil.shouldRender(showSatisfiedDependencies, requireResults);

            if (!renderConflict && !renderRequire) {
                continue;
            }

            InfoNode orNode = null;

            if (firstRendered) {
                firstRendered = false;
            } else {
                if (!composite) {
                    compositeNode = new InfoNode(null,
                            labelDecorator.apply(I18n.tr("magiclib.dependency.label.composite")));

                    for (InfoNode child : rootNode.getChildren()) {
                        child.moveTo(compositeNode);
                    }

                    compositeNode.moveTo(rootNode);
                    composite = true;
                }

                orNode = new InfoNode(rootNode, labelDecorator.apply(I18n.tr("magiclib.dependency.label.or")));
            }

            InfoNode sectionParent = orNode == null ? rootNode : orNode;

            if (renderConflict) {
                InfoNode conflictNode = new InfoNode(sectionParent,
                        labelDecorator.apply(I18n.tr("magiclib.dependency.label.conflict")));

                for (DependencyCheckResult result : conflictResults) {
                    new InfoNode(conflictNode, resultDecorator.apply(result));
                }
            }

            if (renderRequire) {
                InfoNode requireNode = new InfoNode(sectionParent,
                        labelDecorator.apply(I18n.tr("magiclib.dependency.label.require")));

                for (DependencyCheckResult result : requireResults) {
                    new InfoNode(requireNode, resultDecorator.apply(result));
                }
            }
        }
    }

    private static boolean shouldRender(boolean showSatisfiedDependencies,
                                        @NotNull List<DependencyCheckResult> results) {
        if (results.isEmpty()) {
            return false;
        }

        if (showSatisfiedDependencies) {
            return true;
        }

        return results.stream().anyMatch(result -> !result.isSuccess());
    }

    @Deprecated
    @ApiStatus.ScheduledForRemoval()
    public static Gson GSON = GsonUtil.GSON;

    @Deprecated
    @ApiStatus.ScheduledForRemoval()
    public static JsonObject readJson(@NotNull URL url) throws IOException {
        return JsonUtil.readJson(url);
    }

    @Deprecated
    @ApiStatus.ScheduledForRemoval()
    public static void loadStringMapFromJson(InputStream inputStream, BiConsumer<String, String> biConsumer) {
        JsonUtil.loadStringMapFromJson(inputStream, biConsumer);
    }
}
