package top.hendrixshen.magiclib.event.render.api;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import top.hendrixshen.magiclib.event.render.impl.RenderContext;

import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
interface IRenderEvent<T> {
    default Supplier<String> getProfilerSectionSupplier() {
        return () -> this.getClass().getName();
    }

    void render(T obj, RenderContext context, float tickDelta);
}
