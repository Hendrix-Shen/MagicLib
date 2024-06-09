package top.hendrixshen.magiclib.impl.dependency;

import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.api.dependency.DependencyCheckException;
import top.hendrixshen.magiclib.api.i18n.I18n;
import top.hendrixshen.magiclib.api.platform.DistType;
import top.hendrixshen.magiclib.api.platform.adapter.ModContainerAdapter;
import top.hendrixshen.magiclib.api.platform.adapter.ModMetaDataAdapter;
import top.hendrixshen.magiclib.impl.gui.fabric.FabricGuiEntry;
import top.hendrixshen.magiclib.util.DependencyUtil;
import top.hendrixshen.magiclib.util.MiscUtil;
import top.hendrixshen.magiclib.util.collect.InfoNode;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

@ApiStatus.Internal
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EntryPointDependency {
    @Getter(lazy = true)
    private static final EntryPointDependency instance = new EntryPointDependency();

    private final AtomicBoolean isChecked = new AtomicBoolean();

    public void check() {
        if (this.isChecked.get()) {
            throw new IllegalStateException("Re-trigger EntryPointDependency check.");
        }

        List<DependencyCheckException> exceptions = Lists.newArrayList();

        for (ModContainerAdapter mod : MagicLib.getInstance().getCurrentPlatform().getMods()) {
            for (ClassNode entryPoint : mod.getModEntryPoint().getMagicEntryPoints()) {
                exceptions.add(this.check(mod.getModMetaData(), entryPoint));
            }
        }

        exceptions.stream()
                .filter(Objects::nonNull)
                .reduce((a, b) -> new DependencyCheckException(a.getMessage() + b.getMessage()))
                .ifPresent(e -> {
                    e.setStackTrace(new StackTraceElement[0]);
                    FabricGuiEntry.displayCriticalError(e, true);
                });
        this.isChecked.set(true);
    }

    private @Nullable DependencyCheckException check(ModMetaDataAdapter modMetaDataAdapter, ClassNode entryPoint) {
        List<DependenciesContainer<Object>> dependencies = Lists.newArrayList();

        if (MagicLib.getInstance().getCurrentPlatform().getCurrentDistType()
                .matches(DistType.CLIENT)) {
            dependencies.addAll(this.getDependencies("onInitializeClient", entryPoint));
        } else if (MagicLib.getInstance().getCurrentPlatform().getCurrentDistType()
                .matches(DistType.SERVER)) {
            dependencies.addAll(this.getDependencies("onInitializeServer", entryPoint));
        }

        dependencies.addAll(this.getDependencies("onInitialize", entryPoint));

        if (dependencies.stream().allMatch(DependenciesContainer::isSatisfied)) {
            return null;
        }

        InfoNode rootNode = new InfoNode(null, I18n.tr("magiclib.dependency.checker.entrypoint.title",
                modMetaDataAdapter.getName(), modMetaDataAdapter.getModId(), modMetaDataAdapter.getVersion()));
        MiscUtil.generateDependencyCheckMessage(dependencies, rootNode);
        return new DependencyCheckException("\n" + rootNode);
    }

    private @NotNull List<DependenciesContainer<Object>> getDependencies(String name, @NotNull ClassNode entryPoint) {
        for (MethodNode method : entryPoint.methods) {
            if (!(method.name.equals(name) && method.desc.equals("()V"))) {
                continue;
            }

            return DependencyUtil.parseDependencies(method, null);
        }

        return Collections.emptyList();
    }
}
