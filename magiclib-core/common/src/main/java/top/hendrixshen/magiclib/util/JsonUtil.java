package top.hendrixshen.magiclib.util;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.function.BiConsumer;

public class JsonUtil {
    public static void loadStringMapFromJson(InputStream inputStream, BiConsumer<String, String> biConsumer) {
        JsonObject jsonObject = GsonUtil.GSON.fromJson(new InputStreamReader(inputStream,
                StandardCharsets.UTF_8), JsonObject.class);
        jsonObject.entrySet().forEach(entry -> biConsumer.accept(entry.getKey(),
                entry.getValue().getAsString()));
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
