package top.hendrixshen.magiclib.impl.compat.minecraft.world.entity;

import org.jetbrains.annotations.NotNull;

import net.minecraft.world.entity.LivingEntity;

// CHECKSTYLE.OFF: ImportOrder
//#if MC > 12004
//$$ import net.minecraft.world.entity.ai.attributes.Attributes;
//#endif
// CHECKSTYLE.ON: ImportOrder

import top.hendrixshen.magiclib.api.compat.minecraft.world.entity.LivingEntityCompat;

// CHECKSTYLE.OFF: ImportOrder
//#if MC > 12004
//$$ import top.hendrixshen.magiclib.util.collect.ValueContainer;
//#endif
// CHECKSTYLE.ON: ImportOrder

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
        //$$         .ifPresent(attribute -> {
        //$$             double value = attribute.getAttribute().value().sanitizeValue(maxUpStep);
        //$$             attribute.setBaseValue(value);
        //$$         });
        //#else
        super.setMaxUpStep(maxUpStep);
        //#endif
    }
}
