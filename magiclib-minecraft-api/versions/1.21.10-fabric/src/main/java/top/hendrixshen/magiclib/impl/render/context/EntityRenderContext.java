package top.hendrixshen.magiclib.impl.render.context;

/**
 * Since submission and rendering have been separated, we no longer provide this API.
 *
 * <p>
 * Preprocessor version guide.
 *
 * <li>mc1.14 ~ mc1.12.8: subproject 1.16.5 (main project)</li>
 * <li>mc1.21.9+        : subproject 1.21.10 [dummy]        &lt;--------</li>
 */
public final class EntityRenderContext {
    private EntityRenderContext() {
        throw new AssertionError("No" + this.getClass().getName() + "instance for you!");
    }
}
