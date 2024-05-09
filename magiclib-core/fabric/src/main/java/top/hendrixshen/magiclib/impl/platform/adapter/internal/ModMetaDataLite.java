package top.hendrixshen.magiclib.impl.platform.adapter.internal;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import lombok.Getter;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.util.FileUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Map;

@ApiStatus.Internal
public class ModMetaDataLite {
    private static final Map<String, MetaData> data = Maps.newHashMap();

    public static MetaData getMetaData(String modIdentifier) {
        return ModMetaDataLite.data.get(modIdentifier);
    }

    static {
        try {
            for (URL url : FileUtil.getResources("fabric.mod.json")) {
                InputStream inputStream = url.openStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                JsonReader jsonReader = new JsonReader(inputStreamReader);

                try {
                    MetaData modMetaData = new MetaData(jsonReader);
                    ModMetaDataLite.data.put(modMetaData.getModIdentifier(), modMetaData);
                } catch (Throwable e) {
                    MagicLib.getLogger().debug("Failed to parse {}.", url, e);
                }
            }
        } catch (IOException e) {
            MagicLib.getLogger().error("Failed to load fabric.mod.json", e);
        }
    }

    @Getter
    public static class MetaData {
        private String modIdentifier;
        private final Map<String, HashSet<String>> entrypoints;

        MetaData(@NotNull JsonReader reader) throws IOException {
            this.entrypoints = Maps.newHashMap();
            reader.beginObject();

            while (reader.hasNext()) {
                String key = reader.nextName();

                switch (key) {
                    case "id":
                        this.modIdentifier = reader.nextString();
                        break;

                    case "entrypoints":
                        reader.beginObject();

                        while (reader.hasNext()) {
                            final String entryKey = reader.nextName();
                            reader.beginArray();

                            while (reader.hasNext()) {
                                if (reader.peek() == JsonToken.STRING) {
                                    this.entrypoints.computeIfAbsent(entryKey, k -> Sets.newHashSet()).add(reader.nextString());
                                } else {
                                    reader.skipValue();
                                }
                            }

                            reader.endArray();
                        }

                        reader.endObject();
                        break;
                    default:
                        reader.skipValue();
                }
            }

            reader.endObject();
        }
    }
}
