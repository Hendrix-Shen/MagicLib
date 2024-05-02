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

public class MiscUtil {
    public static @NotNull String getSystemLanguageCode() {
        return Locale.getDefault().toString().toLowerCase();
    }

    @SuppressWarnings("unchecked")
    public static <T> T cast(Object obj) {
        return (T) obj;
    }

    public static void generateDependencyCheckMessage(@NotNull List<DependenciesContainer<?>> dependencies,
                                                      InfoNode rootNode) {
        boolean first = true;
        boolean composite = false;
        InfoNode compositeNode = new InfoNode(null, I18n.tr("magiclib.dependency.label.composite"));

        for (DependenciesContainer<?> dependenciesContainer : dependencies) {
            boolean conflictSatisfied = dependenciesContainer.isConflictSatisfied();
            boolean requireSatisfied = dependenciesContainer.isRequireSatisfied();
            InfoNode orNode = null;

            if (first) {
                first = false;
            } else if (!conflictSatisfied || !requireSatisfied) {
                if (!composite) {
                    for (InfoNode child : rootNode.getChildren()) {
                        child.moveTo(compositeNode);
                    }

                    compositeNode.moveTo(rootNode);
                    composite = true;
                }

                orNode = new InfoNode(rootNode, I18n.tr("magiclib.dependency.label.or"));
            }

            if (!conflictSatisfied) {
                InfoNode conflictNode = new InfoNode(orNode == null ? rootNode : orNode,
                        I18n.tr("magiclib.dependency.label.conflict"));

                for (DependencyCheckResult result : dependenciesContainer.checkConflict()) {
                    new InfoNode(conflictNode, result.getReason());
                }
            }

            if (!requireSatisfied) {
                InfoNode requireNode = new InfoNode(orNode == null ? rootNode : orNode,
                        I18n.tr("magiclib.dependency.label.require"));

                for (DependencyCheckResult result : dependenciesContainer.checkRequire()) {
                    new InfoNode(requireNode, result.getReason());
                }
            }
        }
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
