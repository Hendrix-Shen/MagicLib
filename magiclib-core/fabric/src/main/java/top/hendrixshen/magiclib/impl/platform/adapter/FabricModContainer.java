package top.hendrixshen.magiclib.impl.platform.adapter;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.platform.adapter.ModContainerAdapter;
import top.hendrixshen.magiclib.api.platform.adapter.ModMetaDataAdapter;

import java.util.NoSuchElementException;

public class FabricModContainer implements ModContainerAdapter {
    private final ModContainer modContainer;

    private FabricModContainer(ModContainer modContainer) {
        this.modContainer = modContainer;
    }

    public static @NotNull ModContainerAdapter of(String id) {
        ModContainer modContainer = FabricLoader.getInstance().getModContainer(id)
                .orElseThrow(() -> new NoSuchElementException("No value present"));
        return new FabricModContainer(modContainer);
    }

    public static @NotNull ModContainerAdapter of(ModContainer modContainer) {
        return new FabricModContainer(modContainer);
    }

    public ModContainer get() {
        return this.modContainer;
    }

    @Override
    public ModMetaDataAdapter getModMetaData() {
        return new FabricModMetaData(this.modContainer.getMetadata());
    }
}
