package top.hendrixshen.magiclib.impl.malilib.config.statistic;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.api.i18n.minecraft.I18n;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

/**
 * Reference to <a href="https://github.com/Fallen-Breath/TweakerMore">TweakerMore</a>
 */
public class ConfigStatistic {
    private static final int USE_AMOUNT_INCREASE_COOLDOWN = 1000;  // ms
    public long lastUsedTime;
    public long useAmount;

    public ConfigStatistic() {
        this.reset();
    }

    public void reset() {
        this.lastUsedTime = 0;
        this.useAmount = 0;
    }

    public void loadFromJson(JsonElement jsonElement) {
        try {
            if (jsonElement.isJsonObject()) {
                JsonObject obj = jsonElement.getAsJsonObject();
                this.lastUsedTime = obj.get("lastUsedTime").getAsLong();
                this.useAmount = obj.get("useAmount").getAsLong();
            }
        } catch (Exception e) {
            MagicLib.getLogger().warn("Failed to load OptionStatistic from json '{}'", jsonElement);
        }
    }

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("lastUsedTime", this.lastUsedTime);
        obj.addProperty("useAmount", this.useAmount);
        return obj;
    }

    public void onConfigUsed() {
        long currentTime = System.currentTimeMillis();

        if (currentTime - this.lastUsedTime > ConfigStatistic.USE_AMOUNT_INCREASE_COOLDOWN) {
            this.useAmount += 1;
        }

        this.lastUsedTime = currentTime;
    }

    public List<String> getDisplayLines() {
        Function<String, String> tr = name -> I18n.tr("magiclib.config.gui.statistic.entry." + name);

        String lastUsedTimeText = this.lastUsedTime <= 0 ?
                "N/A" : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(this.lastUsedTime));

        return Lists.newArrayList(
                String.format("%s: %s", tr.apply("lastUsedTime"), lastUsedTimeText),
                String.format("%s: %d", tr.apply("useAmount"), this.useAmount)
        );
    }
}
