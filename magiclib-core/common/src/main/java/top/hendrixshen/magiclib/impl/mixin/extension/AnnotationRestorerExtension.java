package top.hendrixshen.magiclib.impl.mixin.extension;

import com.google.common.collect.Sets;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.transformer.ext.ITargetClassContext;
import top.hendrixshen.magiclib.api.dependency.annotation.CompositeDependencies;
import top.hendrixshen.magiclib.api.mixin.extension.EmptyExtension;
import top.hendrixshen.magiclib.impl.mixin.AnnotationRestorer;

import java.lang.annotation.Annotation;
import java.util.Set;

public final class AnnotationRestorerExtension extends EmptyExtension {
    private static final Set<AnnotationRestorer> annotationRestorers = Sets.newHashSet();

    public static void register(Class<? extends Annotation> clazz) {
        AnnotationRestorerExtension.annotationRestorers.add(new AnnotationRestorer(clazz));
    }

    public static void unregister(Class<? extends Annotation> clazz) {
        AnnotationRestorerExtension.annotationRestorers.removeIf(restorer -> restorer.matchAnnotationClass(clazz));
    }

    public AnnotationRestorerExtension() {
        AnnotationRestorerExtension.register(CompositeDependencies.class);
    }

    @Override
    public boolean checkActive(MixinEnvironment environment) {
        return true;
    }

    @Override
    public void preApply(ITargetClassContext context) {
        AnnotationRestorerExtension.annotationRestorers.forEach(restorer ->
                restorer.preApply(context.getClassNode()));
    }

    @Override
    public void postApply(ITargetClassContext context) {
        AnnotationRestorerExtension.annotationRestorers.forEach(restorer ->
                restorer.postApply(context.getClassNode()));
    }
}
