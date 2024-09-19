package top.hendrixshen.magiclib.impl.malilib.config.gui.button;

import fi.dy.masa.malilib.gui.MaLiLibIcons;
import lombok.AllArgsConstructor;
import lombok.Getter;
import top.hendrixshen.magiclib.api.i18n.I18n;

@AllArgsConstructor
public enum ListEditEntryButtonType {
    ADD(MaLiLibIcons.PLUS, "magiclib.config.gui.button.hover_text.add"),
    REMOVE(MaLiLibIcons.MINUS, "magiclib.config.gui.button.hover_text.remove"),
    MOVE_UP(MaLiLibIcons.ARROW_UP, "magiclib.config.gui.button.hover_text.move_up"),
    MOVE_DOWN(MaLiLibIcons.ARROW_DOWN, "magiclib.config.gui.button.hover_text.move_down");

    @Getter
    private final MaLiLibIcons icon;
    private final String hoverTextTrKey;

    public String getDisplayName() {
        return I18n.tr(this.hoverTextTrKey);
    }
}
