package top.hendrixshen.magiclib.api.render;

// CHECKSTYLE.OFF: ImportOrder
//#if MC >= 26.3
//$$ import com.mojang.blaze3d.systems.RenderSystem;
//$$ import com.mojang.renderpearl.api.device.GpuDevice;
//#elseif MC >= 26.2
//$$ import com.mojang.blaze3d.systems.GpuDevice;
//$$ import com.mojang.blaze3d.systems.RenderSystem;
//#endif
// CHECKSTYLE.ON: ImportOrder

/**
 * The graphics backend used by the running game.
 *
 * <p>
 * Minecraft 26.2 introduced an experimental Vulkan backend, and Minecraft 26.3 moved the whole
 * graphics device abstraction into the {@code com.mojang.renderpearl} namespace. All immediate
 * (direct) graphics state manipulation performed by {@code RenderGlobal} only exists on the OpenGL
 * backend, so callers must consult {@link #supportsImmediateState()} before touching the graphics
 * state directly.
 *
 * <p>
 * {@link #getCurrent()} reports {@link #UNKNOWN} when no device has been created yet, and unknown
 * backends are treated as not supporting immediate state, so an unrecognized backend can never crash
 * the game.
 */
public enum RenderBackend {
    /**
     * The OpenGL backend, which is the only backend available before Minecraft 26.2.
     */
    OPENGL(true),

    /**
     * The experimental Vulkan backend, introduced in Minecraft 26.2.
     */
    VULKAN(false),

    /**
     * An unknown backend, either because no graphics device exists yet or because the device reports
     * a backend name this library does not recognize.
     */
    UNKNOWN(false);

    private final boolean immediateStateSupported;

    RenderBackend(boolean immediateStateSupported) {
        this.immediateStateSupported = immediateStateSupported;
    }

    /**
     * Gets the graphics backend that is currently in use.
     *
     * <p>
     * Minecraft versions prior to 26.2 only ship the OpenGL backend, so this method always returns
     * {@link #OPENGL} for them.
     *
     * @return the current graphics backend.
     */
    public static RenderBackend getCurrent() {
        //#if MC >= 26.2
        //$$ GpuDevice device = RenderSystem.tryGetDevice();
        //$$
        //$$ if (device == null) {
        //$$     return RenderBackend.UNKNOWN;
        //$$ }
        //$$
        //$$ String backendName = device.getDeviceInfo().backendName();
        //$$
        //$$ if ("OpenGL".equalsIgnoreCase(backendName)) {
        //$$     return RenderBackend.OPENGL;
        //$$ }
        //$$
        //$$ if ("Vulkan".equalsIgnoreCase(backendName)) {
        //$$     return RenderBackend.VULKAN;
        //$$ }
        //$$
        //$$ return RenderBackend.UNKNOWN;
        //#else
        return RenderBackend.OPENGL;
        //#endif
    }

    /**
     * Checks whether this backend exposes the immediate graphics state.
     *
     * @return {@code true} if immediate state calls are safe on this backend, {@code false} otherwise.
     */
    public boolean supportsImmediateState() {
        return this.immediateStateSupported;
    }
}
