package top.hendrixshen.magiclib.impl.mixin.extension.jikuTsuiho.info;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import top.hendrixshen.magiclib.api.mixin.extension.jikuTsuiho.info.MixinMethodInfo;
import top.hendrixshen.magiclib.util.mixin.MixinUtil;

@AllArgsConstructor
public class MixinMethodInfoImpl implements MixinMethodInfo {
    @Getter
    private final IMixinInfo mixinInfo;
    private final MethodNode methodNode;

    @Contract("_, _ -> new")
    public static @NotNull MixinMethodInfo of(IMixinInfo mixinInfo, MethodNode methodNode) {
        return new MixinMethodInfoImpl(mixinInfo, methodNode);
    }

    @Override
    public int getAccess() {
        return this.methodNode.access;
    }

    @Override
    public String getName() {
        return this.methodNode.name;
    }

    @Override
    public String getDesc() {
        return this.methodNode.desc;
    }

    @Override
    public boolean containsVisibleAnnotation(String annotationDesc) {
        return MixinUtil.getMethodVisibleAnnotations(this.methodNode).contains(annotationDesc);
    }

    @Override
    public boolean containsInVisibleAnnotation(String annotationDesc) {
        return MixinUtil.getMethodInvisibleAnnotations(this.methodNode).contains(annotationDesc);
    }
}
