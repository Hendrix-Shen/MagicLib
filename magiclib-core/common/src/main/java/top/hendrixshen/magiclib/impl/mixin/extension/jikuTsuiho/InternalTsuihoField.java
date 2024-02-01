package top.hendrixshen.magiclib.impl.mixin.extension.jikuTsuiho;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.mixin.extension.jikuTsuiho.info.MixinFieldInfo;
import top.hendrixshen.magiclib.api.mixin.extension.jikuTsuiho.predicate.MixinEraserField;

@ApiStatus.Internal
class InternalTsuihoField implements MixinEraserField {
    private final String mixinClassName;
    private final String fieldName;
    private final String fieldDesc;

    public InternalTsuihoField(String mixinClassName, String fieldName, String fieldDesc) {
        this.mixinClassName = mixinClassName;
        this.fieldName = fieldName;
        this.fieldDesc = fieldDesc;
    }

    @Override
    public boolean shouldErase(@NotNull MixinFieldInfo mixinFieldInfo) {
        return mixinFieldInfo.getMixinInfo().getClassName().equals(this.mixinClassName) &&
                mixinFieldInfo.getName().equals(this.fieldName) &&
                mixinFieldInfo.getDesc().equals(this.fieldDesc);
    }
}
