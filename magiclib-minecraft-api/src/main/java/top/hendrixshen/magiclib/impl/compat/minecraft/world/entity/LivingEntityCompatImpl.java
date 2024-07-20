package top.hendrixshen.magiclib.impl.compat.minecraft.world.entity;

import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.compat.minecraft.world.entity.LivingEntityCompat;

//#if MC > 12004
//$$ import net.minecraft.core.Holder;
//$$ import net.minecraft.world.entity.ai.attributes.AttributeInstance;
//$$ import net.minecraft.world.entity.ai.attributes.Attributes;
//$$ import top.hendrixshen.magiclib.util.collect.ValueContainer;
//#endif

public class LivingEntityCompatImpl extends EntityCompatImpl implements LivingEntityCompat {
    public LivingEntityCompatImpl(@NotNull LivingEntity type) {
        super(type);
    }

    @Override
    public @NotNull LivingEntity get() {
        return (LivingEntity) super.get();
    }

    @Override
    public void setMaxUpStep(float maxUpStep) {
        //#if MC > 12004
        //$$ ValueContainer.ofNullable(this.get().getAttribute(Attributes.STEP_HEIGHT))
        //$$       .map(AttributeInstance::getAttribute)
        //$$       .map(Holder::value)
        //$$       .ifPresent(attribute -> attribute.sanitizeValue(maxUpStep));
        //#else
        super.setMaxUpStep(maxUpStep);
        //#endif
    }
}
