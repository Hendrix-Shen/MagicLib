package top.hendrixshen.magiclib.api.platform.adapter.forge;

import net.minecraftforge.fml.loading.moddiscovery.ModFileInfo;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;
import top.hendrixshen.magiclib.util.collect.ValueContainer;

import java.util.Collection;

public interface ModListAdapter {
    ValueContainer<Collection<ModFileInfo>> getModFiles();

    ValueContainer<Collection<ModInfo>> getMods();
}
