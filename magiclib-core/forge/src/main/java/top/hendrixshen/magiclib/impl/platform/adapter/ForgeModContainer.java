package top.hendrixshen.magiclib.impl.platform.adapter;

import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.platform.adapter.ModContainerAdapter;
import top.hendrixshen.magiclib.api.platform.adapter.ModMetaDataAdapter;

import java.util.NoSuchElementException;

public class ForgeModContainer implements ModContainerAdapter {
    private final ModContainer modContainer;

    private ForgeModContainer(ModContainer modContainer) {
        this.modContainer = modContainer;
    }

    public static @NotNull ModContainerAdapter of(String id) {
        ModContainer modContainer = ModList.get().getModContainerById(id)
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
}
