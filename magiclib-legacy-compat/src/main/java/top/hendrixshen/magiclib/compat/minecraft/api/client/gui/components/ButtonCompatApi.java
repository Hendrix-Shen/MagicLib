package top.hendrixshen.magiclib.compat.minecraft.api.client.gui.components;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@Deprecated
@ApiStatus.ScheduledForRemoval
@Environment(EnvType.CLIENT)
public interface ButtonCompatApi {
    @Contract(value = "_, _ -> new", pure = true)
    static @NotNull BuilderCompatApi builder(Component component, OnPressCompat onPress) {
        return new BuilderCompatApi(component, onPress);
    }

    class BuilderCompatApi {
        //#if MC > 11902
        //$$ private final Button.Builder builder;
        //#else
        private final Component message;
        private final OnPressCompat onPress;
        private int x;
        private int y;
        private int width = 150;
        private int height = 20;
        //#endif

        @Contract(pure = true)
        public BuilderCompatApi(Component message, @NotNull OnPressCompat onPress) {
            //#if MC > 11902
            //$$ this.builder = new Button.Builder(message, onPress::onPress);
            //#else
            this.message = message;
            this.onPress = onPress;
            //#endif
        }

        public BuilderCompatApi pos(int x, int y) {
            //#if MC > 11902
            //$$ this.builder.pos(x, y);
            //#else
            this.x = x;
            this.y = y;
            //#endif
            return this;
        }

        public BuilderCompatApi width(int width) {
            //#if MC > 11902
            //$$ this.builder.width(width);
            //#else
            this.width = width;
            //#endif
            return this;
        }

        public BuilderCompatApi size(int width, int height) {
            //#if MC > 11902
            //$$ this.builder.size(width, height);
            //#else
            this.width = width;
            this.height = height;
            //#endif
            return this;
        }

        public BuilderCompatApi bounds(int x, int y, int width, int height) {
            return this.pos(x, y).size(width, height);
        }

        public Button build() {
            //#if MC > 11902
            //$$ return this.builder.build();
            //#elseif MC > 11502
            return new Button(this.x, this.y, this.width, this.height, this.message, this.onPress::onPress);
            //#else
            //$$ return new Button(this.x, this.y, this.width, this.height, this.message.getString(), this.onPress::onPress);
            //#endif
        }
    }

    @FunctionalInterface
    interface OnPressCompat {
        void onPress(Button button);
    }
}
