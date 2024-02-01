package top.hendrixshen.magiclib.api.mixin.extension.jikuTsuiho.info;

import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public interface MixinFieldInfo {
    /**
     * Get the MixinInfo
     *
     * @return {@link IMixinInfo} object.
     */
    IMixinInfo getMixinInfo();

    /**
     * Get the field's access flags (see {@link org.objectweb.asm.Opcodes Opcodes} access flags section).
     * This field also indicates if the field is synthetic and/or deprecated.
     *
     * @return The access flags of the class.
     */
    int getAccess();

    /**
     * Get field's name.
     *
     * @return Field's name.
     */
    String getName();

    /**
     * Get the field's descriptor (see {@link org.objectweb.asm.Type Type}).
     *
     * @return The field's descriptor.
     */
    String getDesc();
}
