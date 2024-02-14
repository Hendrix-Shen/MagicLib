package top.hendrixshen.magiclib.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtil {
    public static final Gson GSON = new GsonBuilder().enableComplexMapKeySerialization().create();
}
