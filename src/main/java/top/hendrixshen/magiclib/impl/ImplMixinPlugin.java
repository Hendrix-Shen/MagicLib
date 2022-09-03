package top.hendrixshen.magiclib.impl;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import top.hendrixshen.magiclib.MagicMixinPlugin;

public class ImplMixinPlugin extends MagicMixinPlugin {
    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        super.postApply(targetClassName, targetClass, mixinClassName, mixinInfo);
        /*
         * Fix a stack overflow exception when you use Mojang Mappings in a development environment.
         * This bug only seems to affect MC 1.16-1.17.
         * And since 1.18, carpet uses Mojang Mappings, this bug fixed itself.
         */
        if (targetClassName.equals("net.minecraft.server.level.ChunkMap")) {
            for (MethodNode methodNode : targetClass.methods) {
                if (methodNode.name.equals("getChunks")) {
                    methodNode.instructions.clear();
                    InsnList insnList = new InsnList();
                    insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    insnList.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/server/level/ChunkMap", "visibleChunkMap", "Lit/unimi/dsi/fastutil/longs/Long2ObjectLinkedOpenHashMap;"));
                    insnList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "it/unimi/dsi/fastutil/longs/Long2ObjectLinkedOpenHashMap", "values", "()Lit/unimi/dsi/fastutil/objects/ObjectCollection;"));
                    insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/google/common/collect/Iterables", "unmodifiableIterable", "(Ljava/lang/Iterable;)Ljava/lang/Iterable;"));
                    insnList.add(new InsnNode(Opcodes.ARETURN));
                    methodNode.instructions.add(insnList);
                }
            }
        }
    }
}
