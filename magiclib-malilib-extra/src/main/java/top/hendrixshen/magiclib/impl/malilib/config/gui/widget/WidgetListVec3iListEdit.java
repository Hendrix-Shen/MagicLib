package top.hendrixshen.magiclib.impl.malilib.config.gui.widget;

import fi.dy.masa.malilib.gui.widgets.WidgetListConfigOptionsBase;
import lombok.Getter;
import net.minecraft.core.Vec3i;
import top.hendrixshen.magiclib.api.malilib.config.option.ConfigVec3iList;
import top.hendrixshen.magiclib.impl.malilib.config.gui.GuiVec3iListEdit;

@Getter
public class WidgetListVec3iListEdit extends WidgetListConfigOptionsBase<Vec3i, WidgetVec3iListEditEntry> {
    protected final GuiVec3iListEdit parent;

    public WidgetListVec3iListEdit(int x, int y, int width, int height, int configWidth, GuiVec3iListEdit parent) {
        super(x, y, width, height, configWidth);
        this.parent = parent;
    }

    @Override
    protected void refreshBrowserEntries() {
        this.listContents.clear();
        this.listContents.addAll(this.parent.getConfig().getVec3iList());
        this.reCreateListEntryWidgets();
    }

    @Override
    protected void reCreateListEntryWidgets() {
        if (this.listContents.isEmpty()) {
            this.listWidgets.clear();
            this.maxVisibleBrowserEntries = 1;
            int x = this.posX + 2;
            int y = this.posY + 4 + this.browserEntriesOffsetY;
            this.listWidgets.add(this.createListEntryWidget(x, y, -1, false, Vec3i.ZERO));
            this.scrollBar.setMaxValue(0);
        } else {
            super.reCreateListEntryWidgets();
        }
    }

    @Override
    protected WidgetVec3iListEditEntry createListEntryWidget(int x, int y, int listIndex, boolean isOdd, Vec3i entry) {
        ConfigVec3iList config = this.parent.getConfig();

        if (listIndex >= 0 && listIndex < config.getVec3iList().size()) {
            Vec3i defaultValue = config.getDefaultVec3iList().size() > listIndex ? config.getDefaultVec3iList().get(listIndex) : Vec3i.ZERO;
            return new WidgetVec3iListEditEntry(x, y, this.browserEntryWidth, this.browserEntryHeight, listIndex, isOdd, config.getVec3iList().get(listIndex), defaultValue, this);
        } else {
            return new WidgetVec3iListEditEntry(x, y, this.browserEntryWidth, this.browserEntryHeight, listIndex, isOdd, Vec3i.ZERO, Vec3i.ZERO, this);
        }
    }
}
