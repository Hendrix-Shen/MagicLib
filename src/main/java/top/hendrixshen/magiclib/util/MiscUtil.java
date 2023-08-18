package top.hendrixshen.magiclib.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.function.BiConsumer;

public class MiscUtil {
    public static Gson GSON = new Gson();

    @SuppressWarnings("unchecked")
    public static <T> T cast(Object obj) {
        return (T) obj;
    }

    public static JsonObject readJson(@NotNull URL url) throws IOException {
        InputStream inputStream = url.openStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        JsonObject jsonObject = GSON.fromJson(inputStreamReader, JsonObject.class);
        inputStreamReader.close();
        inputStream.close();
        return jsonObject;
    }

    public static void loadStringMapFromJson(InputStream inputStream, BiConsumer<String, String> biConsumer) {
        JsonObject jsonObject = GSON.fromJson(new InputStreamReader(inputStream, StandardCharsets.UTF_8), JsonObject.class);

        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            biConsumer.accept(entry.getKey(), entry.getValue().getAsString());
        }
    }
}
