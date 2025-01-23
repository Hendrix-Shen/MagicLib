package top.hendrixshen.magiclib.api.malilib.config.option;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.IConfigResettable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import net.minecraft.core.Vec3i;

import java.util.List;

public interface ConfigVec3iTupleList extends IConfigResettable, MagicIConfigBase {
    List<Entry> getVec3iTupleList();

    void setVec3iTupleList(List<Entry> vec3iTuples);

    ImmutableList<Entry> getDefaultVec3iTupleList();

    @Getter
    @ToString
    @EqualsAndHashCode
    @AllArgsConstructor
    class Entry {
        public static Entry ZERO = new Entry(Vec3i.ZERO, Vec3i.ZERO);

        private final Vec3i firstVec3i;
        private final Vec3i secondVec3i;
    }
}
