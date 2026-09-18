package top.hendrixshen.magiclib.impl.dependency;

import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.api.dependency.DependencyCheckException;
import top.hendrixshen.magiclib.api.i18n.I18n;
import top.hendrixshen.magiclib.api.platform.DistType;
import top.hendrixshen.magiclib.api.platform.Platform;
import top.hendrixshen.magiclib.api.platform.adapter.ModContainerAdapter;
import top.hendrixshen.magiclib.api.platform.adapter.ModMetaDataAdapter;
import top.hendrixshen.magiclib.impl.gui.fabric.FabricGuiEntry;
import top.hendrixshen.magiclib.util.DependencyUtil;
import top.hendrixshen.magiclib.util.MiscUtil;
import top.hendrixshen.magiclib.util.collect.InfoNode;

import java.util.Collections;
import java.util.List;
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

        Platform platform = MagicLib.getInstance().getCurrentPlatform();
        DistType currentDistType = platform.getCurrentDistType();
        List<DependencyCheckException> exceptions = Lists.newArrayList();

        for (ModContainerAdapter mod : platform.getMods()) {
            for (ClassNode entryPoint : mod.getModEntryPoint().getMagicEntryPoints()) {
                this.checkEntryPoint(currentDistType, mod.getModMetaData(), entryPoint, exceptions);
            }
        }

        if (!exceptions.isEmpty()) {
            StringBuilder message = new StringBuilder();

            for (DependencyCheckException exception : exceptions) {
                message.append(exception.getMessage());
            }

            DependencyCheckException exception = new DependencyCheckException(message.toString());
            exception.setStackTrace(new StackTraceElement[0]);
            FabricGuiEntry.displayCriticalError(exception, true);
        }

        this.isChecked.set(true);
    }

    private void checkEntryPoint(DistType currentDistType, ModMetaDataAdapter modMetaData, ClassNode entryPoint,
                                 List<DependencyCheckException> exceptions) {
        List<DependenciesContainer<Object>> dependencies = Lists.newArrayList();

        if (currentDistType.matches(DistType.CLIENT)) {
            dependencies.addAll(this.getDependencies("onInitializeClient", entryPoint));
        } else if (currentDistType.matches(DistType.SERVER)) {
            dependencies.addAll(this.getDependencies("onInitializeServer", entryPoint));
        }

        dependencies.addAll(this.getDependencies("onInitialize", entryPoint));

        if (dependencies.isEmpty() || dependencies.stream().anyMatch(DependenciesContainer::isSatisfied)) {
            return;
        }

        InfoNode rootNode = new InfoNode(null, I18n.tr("magiclib.dependency.checker.entrypoint.title",
                modMetaData.getName(), modMetaData.getModId(), modMetaData.getVersion()));
        MiscUtil.generateDependencyCheckMessage(dependencies, rootNode);
        exceptions.add(new DependencyCheckException("\n" + rootNode));
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
