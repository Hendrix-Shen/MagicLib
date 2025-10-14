package top.hendrixshen.magiclib.impl.event.minecraft.render;

// CHECKSTYLE.OFF: JavadocStyle
/**
 * Since submission and rendering have been separated, we no longer provide this API.
 *
 * <li>mc1.14 ~ mc1.12.8: subproject 1.16.5 (main project)</li>
 * <li>mc1.21.9+        : subproject 1.21.10 [dummy]        &lt;--------</li>
 */
// CHECKSTYLE.ON: JavadocStyle
public final class RenderEntityEvent {
    private RenderEntityEvent() {
        throw new AssertionError("No" + this.getClass().getName() + "instance for you!");
    }
}
