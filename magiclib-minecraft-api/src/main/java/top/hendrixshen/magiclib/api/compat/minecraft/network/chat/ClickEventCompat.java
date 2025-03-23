package top.hendrixshen.magiclib.api.compat.minecraft.network.chat;

import org.jetbrains.annotations.NotNull;

// CHECKSTYLE.OFF: ImportOrder
//#if MC < 12105
import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;
//#endif
// CHECKSTYLE.OFF: ImportOrder

import net.minecraft.network.chat.ClickEvent;

// CHECKSTYLE.OFF: ImportOrder
//#if MC > 12104
//$$ import net.minecraft.network.chat.ClickEvent.ChangePage;
//$$ import net.minecraft.network.chat.ClickEvent.CopyToClipboard;
//$$ import net.minecraft.network.chat.ClickEvent.OpenFile;
//$$ import net.minecraft.network.chat.ClickEvent.OpenUrl;
//$$ import net.minecraft.network.chat.ClickEvent.RunCommand;
//$$ import net.minecraft.network.chat.ClickEvent.SuggestCommand;
//#else
import net.minecraft.network.chat.ClickEvent.Action;
//#endif
// CHECKSTYLE.OFF: ImportOrder

import top.hendrixshen.magiclib.impl.compat.minecraft.network.chat.ClickEventCompatImpl;
import top.hendrixshen.magiclib.util.collect.Provider;

// CHECKSTYLE.OFF: ImportOrder
//#if MC > 12104
//$$ import java.net.URI;
//#endif
// CHECKSTYLE.OFF: ImportOrder

public interface ClickEventCompat extends Provider<ClickEvent> {
    static @NotNull ClickEventCompat of(ClickEvent clickEvent) {
        return new ClickEventCompatImpl(clickEvent);
    }

    //#if MC < 12105
    @ScheduledForRemoval
    @Deprecated
    static @NotNull ClickEventCompat of(Action action, String string) {
        return ClickEventCompatImpl.of(action, string);
    }
    //#endif

    static @NotNull ClickEvent openUrl(String url) {
        //#if MC > 12104
        //$$ return new OpenUrl(URI.create(url));
        //#else
        return new ClickEvent(Action.OPEN_URL, url);
        //#endif
    }

    static @NotNull ClickEventCompat openUrlCompat(String url) {
        return ClickEventCompat.of(ClickEventCompat.openUrl(url));
    }

    static @NotNull ClickEvent openFile(String file) {
        //#if MC > 12104
        //$$ return new OpenFile(file);
        //#else
        return new ClickEvent(Action.OPEN_FILE, file);
        //#endif
    }

    static @NotNull ClickEventCompat openFileCompat(String file) {
        return ClickEventCompat.of(ClickEventCompat.openFile(file));
    }

    static @NotNull ClickEvent runCommand(String command) {
        //#if MC > 12104
        //$$ return new RunCommand(command);
        //#else
        return new ClickEvent(Action.RUN_COMMAND, command);
        //#endif
    }

    static @NotNull ClickEventCompat runCommandCompat(String command) {
        return ClickEventCompat.of(ClickEventCompat.runCommand(command));
    }

    static @NotNull ClickEvent suggestCommand(String command) {
        //#if MC > 12104
        //$$ return new SuggestCommand(command);
        //#else
        return new ClickEvent(Action.SUGGEST_COMMAND, command);
        //#endif
    }

    static @NotNull ClickEventCompat suggestCommandCompat(String command) {
        return ClickEventCompat.of(ClickEventCompat.suggestCommand(command));
    }

    static @NotNull ClickEvent changePage(String page) {
        //#if MC > 12104
        //$$ return new ChangePage(Integer.parseInt(page));
        //#else
        return new ClickEvent(Action.CHANGE_PAGE, page);
        //#endif
    }

    static @NotNull ClickEventCompat changePageCompat(String page) {
        return ClickEventCompat.of(ClickEventCompat.changePage(page));
    }

    //#if MC > 11404
    static @NotNull ClickEvent copyToClipboard(String text) {
        //#if MC > 12104
        //$$ return new CopyToClipboard(text);
        //#else
        return new ClickEvent(Action.COPY_TO_CLIPBOARD, text);
        //#endif
    }

    static @NotNull ClickEventCompat copyToClipboardCompat(String text) {
        return ClickEventCompat.of(ClickEventCompat.copyToClipboard(text));
    }
    //#endif
}
