package top.hendrixshen.magiclib.impl.mixin;

import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.util.Annotations;

import java.lang.annotation.Annotation;
import java.util.List;

@EqualsAndHashCode
public class AnnotationRestorer {
    private final Class<? extends Annotation> annotationClass;

    public AnnotationRestorer(Class<? extends Annotation> annotationClass) {
        this.annotationClass = annotationClass;
    }

    @Nullable
    private AnnotationNode previousAnnotationNode;

    public void preApply(ClassNode targetClass) {
        this.previousAnnotationNode = Annotations.getVisible(targetClass, annotationClass);
    }

    public void postApply(@NotNull ClassNode targetClass) {
        String descriptor = Type.getDescriptor(this.annotationClass);
        List<AnnotationNode> annotationNodes = targetClass.visibleAnnotations;

        if (annotationNodes == null) {
            return;
        }

        int index = -1;

        for (int i = 0; i < annotationNodes.size(); i++) {
            if (descriptor.equals(annotationNodes.get(i).desc)) {
                index = i;
                break;
            }
        }

        if (this.previousAnnotationNode == null && index != -1) {
            annotationNodes.remove(index);
        } else if (this.previousAnnotationNode != null) {
            annotationNodes.set(index, this.previousAnnotationNode);
        }
    }

    public boolean matchAnnotationClass(Class<? extends Annotation> annotationClass) {
        return this.annotationClass == annotationClass;
    }
}
