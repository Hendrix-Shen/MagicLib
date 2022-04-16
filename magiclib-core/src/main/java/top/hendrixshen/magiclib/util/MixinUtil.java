package top.hendrixshen.magiclib.util;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.transformer.ClassInfo;
import org.spongepowered.asm.mixin.transformer.ext.ITargetClassContext;
import org.spongepowered.asm.service.MixinService;
import org.spongepowered.asm.util.Annotations;
import top.hendrixshen.magiclib.compat.annotation.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MixinUtil {
    private static final Field mixinInfoStateField;
    private static final Field mixinInfoInfoField;
    private static final Field mixinInfoStateClassNodeField;
    private static final Field mixinsField;
    public static Map<String, String> classMap = new ConcurrentHashMap<>();

    static {
        try {
            mixinInfoStateField = Class.forName("org.spongepowered.asm.mixin.transformer.MixinInfo")
                    .getDeclaredField("state");
            mixinInfoStateField.setAccessible(true);

            mixinInfoInfoField = Class.forName("org.spongepowered.asm.mixin.transformer.MixinInfo")
                    .getDeclaredField("info");
            mixinInfoInfoField.setAccessible(true);

            mixinInfoStateClassNodeField = Class.forName("org.spongepowered.asm.mixin.transformer.MixinInfo$State")
                    .getDeclaredField("classNode");
            mixinInfoStateClassNodeField.setAccessible(true);

            mixinsField = Class.forName("org.spongepowered.asm.mixin.transformer.TargetClassContext")
                    .getDeclaredField("mixins");
            mixinsField.setAccessible(true);


        } catch (NoSuchFieldException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


    }

    @SuppressWarnings("unchecked")
    public static SortedSet<IMixinInfo> getMixins(ITargetClassContext iTargetClassContext) {
        try {
            return (SortedSet<IMixinInfo>) mixinsField.get(iTargetClassContext);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static ClassInfo getClassInfo(IMixinInfo iMixinInfo) {
        try {
            return (ClassInfo) mixinInfoInfoField.get(iMixinInfo);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static ClassNode getMixinClassNode(IMixinInfo iMixinInfo) {
        try {
            return (ClassNode) mixinInfoStateClassNodeField.get(mixinInfoStateField.get(iMixinInfo));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getMinecraftTypeStr(String str, int startIdx) {
        int lIndex = str.indexOf("Lnet/minecraft/", startIdx);
        if (lIndex == -1) {
            return null;
        }
        int rIndex = str.indexOf(";", lIndex);
        assert rIndex != -1;
        return str.substring(lIndex + 1, rIndex);
    }

    public static String remap(String str) {
        if (str == null) {
            return null;
        }
        int nextIdx = -1;
        for (String minecraftTypeStr = getMinecraftTypeStr(str, 0); minecraftTypeStr != null; minecraftTypeStr = getMinecraftTypeStr(str, nextIdx)) {
            nextIdx = str.indexOf(minecraftTypeStr, nextIdx + 1);
            str = str.replace(String.format("L%s;", minecraftTypeStr),
                    String.format("L%s;", classMap.getOrDefault(minecraftTypeStr, minecraftTypeStr)));
        }
        // Remap str without L%s;
        return classMap.getOrDefault(str, str);
    }

    private static boolean containsMethodNode(Collection<MethodNode> methodNodes, String name, String desc) {
        return methodNodes.stream().anyMatch(
                methodNode -> methodNode.name.equals(name) &&
                        methodNode.desc.equals(desc));
    }

    public static void applyInit(ClassNode classNode) {
        Set<MethodNode> thisInitMethodSet = new HashSet<>();
        Set<MethodNode> superInitMethodSet = new HashSet<>();

        // Get thisInitMethodSet and superInitMethodSet.
        for (MethodNode methodNode : classNode.methods) {
            AnnotationNode thisInitMethodAnnotation = Annotations.getVisible(methodNode, ThisInitMethod.class);
            AnnotationNode superInitMethodAnnotation = Annotations.getVisible(methodNode, SuperInitMethod.class);

            if (thisInitMethodAnnotation != null) {
                thisInitMethodSet.add(methodNode);
            }

            if (superInitMethodAnnotation != null) {
                superInitMethodSet.add(methodNode);
            }
        }

        // this super 构造方法无法直接调用，这里只是个 warpper，类似 shadow，因此需要移除掉
        classNode.methods.removeAll(thisInitMethodSet);
        classNode.methods.removeAll(superInitMethodSet);

        for (MethodNode methodNode : classNode.methods) {
            AnnotationNode initMethodAnnotation = Annotations.getVisible(methodNode, InitMethod.class);
            // Remap this and super
            if (initMethodAnnotation != null) {
                methodNode.name = "<init>";
                boolean initInvoke = false;
                for (AbstractInsnNode abstractInsnNode : methodNode.instructions) {
                    if (abstractInsnNode instanceof MethodInsnNode) {
                        MethodInsnNode methodInsnNode = (MethodInsnNode) abstractInsnNode;
                        if (methodInsnNode.getOpcode() == Opcodes.INVOKEVIRTUAL) {
                            if (containsMethodNode(thisInitMethodSet, methodInsnNode.name, methodInsnNode.desc)) {
                                methodInsnNode.setOpcode(Opcodes.INVOKESPECIAL);
                                methodInsnNode.name = "<init>";
                                initInvoke = true;
                                break;
                            } else if (containsMethodNode(superInitMethodSet, methodInsnNode.name, methodInsnNode.desc)) {
                                methodInsnNode.setOpcode(Opcodes.INVOKESPECIAL);
                                methodInsnNode.name = "<init>";
                                methodInsnNode.owner = classNode.superName;
                                initInvoke = true;
                                break;
                            }
                        }
                    }
                }
                // Add default constructor
                if (!initInvoke) {
                    methodNode.instructions.insert(new MethodInsnNode(Opcodes.INVOKESPECIAL, classNode.superName, "<init>", "()V"));
                    methodNode.instructions.insert(new VarInsnNode(Opcodes.ALOAD, 0));
                }
                methodNode.visibleAnnotations.removeIf(annotationNode -> annotationNode.desc.equals(Type.getDescriptor(InitMethod.class)));
            }
        }
    }

    public static void applyPublic(ClassNode classNode) {
        for (FieldNode fieldNode : classNode.fields) {
            AnnotationNode fieldPublicAnnotation = Annotations.getVisible(fieldNode, Public.class);
            if (fieldPublicAnnotation != null) {
                fieldNode.access = (fieldNode.access & ~(Opcodes.ACC_PUBLIC | Opcodes.ACC_PRIVATE | Opcodes.ACC_PROTECTED)) | Opcodes.ACC_PUBLIC;
            }
        }
        for (MethodNode methodNode : classNode.methods) {
            AnnotationNode publicAnnotation = Annotations.getVisible(methodNode, Public.class);
            if (publicAnnotation != null) {
                methodNode.access = (methodNode.access & ~(Opcodes.ACC_PUBLIC | Opcodes.ACC_PRIVATE | Opcodes.ACC_PROTECTED)) | Opcodes.ACC_PUBLIC;
            }
        }
    }

    public static void applyInnerClass(ClassNode classNode, ClassNode mixinClassNode) {
        AnnotationNode innerClassAnnotation = Annotations.getVisible(mixinClassNode, InnerClass.class);
        if (innerClassAnnotation == null) {
            return;
        }

        ArrayList<String> innerClassAnnotations = new ArrayList<>();
        Object classInnerAnnotationValue = Annotations.getValue(innerClassAnnotation, "value");

        if (classInnerAnnotationValue instanceof Type) {
            innerClassAnnotations.add(((Type) classInnerAnnotationValue).getClassName());
        } else {
            for (Object t : (ArrayList<?>) classInnerAnnotationValue) {
                innerClassAnnotations.add(((Type) t).getClassName());
            }
        }

        for (String innerClassName : innerClassAnnotations) {
            ClassNode innerClassNode;
            try {
                innerClassNode = MixinService.getService().getBytecodeProvider().getClassNode(innerClassName);
            } catch (ClassNotFoundException | IOException e) {
                throw new RuntimeException(e);
            }
            innerClassNode.innerClasses.clear();

            AnnotationNode classRemapAnnotation = Annotations.getVisible(innerClassNode, Remap.class);
            if (classRemapAnnotation != null) {
                String oldName = innerClassNode.name;
                // Remap first time
                if (!classMap.containsKey(oldName)) {
                    classMap.put(oldName, Annotations.getValue(classRemapAnnotation, "value"));
                    applyRemap(innerClassNode);
                    MagicStreamHandler.addClass(innerClassNode);
                }
            }
            // TODO add inner class to classNode
        }
    }

    public static void remapInterface(ClassNode classNode) {
        // remap interfaces
        for (String interfaceName : classNode.interfaces) {
            ClassNode interfaceClassNode;
            try {
                interfaceClassNode = MixinService.getService().getBytecodeProvider().getClassNode(interfaceName);
            } catch (ClassNotFoundException | IOException e) {
                throw new RuntimeException(e);
            }

            AnnotationNode classRemapAnnotation = Annotations.getVisible(interfaceClassNode, Remap.class);
            if (classRemapAnnotation != null) {
                String oldName = interfaceClassNode.name;

                // Remap first time
                if (!classMap.containsKey(oldName)) {
                    classMap.put(oldName, Annotations.getValue(classRemapAnnotation, "value"));
                    applyRemap(interfaceClassNode);
                    MagicStreamHandler.addClass(interfaceClassNode);
                }
            }
        }
    }

    public static void applyRemap(ClassNode classNode) {
        // Remap interfaces name
        for (int i = 0; i < classNode.interfaces.size(); ++i) {
            String classIntermediaryName = classMap.getOrDefault(classNode.interfaces.get(i), null);
            if (classIntermediaryName != null) {
                classNode.interfaces.set(i, classIntermediaryName);
            }
        }

        classNode.signature = remap(classNode.signature);
        classNode.name = remap(classNode.name);

        // Remap field
        Map<String, FieldNode> remappedFieldsMap = new HashMap<>();
        for (FieldNode fieldNode : classNode.fields) {
            AnnotationNode fieldRemapAnnotation = Annotations.getVisible(fieldNode, Remap.class);
            if (fieldRemapAnnotation != null) {
                String intermediaryName = Annotations.getValue(fieldRemapAnnotation, "value");
                remappedFieldsMap.put(fieldNode.name, fieldNode);
                fieldNode.name = intermediaryName;
            }
            fieldNode.desc = remap(fieldNode.desc);
        }
        // 移除已有的同名的 field
        classNode.fields.removeIf(fieldNode ->
                remappedFieldsMap.values().stream().anyMatch(remappedFieldNode ->
                        remappedFieldNode.name.equals(fieldNode.name) && remappedFieldNode != fieldNode)
        );

        Map<String, MethodNode> remappedMethodsMap = new HashMap<>();

        // Remap method name
        for (MethodNode methodNode : classNode.methods) {
            AnnotationNode remapAnnotation = Annotations.getVisible(methodNode, Remap.class);
            if (remapAnnotation != null) {
                remappedMethodsMap.put(methodNode.name + methodNode.desc, methodNode);
                methodNode.name = Annotations.getValue(remapAnnotation, "value");
                methodNode.visibleAnnotations.removeIf(annotationNode -> annotationNode.desc.equals(Type.getDescriptor(Remap.class)));
            }
            methodNode.desc = remap(methodNode.desc);
        }

        // Remap method instructions
        for (MethodNode methodNode : classNode.methods) {
            for (AbstractInsnNode abstractInsnNode : methodNode.instructions) {
                if (abstractInsnNode instanceof FieldInsnNode) {
                    FieldInsnNode fieldInsnNode = (FieldInsnNode) abstractInsnNode;
                    FieldNode remappedFieldNode = remappedFieldsMap.getOrDefault(fieldInsnNode.name, null);
                    if (remappedFieldNode != null) {
                        fieldInsnNode.name = remappedFieldNode.name;
                    }
                    fieldInsnNode.desc = remap(fieldInsnNode.desc);
                    fieldInsnNode.owner = remap(fieldInsnNode.owner);
                }

                if (abstractInsnNode instanceof MethodInsnNode) {
                    MethodInsnNode methodInsnNode = (MethodInsnNode) abstractInsnNode;
                    if (Objects.equals(methodInsnNode.owner, classNode.name)) {
                        MethodNode remappedMethodNode = remappedMethodsMap.getOrDefault(methodInsnNode.name + methodInsnNode.desc, null);
                        if (remappedMethodNode != null) {
                            methodInsnNode.name = remappedMethodNode.name;
                        }
                    }
                    methodInsnNode.desc = remap(methodInsnNode.desc);
                    methodInsnNode.owner = remap(methodInsnNode.owner);
                }

                if (abstractInsnNode instanceof TypeInsnNode) {
                    TypeInsnNode typeInsnNode = (TypeInsnNode) abstractInsnNode;
                    typeInsnNode.desc = remap(typeInsnNode.desc);
                }
            }
        }

        // Remap local var
        for (MethodNode methodNode : classNode.methods) {
            if (methodNode.localVariables != null) {
                for (LocalVariableNode localVariableNode : methodNode.localVariables) {
                    localVariableNode.desc = remap(localVariableNode.desc);
                }
            }
        }

        // 移除已有的同名的 method
        classNode.methods.removeIf(methodNode -> remappedMethodsMap.values().stream().anyMatch(
                remappedMethodNode -> remappedMethodNode.name.equals(methodNode.name) &&
                        remappedMethodNode.desc.equals(methodNode.desc) &&
                        remappedMethodNode != methodNode
        ));
    }
}
