package top.hendrixshen.magiclib.impl.malilib.config.restriction;

import fi.dy.masa.malilib.util.restrictions.UsageRestriction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.api.compat.minecraft.resources.ResourceLocationCompat;

import java.util.List;
import java.util.Set;

//#if MC > 12101
//$$ import net.minecraft.core.Holder;
//#endif

public class EntityTypeRestriction extends UsageRestriction<EntityType<?>> {
    @Override
    protected void setValuesForList(Set<EntityType<?>> set, List<String> list) {
        for (String name : list) {
            ResourceLocation rl = null;

            try {
                rl = ResourceLocationCompat.parse(name);
            } catch (Exception ignore) {
            }

            EntityType<?> entity = rl != null ?
                    Registry.ENTITY_TYPE.get(rl)
                    //#if MC > 12101
                    //$$ .map(Holder.Reference::value).orElse(null)
                    //#endif
                    : null;

            if (entity != null) {
                set.add(entity);
                continue;
            }

            MagicLib.getLogger().warn("Invalid entity in a black- or whitelist: '{}'", name);
        }
    }

    public boolean isAllowed(Entity entity) {
        return this.isAllowed(entity.getType());
    }
}
