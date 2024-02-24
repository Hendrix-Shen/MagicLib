package top.hendrixshen.magiclib.api.platform.adapter;

public interface ModMetaDataAdapter {
    /**
     * Returns the mod's ID.
     *
     * <p>
     * A mod's id must have only lowercase letters, digits, {@code -}(Fabric Only), or {@code _}.
     *
     * @return The mod's ID.
     */
    String getModId();

    /**
     * Returns the mod's version.
     */
    String getVersion();

    /**
     * Returns the mod's display name.
     */
    String getName();

    /**
     * Returns the mod's description.
     */
    String getDescription();
}
