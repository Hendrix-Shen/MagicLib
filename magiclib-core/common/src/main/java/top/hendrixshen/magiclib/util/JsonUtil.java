package top.hendrixshen.magiclib.util;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.function.BiConsumer;

public class JsonUtil {
    public static void loadStringMapFromJson(InputStream inputStream, BiConsumer<String, String> biConsumer) {
        JsonUtil.loadLanguageMapFromJson(inputStream, biConsumer);
    }

    public static void loadLanguageMapFromJson(InputStream inputStream, BiConsumer<String, String> biConsumer) {
        try (
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                JsonReader reader = new JsonReader(inputStreamReader)
        ) {
            if (reader.peek() == JsonToken.BEGIN_OBJECT) {
                reader.beginObject();

                while (reader.hasNext()) {
                    String key = reader.nextName();

                    if (reader.peek() == JsonToken.STRING) {
                        biConsumer.accept(key, reader.nextString());
                    } else {
                        reader.skipValue();
                    }
                }

                reader.endObject();
            }
        } catch (IOException ignore) {
            // ignore.
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
