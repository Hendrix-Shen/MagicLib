package top.hendrixshen.magiclib.api.mixin.extension.jikuTsuiho.info;

import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public interface MixinMethodInfo {
    /**
     * Get the MixinInfo
     *
     * @return {@link IMixinInfo} object.
     */
    IMixinInfo getMixinInfo();

    /**
     * Get the method's access flags (see {@link org.objectweb.asm.Opcodes Opcodes} access flag section).
     * This field also indicates if the method is synthetic and/or deprecated.
     *
     * @return The method's access flags.
     */
    int getAccess();

    /**
     * Get the method's name.
     *
     * @return The method's name.
     */
    String getName();

    /**
     * Get the method's descriptor (see {@link org.objectweb.asm.Type Type}).
     *
     * @return The method's descriptor.
     */
    String getDesc();

    /**
     * Checks whether the method contains the specified visible annotation.
     *
     * @param annotationDesc The description of the visible annotation to check for.
     * @return True if the method contains the specified visible annotation, false otherwise.
     */
    boolean containsVisibleAnnotation(String annotationDesc);

    /**
     * Checks whether the method contains the specified invisible annotation.
     *
     * @param annotationDesc The description of the invisible annotation to check for.
     * @return True if the method contains the specified invisible annotation, false otherwise.
     */
    boolean containsInVisibleAnnotation(String annotationDesc);

    /**
     * Checks whether the method contains the specified annotation, regardless of its visibility.
     *
     * @param annotationDesc The description of the annotation to check for.
     * @return True if the method contains the specified annotation (either visible or invisible),
     * false otherwise.
     */
    default boolean containsAnnotation(String annotationDesc) {
        return this.containsVisibleAnnotation(annotationDesc) || this.containsInVisibleAnnotation(annotationDesc);
    }
}
