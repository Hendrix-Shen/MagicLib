package top.hendrixshen.magiclib.util;

import com.google.common.collect.Queues;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.MagicLibReference;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.BiConsumer;
import java.util.regex.Pattern;

public class MiscUtil {
    private static final LinkedBlockingQueue<String> deprecatedFeatureCache = Queues.newLinkedBlockingQueue();
    private static final Pattern MAGICLIB_PACKAGE_PATTERN = Pattern.compile("^top\\.hendrixshen\\.magiclib\\S+");

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

    @ApiStatus.Internal
    public static void warnDeprecatedFeature(String inVersion) {
        MiscUtil.warnDeprecatedFeature(inVersion, null);
    }

    @ApiStatus.Internal
    public static void warnDeprecatedFeature(String inVersion, String identifier) {
        // Caller also treat as identifier.
        if (identifier == null) {
            identifier = "(Unknown Source)";
            StackTraceElement[] elements = (new Throwable()).getStackTrace();

            for (StackTraceElement element : elements) {
                if (!MiscUtil.MAGICLIB_PACKAGE_PATTERN.matcher(element.getClassName()).matches()) {
                    identifier = element.toString();
                    break;
                }
            }
        }

        if (!MiscUtil.deprecatedFeatureCache.contains(identifier)) {
            MiscUtil.deprecatedFeatureCache.add(identifier);
            MagicLibReference.getLogger().warn("Deprecated MagicLib features were used in {}, making it incompatible with MagicLib {}.", identifier, inVersion);
        }
    }
}
