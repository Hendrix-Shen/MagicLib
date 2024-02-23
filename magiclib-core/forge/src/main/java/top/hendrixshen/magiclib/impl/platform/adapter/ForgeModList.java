package top.hendrixshen.magiclib.impl.platform.adapter;

import lombok.Getter;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.moddiscovery.ModFileInfo;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;
import top.hendrixshen.magiclib.api.platform.adapter.forge.ModListAdapter;
import top.hendrixshen.magiclib.util.collect.ValueContainer;

import java.util.Collection;

public class ForgeModList implements ModListAdapter {
    @Getter(lazy = true)
    private static final ModListAdapter instance = new ForgeModList();

    @Override
    public ValueContainer<Collection<ModFileInfo>> getModFiles() {
        return ValueContainer.ofNullable(ModList.get()).map(ModList::getModFiles);
    }

    @Override
    public ValueContainer<Collection<ModInfo>> getMods() {
        return ValueContainer.ofNullable(ModList.get()).map(ModList::getMods);
    }
}
