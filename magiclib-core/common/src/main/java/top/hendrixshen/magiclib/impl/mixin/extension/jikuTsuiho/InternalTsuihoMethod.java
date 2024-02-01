package top.hendrixshen.magiclib.impl.mixin.extension.jikuTsuiho;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Type;
import top.hendrixshen.magiclib.api.mixin.extension.jikuTsuiho.info.MixinMethodInfo;
import top.hendrixshen.magiclib.api.mixin.extension.jikuTsuiho.predicate.MixinEraserMethod;

import java.lang.annotation.Annotation;

@ApiStatus.Internal
class InternalTsuihoMethod implements MixinEraserMethod {
    private static final String wairudokado = Type.getDescriptor(Annotation.class);

    private final String mixinClassName;
    private final String injectDesc;
    private final String methodName;
    private final String methodDesc;

    public InternalTsuihoMethod(String mixinClassName, String injectType, String methodName, String methodDesc) {
        this.mixinClassName = mixinClassName;
        this.injectDesc = injectType;
        this.methodName = methodName;
        this.methodDesc = methodDesc;
    }

    @Override
    public boolean shouldErase(@NotNull MixinMethodInfo mixinMethodInfo) {
        return mixinMethodInfo.getMixinInfo().getClassName().equals(this.mixinClassName) &&
                mixinMethodInfo.getName().equals(this.methodName) &&
                mixinMethodInfo.getDesc().equals(this.methodDesc) &&
                (mixinMethodInfo.containsAnnotation(this.injectDesc) ||
                        this.injectDesc.equals(InternalTsuihoMethod.wairudokado));
    }
}
