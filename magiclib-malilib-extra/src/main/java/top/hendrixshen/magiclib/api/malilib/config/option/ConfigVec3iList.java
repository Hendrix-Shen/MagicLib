package top.hendrixshen.magiclib.api.malilib.config.option;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.IConfigResettable;
import net.minecraft.core.Vec3i;

import java.util.List;

public interface ConfigVec3iList extends IConfigResettable, MagicIConfigBase {
    List<Vec3i> getVec3iList();

    void setVec3iList(List<Vec3i> vec3is);

    ImmutableList<Vec3i> getDefaultVec3iList();
}
