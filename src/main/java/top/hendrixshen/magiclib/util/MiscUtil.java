package top.hendrixshen.magiclib.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class MiscUtil {

    public static JsonObject readJson(URL url) throws IOException {
        InputStream inputStream = url.openStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        //#if MC > 11701
        JsonObject jsonObject = JsonParser.parseReader(inputStreamReader).getAsJsonObject();
        //#else
        //$$ JsonObject jsonObject = new JsonParser().parse(inputStreamReader).getAsJsonObject();
        //#endif
        inputStreamReader.close();
        inputStream.close();
        return jsonObject;
    }
}
