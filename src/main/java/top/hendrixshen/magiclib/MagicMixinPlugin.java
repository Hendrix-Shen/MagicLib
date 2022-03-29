package top.hendrixshen.magiclib;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.service.MixinService;
import org.spongepowered.asm.util.Annotations;
import top.hendrixshen.magiclib.compat.annotation.*;
import top.hendrixshen.magiclib.dependency.Dependencies;
import top.hendrixshen.magiclib.dependency.mixin.DepCheckFailureCallback;
import top.hendrixshen.magiclib.util.FabricUtil;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MagicMixinPlugin extends EmptyMixinPlugin {

    public static Map<String, String> classMap = new ConcurrentHashMap<>();

    private static boolean compatVersionChecked = false;

    private DepCheckFailureCallback depCheckFailureCallback =
            (targetClassName, mixinClassName, reason) -> {
                if (MixinEnvironment.getCurrentEnvironment().getOption(MixinEnvironment.Option.DEBUG_EXPORT)) {
                    MagicLibReference.LOGGER.warn("{}: \nMixin {} can't apply to {} because: \n{}",
                            Optional.ofNullable(reason.getCause()).orElse(reason).getClass().getSimpleName(),
                            mixinClassName, targetClassName, reason.getMessage());
                }
            };

    private static boolean containsMethodNode(Collection<MethodNode> methodNodes, String name, String desc) {
        return methodNodes.stream().anyMatch(
                thisInitMethod -> thisInitMethod.name.equals(name) &&
                        thisInitMethod.desc.equals(desc));
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
        int nextIdx = -1;
        for (String minecraftTypeStr = getMinecraftTypeStr(str, 0); minecraftTypeStr != null; minecraftTypeStr = getMinecraftTypeStr(str, nextIdx)) {
            nextIdx = str.indexOf(minecraftTypeStr, nextIdx + 1);
            str = str.replace(String.format("L%s;", minecraftTypeStr),
                    String.format("L%s;", classMap.getOrDefault(minecraftTypeStr, minecraftTypeStr)));
        }
        return str;
    }

    public static void remapClass(ClassNode classNode) {

        Map<String, String> remapFields = new HashMap<>();
        classNode.interfaces.removeAll(classMap.keySet());


        for (FieldNode fieldNode : classNode.fields) {
            AnnotationNode fieldRemapAnnotation = Annotations.getVisible(fieldNode, Remap.class);
            AnnotationNode fieldPublicAnnotation = Annotations.getVisible(fieldNode, Public.class);
            if (fieldRemapAnnotation != null) {
                String intermediaryName = Annotations.getValue(fieldRemapAnnotation, "value");
                remapFields.put(fieldNode.name, intermediaryName);
                fieldNode.name = intermediaryName;
            }
            if (fieldPublicAnnotation != null) {
                fieldNode.access = (fieldNode.access & ~(Opcodes.ACC_PUBLIC | Opcodes.ACC_PRIVATE | Opcodes.ACC_PROTECTED)) | Opcodes.ACC_PUBLIC;
            }
            fieldNode.desc = remap(fieldNode.desc);
        }
        Set<MethodNode> thisInitMethodSet = new HashSet<>();
        Set<MethodNode> superInitMethodSet = new HashSet<>();

        // get thisInitMethodSet and superInitMethodSet
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
        classNode.methods.removeAll(thisInitMethodSet);
        classNode.methods.removeAll(superInitMethodSet);

        Map<String, MethodNode> remappedMethodNodeMap = new HashMap<>();

        // remap method name
        for (MethodNode methodNode : classNode.methods) {
            AnnotationNode remapAnnotation = Annotations.getVisible(methodNode, Remap.class);
            AnnotationNode publicAnnotation = Annotations.getVisible(methodNode, Public.class);
            AnnotationNode initMethodAnnotation = Annotations.getVisible(methodNode, InitMethod.class);

            if (remapAnnotation != null) {
                remappedMethodNodeMap.put(methodNode.name + methodNode.desc, methodNode);
                methodNode.name = Annotations.getValue(remapAnnotation, "value");
                methodNode.visibleAnnotations.removeIf(annotationNode -> annotationNode.desc.equals(Type.getDescriptor(Remap.class)));
            }
            if (publicAnnotation != null) {
                methodNode.access = (methodNode.access & ~(Opcodes.ACC_PUBLIC | Opcodes.ACC_PRIVATE | Opcodes.ACC_PROTECTED)) | Opcodes.ACC_PUBLIC;
            }

            // remap this and super
            if (initMethodAnnotation != null) {
                remappedMethodNodeMap.put(methodNode.name + methodNode.desc, methodNode);
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
                // add default constructor
                if (!initInvoke) {
                    methodNode.instructions.insert(new MethodInsnNode(Opcodes.INVOKESPECIAL, classNode.superName, "<init>", "()V"));
                    methodNode.instructions.insert(new VarInsnNode(Opcodes.ALOAD, 0));
                }
                methodNode.visibleAnnotations.removeIf(annotationNode -> annotationNode.desc.equals(Type.getDescriptor(Remap.class)));
            }
            methodNode.desc = remap(methodNode.desc);
        }

        // remap method instructions
        for (MethodNode methodNode : classNode.methods) {
            for (AbstractInsnNode abstractInsnNode : methodNode.instructions) {
                if (abstractInsnNode instanceof FieldInsnNode) {
                    FieldInsnNode fieldInsnNode = (FieldInsnNode) abstractInsnNode;
                    String fieldIntermediaryName = remapFields.getOrDefault(fieldInsnNode.name, null);
                    if (fieldIntermediaryName != null) {
                        fieldInsnNode.name = fieldIntermediaryName;
                    }

                }
                if (abstractInsnNode instanceof MethodInsnNode) {
                    MethodInsnNode methodInsnNode = (MethodInsnNode) abstractInsnNode;

                    if (Objects.equals(methodInsnNode.owner, classNode.name)) {
                        MethodNode remappedMethodNode = remappedMethodNodeMap.getOrDefault(methodInsnNode.name + methodInsnNode.desc, null);
                        if (remappedMethodNode != null) {
                            methodInsnNode.name = remappedMethodNode.name;
                        }
                    }
                }
                if (abstractInsnNode instanceof TypeInsnNode) {
                    TypeInsnNode typeInsnNode = (TypeInsnNode) abstractInsnNode;
                    typeInsnNode.desc = classMap.getOrDefault(typeInsnNode.desc, typeInsnNode.desc);
                }
            }
        }
        classNode.methods.removeIf(methodNode -> containsMethodNode(remappedMethodNodeMap.values(), methodNode.name, methodNode.desc));
        classNode.methods.addAll(remappedMethodNodeMap.values());
    }

    public void setDepCheckFailureCallback(DepCheckFailureCallback depCheckFailureCallback) {
        this.depCheckFailureCallback = depCheckFailureCallback;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return Dependencies.checkDependency(targetClassName, mixinClassName, this.depCheckFailureCallback);
    }

    @Override
    public void onLoad(String mixinPackage) {
        if (!compatVersionChecked) {
            FabricUtil.compatVersionCheck();
            compatVersionChecked = true;
        }
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        ClassNode mixinClassNode = mixinInfo.getClassNode(ClassReader.SKIP_CODE);
        for (String interfaceName : mixinClassNode.interfaces) {
            ClassNode interfaceClassNode;
            try {
                interfaceClassNode = MixinService.getService().getBytecodeProvider().getClassNode(interfaceName);
            } catch (ClassNotFoundException | IOException e) {
                throw new RuntimeException(e);
            }


            AnnotationNode classRemapAnnotation = Annotations.getVisible(interfaceClassNode, Remap.class);
            if (classRemapAnnotation != null) {
                String classIntermediaryName = Annotations.getValue(classRemapAnnotation, "value");
                String oldName = interfaceClassNode.name;

                // remap first time
                if (!classMap.containsKey(oldName)) {
                    classMap.put(oldName, Annotations.getValue(classRemapAnnotation, "value"));
                    interfaceClassNode.name = classIntermediaryName;
                    remapClass(interfaceClassNode);
                    FabricUtil.loadClass(oldName, interfaceClassNode);
                }
                if (!targetClass.interfaces.contains(classIntermediaryName)) {
                    targetClass.interfaces.add(classIntermediaryName);
                }
            }
        }
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        remapClass(targetClass);
    }

}
