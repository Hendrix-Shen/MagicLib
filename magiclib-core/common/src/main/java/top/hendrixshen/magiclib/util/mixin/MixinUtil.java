package top.hendrixshen.magiclib.util.mixin;

import com.google.common.collect.Lists;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.service.MixinService;
import top.hendrixshen.magiclib.MagicLib;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class MixinUtil {
    public static @Nullable ClassNode getClassNode(String className) {
        try {
            return MixinService.getService().getBytecodeProvider().getClassNode(className);
        } catch (ClassNotFoundException | IOException e) {
            MagicLib.getLogger().error("Failed to fetch class node {}: ", className, e);
        }

        return null;
    }

    public static @NotNull List<String> getMethodVisibleAnnotations(@NotNull MethodNode methodNode) {
        List<String> ret = Lists.newArrayList();

        if (methodNode.visibleAnnotations != null) {
            ret.addAll(methodNode.visibleAnnotations
                    .stream()
                    .map(annotationNode -> annotationNode.desc)
                    .collect(Collectors.toList()));
        }

        return ret;
    }

    public static @NotNull List<String> getMethodInvisibleAnnotations(@NotNull MethodNode methodNode) {
        List<String> ret = Lists.newArrayList();

        if (methodNode.invisibleAnnotations != null) {
            ret.addAll(methodNode.invisibleAnnotations
                    .stream()
                    .map(annotationNode -> annotationNode.desc)
                    .collect(Collectors.toList()));
        }

        return ret;
    }

    public static @NotNull List<String> getMethodAnnotations(@NotNull MethodNode methodNode) {
        List<String> ret = MixinUtil.getMethodInvisibleAnnotations(methodNode);
        ret.addAll(MixinUtil.getMethodVisibleAnnotations(methodNode));

        return ret;
    }

    public static boolean containsMethodNode(@NotNull Collection<MethodNode> methodNodes, String name, String desc) {
        return methodNodes.stream()
                .anyMatch(methodNode -> methodNode.name.equals(name) && methodNode.desc.equals(desc));
    }
}
