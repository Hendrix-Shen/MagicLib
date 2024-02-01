package top.hendrixshen.magiclib.impl.mixin.extension.jikuTsuiho.info;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import top.hendrixshen.magiclib.api.mixin.extension.jikuTsuiho.info.MixinClassInfo;

@AllArgsConstructor
public class MixinClassInfoImpl implements MixinClassInfo {
    @Getter
    private final IMixinInfo mixinInfo;
    private final ClassNode classNode;

    @Contract("_, _ -> new")
    public static @NotNull MixinClassInfo of(IMixinInfo mixinInfo, ClassNode classNode) {
        return new MixinClassInfoImpl(mixinInfo, classNode);
    }

    @Override
    public int getVersion() {
        return this.classNode.version;
    }

    @Override
    public int getAccess() {
        return this.classNode.access;
    }

    @Override
    public String getName() {
        return this.classNode.name;
    }
}
