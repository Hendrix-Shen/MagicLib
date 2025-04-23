package top.hendrixshen.magiclib.api.malilib.config;

import com.google.gson.JsonObject;
import fi.dy.masa.malilib.config.IConfigHandler;

import top.hendrixshen.magiclib.util.serializable.JsonSaveAble;

import java.io.File;
import java.util.function.Consumer;

public interface MagicConfigHandler extends IConfigHandler {
    /**
     * Registers external JSON-serializable data to the configuration handler.
     *
     * @param namespace The identifier for the data.
     * @param data      Data object implementing JsonSaveAble interface.
     * @return true if registration succeeded, false otherwise.
     */
    boolean registerExternalData(String namespace, JsonSaveAble data);

    /**
     * Unregisters previously registered external data.
     *
     * @param namespace Namespace of the data to remove.
     * @return true if un-registration succeeded, false otherwise.
     */
    boolean unregisterExternalData(String namespace);

    /**
     * Retrieves the configuration manager instance.
     *
     * @return Associated MagicConfigManager instance
     */
    MagicConfigManager getConfigManager();

    /**
     * Gets the identifier for this configuration handler.
     *
     * @return Handler identifier.
     */
    String getIdentifier();

    /**
     * Obtains the configuration file reference.
     *
     * @return File object representing the configuration file
     */
    File getConfigFile();

    /**
     * Gets the JSON object containing the loaded configuration data.
     * @return JSON object.
     */
    JsonObject getLoadedJson();

    /**
     * Gets the version of this configuration handler.
     *
     * @return Handler version number
     */
    int getHandlerVersion();

    /**
     * Gets the version of the configuration schema.
     *
     * @return Configuration version number
     */
    int getConfigVersion();

    /**
     * Sets callback to execute before deserialization.
     *
     * @param preDeserializeCallback Callback accepting MagicConfigHandler
     */
    void setPreDeserializeCallback(Consumer<MagicConfigHandler> preDeserializeCallback);

    /**
     * Sets callback to execute after deserialization.
     *
     * @param postDeserializeCallback Callback accepting MagicConfigHandler
     */
    void setPostDeserializeCallback(Consumer<MagicConfigHandler> postDeserializeCallback);

    /**
     * Sets callback to execute before serialization.
     *
     * @param preSerializeCallback Callback accepting MagicConfigHandler
     */
    void setPreSerializeCallback(Consumer<MagicConfigHandler> preSerializeCallback);

    /**
     * Sets callback to execute after serialization.
     *
     * @param postSerializeCallback Callback accepting MagicConfigHandler
     */
    void setPostSerializeCallback(Consumer<MagicConfigHandler> postSerializeCallback);
}
