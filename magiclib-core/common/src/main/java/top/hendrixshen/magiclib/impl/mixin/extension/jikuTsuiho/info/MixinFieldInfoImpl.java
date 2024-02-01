package top.hendrixshen.magiclib.impl.mixin.extension.jikuTsuiho.info;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.tree.FieldNode;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import top.hendrixshen.magiclib.api.mixin.extension.jikuTsuiho.info.MixinFieldInfo;

@AllArgsConstructor
public class MixinFieldInfoImpl implements MixinFieldInfo {
    @Getter
    private final IMixinInfo mixinInfo;
    private final FieldNode fieldNode;

    @Contract("_, _ -> new")
    public static @NotNull MixinFieldInfo of(IMixinInfo mixinInfo, FieldNode fieldNode) {
        return new MixinFieldInfoImpl(mixinInfo, fieldNode);
    }

    @Override
    public int getAccess() {
        return this.fieldNode.access;
    }

    @Override
    public String getName() {
        return this.fieldNode.name;
    }

    @Override
    public String getDesc() {
        return this.fieldNode.desc;
    }
}
