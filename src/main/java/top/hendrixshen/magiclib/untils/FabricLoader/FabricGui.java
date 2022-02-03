package top.hendrixshen.magiclib.untils.fabricloader;

import net.fabricmc.loader.impl.gui.FabricGuiEntry;
import top.hendrixshen.magiclib.MagicLib;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@SuppressWarnings("unused")
public class FabricGui {
    // Fabric Loader 0.11 and below support
    private static Method legacyDisplayCriticalError;

    static {
        try {
            legacyDisplayCriticalError = Class.forName("net.fabricmc.loader.gui.FabricGuiEntry").getMethod("displayCriticalError", Throwable.class, boolean.class);
        } catch (ClassNotFoundException | NoSuchMethodException ignored) {
            // Ignored
        }
    }

    public static void displayCriticalError(Throwable exception) {
        if (legacyDisplayCriticalError != null) {
            try {
                legacyDisplayCriticalError.invoke(null, exception, true);
            } catch (IllegalAccessException | InvocationTargetException e) {
                MagicLib.getLogger().error(e);
            }
        } else {
            FabricGuiEntry.displayCriticalError(exception, true);
        }
    }
}
