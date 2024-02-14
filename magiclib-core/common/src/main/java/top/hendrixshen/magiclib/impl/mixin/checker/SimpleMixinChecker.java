package top.hendrixshen.magiclib.impl.mixin.checker;

import com.google.common.collect.Lists;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.util.Annotations;
import top.hendrixshen.magiclib.api.dependency.DependencyCheckException;
import top.hendrixshen.magiclib.api.i18n.I18n;
import top.hendrixshen.magiclib.api.mixin.annotation.MagicMixinConfig;
import top.hendrixshen.magiclib.api.mixin.checker.MixinDependencyCheckFailureCallback;
import top.hendrixshen.magiclib.api.mixin.checker.MixinDependencyChecker;
import top.hendrixshen.magiclib.impl.dependency.DependenciesContainer;
import top.hendrixshen.magiclib.util.mixin.MixinUtil;

import java.util.List;
import java.util.stream.Collectors;

public class SimpleMixinChecker implements MixinDependencyChecker {
    private MixinDependencyCheckFailureCallback failureCallback;

    private void addLine(@NotNull List<String> list, String line) {
        list.add(line);
        list.add("\n");
    }

    @Override
    public boolean check(String targetClassName, String mixinClassName) {
        ClassNode targetClassNode = MixinUtil.getClassNode(targetClassName);
        ClassNode mixinClassNode = MixinUtil.getClassNode(mixinClassName);

        if (targetClassNode == null || mixinClassNode == null) {
            return false;
        }

        AnnotationNode mixinConfigNode = Annotations.getVisible(mixinClassNode, MagicMixinConfig.class);
        List<AnnotationNode> nodes = Annotations.getValue(mixinConfigNode, "dependencies", true);
        List<DependenciesContainer<ClassNode>> dependencies = nodes
                .stream()
                .map(node -> DependenciesContainer.of(node, targetClassNode))
                .collect(Collectors.toList());
        List<String> resultList = Lists.newArrayList();
        boolean ret = true;
        boolean first = true;

        for (DependenciesContainer<ClassNode> dependenciesContainer : dependencies) {
            boolean conflictSatisfied = dependenciesContainer.isConflictSatisfied();
            boolean requireSatisfied = dependenciesContainer.isRequireSatisfied();

            if (first) {
                first = false;
            } else if (!conflictSatisfied || !requireSatisfied) {
                this.addLine(resultList, "!" + I18n.tr("magiclib.dependency.label.or"));
            }

            if (!conflictSatisfied) {
                this.addLine(resultList, I18n.tr("magiclib.dependency.label.conflict"));
                dependenciesContainer.checkConflict().forEach(r ->
                        this.addLine(resultList, String.format("\t%s", r.getReason())));
            }

            if (!requireSatisfied) {
                this.addLine(resultList, I18n.tr("magiclib.dependency.label.require"));
                dependenciesContainer.checkRequire().forEach(r ->
                        this.addLine(resultList, String.format("\t%s", r.getReason())));
            }

            ret = conflictSatisfied && requireSatisfied && ret;
        }

        if (!resultList.isEmpty()) {
            resultList.remove(resultList.size() - 1);
        }

        String result = resultList.stream()
                .map(s -> s.startsWith("!") ? s.replaceFirst("^!", "") : "\t" + s)
                .collect(Collectors.joining());

        if (!ret) {
            this.onCheckFailure(targetClassName, mixinClassName, new DependencyCheckException(result.toString()));
        }

        return ret;
    }

    @Override
    public void setCheckFailureCallback(MixinDependencyCheckFailureCallback callback) {
        this.failureCallback = callback;
    }

    private void onCheckFailure(String targetClassName, String mixinClassName, DependencyCheckException result) {
        if (this.failureCallback != null) {
            this.failureCallback.callback(targetClassName, mixinClassName, result);
        }
    }
}
