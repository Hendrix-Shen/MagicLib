package top.hendrixshen.magiclib.impl.mixin.extension;

import com.google.common.collect.Sets;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.transformer.ext.ITargetClassContext;
import org.spongepowered.asm.mixin.transformer.meta.MixinMerged;
import org.spongepowered.asm.util.Annotations;
import top.hendrixshen.magiclib.api.mixin.annotation.MagicInit;
import top.hendrixshen.magiclib.api.mixin.annotation.SuperInit;
import top.hendrixshen.magiclib.api.mixin.annotation.ThisInit;
import top.hendrixshen.magiclib.api.mixin.extension.EmptyExtension;
import top.hendrixshen.magiclib.util.mixin.MixinUtil;

import java.util.Set;

public final class MagicInitExtension extends EmptyExtension {
    @Override
    public boolean checkActive(MixinEnvironment environment) {
        return true;
    }

    @Override
    public void postApply(@NotNull ITargetClassContext context) {
        ClassNode classNode = context.getClassNode();
        Set<MethodNode> thisInitMethods = Sets.newHashSet();
        Set<MethodNode> superInitMethods = Sets.newHashSet();

        // Get thisInit and superInit shadow, also backup them.
        for (MethodNode methodNode : classNode.methods) {
            AnnotationNode thisInit = Annotations.getInvisible(methodNode, ThisInit.class);
            AnnotationNode superInit = Annotations.getInvisible(methodNode, SuperInit.class);

            if (thisInit != null) {
                thisInitMethods.add(methodNode);
            }

            if (superInit != null) {
                superInitMethods.add(methodNode);
            }
        }

        // The shadow constructor cannot be called directly, and needs to be removed.
        classNode.methods.removeAll(thisInitMethods);
        classNode.methods.removeAll(superInitMethods);

        // Apply MagicInit
        for (MethodNode methodNode : classNode.methods) {
            if (Annotations.getVisible(methodNode, MagicInit.class) == null) {
                continue;
            }

            methodNode.name = "<init>";
            boolean initInvoke = false;

            for (AbstractInsnNode abstractInsnNode : methodNode.instructions) {
                if (abstractInsnNode.getOpcode() != Opcodes.INVOKEVIRTUAL) {
                    continue;
                }

                if (!(abstractInsnNode instanceof MethodInsnNode)) {
                    continue;
                }

                MethodInsnNode methodInsnNode = (MethodInsnNode) abstractInsnNode;

                if (MixinUtil.containsMethodNode(thisInitMethods, methodInsnNode.name, methodInsnNode.desc)) {
                    methodInsnNode.setOpcode(Opcodes.INVOKESPECIAL);
                    methodInsnNode.name = "<init>";
                    initInvoke = true;
                    break;
                } else if (MixinUtil.containsMethodNode(superInitMethods, methodInsnNode.name, methodInsnNode.desc)) {
                    methodInsnNode.setOpcode(Opcodes.INVOKESPECIAL);
                    methodInsnNode.name = "<init>";
                    methodInsnNode.owner = classNode.superName;
                    initInvoke = true;
                    break;
                }
            }

            // Add default constructor invoke.
            if (!initInvoke) {
                methodNode.instructions.insert(new MethodInsnNode(Opcodes.INVOKESPECIAL, classNode.superName, "<init>", "()V"));
                methodNode.instructions.insert(new VarInsnNode(Opcodes.ALOAD, 0));
                AnnotationNode info = Annotations.getVisible(methodNode, MixinMerged.class);

                if (info == null) {
                    return;
                }
            }
        }
    }
}
