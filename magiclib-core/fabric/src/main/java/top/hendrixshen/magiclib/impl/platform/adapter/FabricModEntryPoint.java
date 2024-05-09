package top.hendrixshen.magiclib.impl.platform.adapter;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import top.hendrixshen.magiclib.api.platform.adapter.ModEntryPointAdapter;
import top.hendrixshen.magiclib.impl.platform.adapter.internal.ModMetaDataLite;
import top.hendrixshen.magiclib.util.collect.ValueContainer;
import top.hendrixshen.magiclib.util.mixin.MixinUtil;

import java.util.*;
import java.util.stream.Collectors;

public class FabricModEntryPoint implements ModEntryPointAdapter {
    private final List<ClassNode> entryPoints;

    public FabricModEntryPoint(@NotNull FabricModContainer container) {
        Set<String> containers = Sets.newHashSet();
        containers.addAll(FabricModEntryPoint.getEntryPoint("main", container.getModMetaData().getModId()));
        containers.addAll(FabricModEntryPoint.getEntryPoint("client", container.getModMetaData().getModId()));
        containers.addAll(FabricModEntryPoint.getEntryPoint("server", container.getModMetaData().getModId()));
        this.entryPoints = containers.stream()
                .map(FabricModEntryPoint::validateAndCreate)
                .filter(Objects::nonNull)
                .collect(Collectors.collectingAndThen(Collectors.toCollection(() ->
                        Sets.newTreeSet(Comparator.comparing(classNode -> classNode.name))), Lists::newArrayList));
    }

    @Override
    public Collection<ClassNode> getEntryPoints() {
        return Lists.newArrayList(this.entryPoints);
    }

    @Override
    public Collection<ClassNode> getMagicEntryPoints() {
        return this.entryPoints.stream()
                .filter(classNode ->
                        classNode.interfaces.contains(Type.getType(ModInitializer.class).getInternalName()) ||
                                classNode.interfaces.contains(Type.getType(ClientModInitializer.class).getInternalName()) ||
                                classNode.interfaces.contains(Type.getType(DedicatedServerModInitializer.class).getInternalName()))
                .collect(Collectors.toList());
    }

    public static @NotNull Collection<String> getEntryPoint(String key, String modIdentifier) {
        return Lists.newArrayList(ValueContainer.ofNullable(ModMetaDataLite.getMetaData(modIdentifier))
                .flatMap(metaData -> ValueContainer.ofNullable(metaData.getEntrypoints().get(key)))
                .orElse(Sets.newHashSet()));
    }

    private static @Nullable ClassNode validateAndCreate(@NotNull String value) {
        String[] methodSplit = value.split("::");

        // Only provide standard entrypoint.
        if (methodSplit.length != 1) {
            return null;
        }

        return MixinUtil.getClassNode(value);
    }
}
