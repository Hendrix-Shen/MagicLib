package top.hendrixshen.magiclib.impl.mixin.extension.jikuTsuiho;

import com.google.common.collect.Sets;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.util.Annotations;
import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.api.mixin.annotation.JikuTsuiho;
import top.hendrixshen.magiclib.api.mixin.extension.jikuTsuiho.info.MixinClassInfo;
import top.hendrixshen.magiclib.api.mixin.extension.jikuTsuiho.info.MixinFieldInfo;
import top.hendrixshen.magiclib.api.mixin.extension.jikuTsuiho.info.MixinMethodInfo;
import top.hendrixshen.magiclib.api.mixin.extension.jikuTsuiho.predicate.MixinEraserClass;
import top.hendrixshen.magiclib.api.mixin.extension.jikuTsuiho.predicate.MixinEraserField;
import top.hendrixshen.magiclib.api.mixin.extension.jikuTsuiho.predicate.MixinEraserMethod;
import top.hendrixshen.magiclib.util.mixin.MixinUtil;

import java.util.Set;

public class MixinEraserManager {
    private static final Set<MixinEraserClass> jikuTsuihoClasses = Sets.newHashSet();
    private static final Set<MixinEraserField> jikuTsuihoFields = Sets.newHashSet();
    private static final Set<MixinEraserMethod> jikuTsuihoMethods = Sets.newHashSet();

    public static boolean shouldTsuiho(MixinClassInfo info) {
        return MixinEraserManager.jikuTsuihoClasses
                .stream()
                .anyMatch(eraser -> eraser.shouldErase(info));
    }

    public static boolean shouldTsuiho(MixinFieldInfo info) {
        return MixinEraserManager.jikuTsuihoFields
                .stream()
                .anyMatch(eraser -> eraser.shouldErase(info));
    }

    public static boolean shouldTsuiho(MixinMethodInfo info) {
        return MixinEraserManager.jikuTsuihoMethods
                .stream()
                .anyMatch(eraser -> eraser.shouldErase(info));
    }

    public static void register(MixinEraserClass eraser) {
        MixinEraserManager.jikuTsuihoClasses.add(eraser);
        MagicLib.getLogger().debug("Registered mixin class tsuiho: {}", eraser.getClass().getName());
    }

    public static void register(MixinEraserField eraser) {
        MixinEraserManager.jikuTsuihoFields.add(eraser);
        MagicLib.getLogger().debug("Registered mixin method tsuiho: {}", eraser.getClass().getName());
    }

    public static void register(MixinEraserMethod eraser) {
        MixinEraserManager.jikuTsuihoMethods.add(eraser);
        MagicLib.getLogger().debug("Registered mixin field tsuiho: {}", eraser.getClass().getName());
    }

    public static void parseFromClass(@NotNull Class<?> clazz) {
        ClassNode classNode = MixinUtil.getClassNode(clazz.getName());

        if (classNode == null) {
            throw new RuntimeException("Failed to parse class: " + clazz.getName());
        }

        int fields = 0;
        int methods = 0;

        for (FieldNode field : classNode.fields) {
            AnnotationNode annotation = Annotations.getInvisible(field, JikuTsuiho.class);

            if (annotation == null) {
                continue;
            }

            String mixinClassName = Annotations.getValue(annotation, "mixinClassName");
            String fieldName = field.name;
            String fieldDesc = field.desc;
            MixinEraserManager.jikuTsuihoFields.add(new InternalTsuihoField(mixinClassName, fieldName, fieldDesc));
            fields++;
        }

        for (MethodNode method : classNode.methods) {
            AnnotationNode annotation = Annotations.getInvisible(method, JikuTsuiho.class);

            if (annotation == null) {
                continue;
            }

            String mixinClassName = Annotations.getValue(annotation, "mixinClassName");
            Type injectType = Annotations.getValue(annotation, "injectType");
            String methodName = method.name;
            String methodDesc = method.desc;
            MixinEraserManager.jikuTsuihoMethods.add(new InternalTsuihoMethod(mixinClassName,
                    injectType.getDescriptor(), methodName, methodDesc));
            methods++;
        }

        MagicLib.getLogger().debug("Parsed {} fields and {} methods from {}", fields, methods, clazz.getName());
    }
}
