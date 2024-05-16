package top.hendrixshen.magiclib.impl.platform.adapter;

import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.platform.adapter.ModContainerAdapter;
import top.hendrixshen.magiclib.api.platform.adapter.ModEntryPointAdapter;
import top.hendrixshen.magiclib.api.platform.adapter.ModMetaDataAdapter;

import java.util.NoSuchElementException;

public class ForgeModContainer implements ModContainerAdapter {
    private final ModContainer modContainer;
    private final ModEntryPointAdapter modEntryPoint;

    private ForgeModContainer(ModContainer modContainer) {
        this.modContainer = modContainer;
        this.modEntryPoint = new ForgeModEntryPoint(this);
    }

    public static @NotNull ModContainerAdapter of(String modIdentifier) {
        ModContainer modContainer = ModList.get().getModContainerById(modIdentifier)
                .orElseThrow(() -> new NoSuchElementException("No value present"));
        return new ForgeModContainer(modContainer);
    }

    public static @NotNull ModContainerAdapter of(ModContainer modContainer) {
        return new ForgeModContainer(modContainer);
    }

    public ModContainer get() {
        return this.modContainer;
    }

    @Override
    public ModMetaDataAdapter getModMetaData() {
        return new ForgeModMetaData(this.modContainer.getModInfo());
    }

    @Override
    public ModEntryPointAdapter getModEntryPoint() {
        return this.modEntryPoint;
    }
}
