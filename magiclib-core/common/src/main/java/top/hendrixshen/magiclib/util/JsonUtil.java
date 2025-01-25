package top.hendrixshen.magiclib.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map.Entry;
import java.util.function.BiConsumer;

public class JsonUtil {
    public static void loadStringMapFromJson(InputStream inputStream, BiConsumer<String, String> biConsumer) {
        JsonUtil.loadStringMapFromJson(inputStream, biConsumer, false);
    }

    public static void loadStringMapFromJson(InputStream inputStream, BiConsumer<String, String> biConsumer,
                                             boolean failSoft) {
        try {
            JsonObject jsonObject = GsonUtil.GSON.fromJson(new InputStreamReader(inputStream, StandardCharsets.UTF_8),
                    JsonObject.class);

            for (Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                JsonElement element = entry.getValue();

                if (element.isJsonPrimitive()) {
                    biConsumer.accept(entry.getKey(), element.getAsString());
                } else if (!failSoft) {
                    throw new JsonSyntaxException("Expected string value for " + entry.getKey() + " but got " + element);
                }
            }
        } catch (JsonSyntaxException e) {
            if (!failSoft) {
                throw e;
            }
        }
    }

    public static JsonObject readJson(@NotNull URL url) throws IOException {
        InputStream inputStream = url.openStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        JsonObject jsonObject = GsonUtil.GSON.fromJson(inputStreamReader, JsonObject.class);
        inputStreamReader.close();
        inputStream.close();
        return jsonObject;
    }
}
