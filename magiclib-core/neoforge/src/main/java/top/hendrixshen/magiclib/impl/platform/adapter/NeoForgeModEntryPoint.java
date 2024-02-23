package top.hendrixshen.magiclib.impl.platform.adapter;

import com.google.common.collect.Lists;
import net.neoforged.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import top.hendrixshen.magiclib.api.entrypoint.ModInitializer;
import top.hendrixshen.magiclib.api.platform.adapter.ModEntryPointAdapter;
import top.hendrixshen.magiclib.util.ASMUtil;
import top.hendrixshen.magiclib.util.ReflectionUtil;
import top.hendrixshen.magiclib.util.collect.ValueContainer;
import top.hendrixshen.magiclib.util.mixin.MixinUtil;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class NeoForgeModEntryPoint implements ModEntryPointAdapter {
    private final List<ClassNode> entryPoints;

    public NeoForgeModEntryPoint(@NotNull NeoForgeModContainer container) {
        this.entryPoints = container.get().getModInfo().getOwningFile()
                .getFile()
                .getScanResult()
                .getClasses()
                .stream()
                .map(classData -> ReflectionUtil.<Type>getDeclaredFieldValue(classData.getClass(),
                        "clazz", classData))
                .map(ValueContainer::get)
                .map(type -> MixinUtil.getClassNode(type.getClassName()))
                .filter(Objects::nonNull)
                .filter(classNode -> ASMUtil.getVisibleAnnotations(classNode, Mod.class).isPresent())
                .collect(Collectors.toList());
    }

    @Override
    public Collection<ClassNode> getEntryPoints() {
        return Lists.newArrayList(this.entryPoints);
    }

    @Override
    public Collection<ClassNode> getMagicEntryPoints() {
        return this.entryPoints.stream()
                .filter(classNode -> classNode.interfaces.contains(Type.getType(ModInitializer.class).getInternalName()))
                .collect(Collectors.toList());
    }
}
