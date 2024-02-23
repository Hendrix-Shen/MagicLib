package top.hendrixshen.magiclib.impl.platform.adapter;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.platform.adapter.ModContainerAdapter;
import top.hendrixshen.magiclib.api.platform.adapter.ModEntryPointAdapter;
import top.hendrixshen.magiclib.api.platform.adapter.ModMetaDataAdapter;

import java.util.NoSuchElementException;

public class NeoForgeModContainer implements ModContainerAdapter {
    private final ModContainer modContainer;
    private final ModEntryPointAdapter modEntryPoint;

    private NeoForgeModContainer(ModContainer modContainer) {
        this.modContainer = modContainer;
        this.modEntryPoint = new NeoForgeModEntryPoint(this);
    }

    public static @NotNull ModContainerAdapter of(String modIdentifier) {
        ModContainer modContainer = ModList.get().getModContainerById(modIdentifier)
                .orElseThrow(() -> new NoSuchElementException("No value present"));
        return new NeoForgeModContainer(modContainer);
    }

    public static @NotNull ModContainerAdapter of(ModContainer modContainer) {
        return new NeoForgeModContainer(modContainer);
    }

    public ModContainer get() {
        return this.modContainer;
    }

    @Override
    public ModMetaDataAdapter getModMetaData() {
        return new NeoForgeModMetaData(this.modContainer.getModInfo());
    }

    @Override
    public ModEntryPointAdapter getModEntryPoint() {
        return this.modEntryPoint;
    }
}
