package top.hendrixshen.magiclib.impl.mixin.checker;

import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.util.Annotations;
import top.hendrixshen.magiclib.api.dependency.DependencyCheckException;
import top.hendrixshen.magiclib.api.dependency.annotation.CompositeDependencies;
import top.hendrixshen.magiclib.api.i18n.I18n;
import top.hendrixshen.magiclib.api.mixin.checker.MixinDependencyCheckFailureCallback;
import top.hendrixshen.magiclib.api.mixin.checker.MixinDependencyChecker;
import top.hendrixshen.magiclib.impl.dependency.DependenciesContainer;
import top.hendrixshen.magiclib.util.MiscUtil;
import top.hendrixshen.magiclib.util.collect.InfoNode;
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

        AnnotationNode mixinConfigNode = Annotations.getVisible(mixinClassNode, CompositeDependencies.class);
        List<AnnotationNode> nodes = Annotations.getValue(mixinConfigNode, "value", true);
        List<DependenciesContainer<?>> dependencies = nodes
                .stream()
                .map(node -> DependenciesContainer.of(node, targetClassNode))
                .collect(Collectors.toList());

        if (dependencies.stream().allMatch(DependenciesContainer::isSatisfied)) {
            return false;
        }

        InfoNode rootNode = new InfoNode(null, I18n.tr("magiclib.dependency.checker.mixin.title",
                mixinClassName, targetClassName));
        MiscUtil.generateDependencyCheckMessage(dependencies, rootNode);
        this.onCheckFailure(targetClassName, mixinClassName, new DependencyCheckException(rootNode.toString()));

        return false;
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
