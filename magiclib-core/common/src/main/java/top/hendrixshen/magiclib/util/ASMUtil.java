package top.hendrixshen.magiclib.util;

import com.google.common.collect.Lists;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import top.hendrixshen.magiclib.util.collect.ValueContainer;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;

public class ASMUtil {
    @Contract(pure = true)
    public static @NotNull ValueContainer<AnnotationNode> getVisibleAnnotations(
            @NotNull ClassNode classNode, Class<? extends Annotation> annotationClass) {
        return ASMUtil.getAnnotations(classNode.visibleAnnotations, Type.getDescriptor(annotationClass));
    }

    public static @NotNull ValueContainer<AnnotationNode> getInvisibleAnnotations(
            @NotNull ClassNode classNode, Class<? extends Annotation> annotationClass) {
        return ASMUtil.getAnnotations(classNode.invisibleAnnotations, Type.getDescriptor(annotationClass));
    }

    public static @NotNull ValueContainer<AnnotationNode> getAnnotations(List<AnnotationNode> annotations,
                                                                     String annotationType) {
        if (annotations == null) {
            return ValueContainer.empty();
        }

        for (AnnotationNode annotation : annotations) {
            if (annotationType.equals(annotation.desc)) {
                return ValueContainer.of(annotation);
            }
        }

        return ValueContainer.empty();
    }
}
