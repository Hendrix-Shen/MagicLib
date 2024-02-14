package top.hendrixshen.magiclib.util;

import com.google.gson.JsonObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.function.BiConsumer;

public class JsonUtil {
    public static void loadStringMapFromJson(InputStream inputStream, BiConsumer<String, String> biConsumer) {
        JsonObject jsonObject = GsonUtil.GSON.fromJson(new InputStreamReader(inputStream,
                StandardCharsets.UTF_8), JsonObject.class);
        jsonObject.entrySet().forEach(entry -> biConsumer.accept(entry.getKey(),
                entry.getValue().getAsString()));
    }
}
