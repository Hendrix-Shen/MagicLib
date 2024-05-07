package top.hendrixshen.magiclib.impl.platform.adapter;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import top.hendrixshen.magiclib.api.platform.adapter.ModEntryPointAdapter;
import top.hendrixshen.magiclib.util.mixin.MixinUtil;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FabricModEntryPoint implements ModEntryPointAdapter {
    private final List<ClassNode> entryPoints;

    public FabricModEntryPoint(@NotNull FabricModContainer container) {
        List<EntrypointContainer<?>> containers = Lists.newArrayList();
        containers.addAll(FabricModEntryPoint.getEntryPoint("main", ModInitializer.class,
                container.getModMetaData().getModId()));
        containers.addAll(FabricModEntryPoint.getEntryPoint("client", ClientModInitializer.class,
                container.getModMetaData().getModId()));
        containers.addAll(FabricModEntryPoint.getEntryPoint("server", DedicatedServerModInitializer.class,
                container.getModMetaData().getModId()));

        this.entryPoints = containers.stream()
                .map(entrypointContainer -> entrypointContainer.getEntrypoint().getClass())
                .filter(clazz -> !clazz.getName().startsWith("com.sun.proxy.jdk.proxy3"))
                .filter(clazz -> !clazz.getName().contains("$$Lambda"))
                .map(clazz -> MixinUtil.getClassNode(clazz.getName()))
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
                                classNode.interfaces.contains(Type.getType(ModInitializer.class).getInternalName()))
                .collect(Collectors.toList());
    }

    public static Collection<EntrypointContainer<?>> getEntryPoint(String key, Class<?> type, String modId) {
        return FabricLoader.getInstance().getEntrypointContainers(key, type).stream()
                .filter(entrypoint -> entrypoint.getProvider().getMetadata().getId().equals(modId))
                .collect(Collectors.toList());
    }
}
