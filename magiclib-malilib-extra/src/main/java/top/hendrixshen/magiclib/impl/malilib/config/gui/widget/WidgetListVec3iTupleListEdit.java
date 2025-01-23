package top.hendrixshen.magiclib.impl.malilib.config.gui.widget;

import fi.dy.masa.malilib.gui.widgets.WidgetListConfigOptionsBase;
import lombok.Getter;

import top.hendrixshen.magiclib.api.malilib.config.option.ConfigVec3iTupleList;
import top.hendrixshen.magiclib.impl.malilib.config.gui.GuiVec3iTupleListEdit;

@Getter
public class WidgetListVec3iTupleListEdit extends WidgetListConfigOptionsBase<ConfigVec3iTupleList.Entry, WidgetVec3iTupleListEditEntry> {
    protected final GuiVec3iTupleListEdit parent;

    public WidgetListVec3iTupleListEdit(int x, int y, int width, int height, int configWidth, GuiVec3iTupleListEdit parent) {
        super(x, y, width, height, configWidth);
        this.parent = parent;
        this.browserEntryHeight = 44;
    }

    @Override
    protected void refreshBrowserEntries() {
        this.listContents.clear();
        this.listContents.addAll(this.parent.getConfig().getVec3iTupleList());
        this.reCreateListEntryWidgets();
    }

    @Override
    protected void reCreateListEntryWidgets() {
        if (this.listContents.isEmpty()) {
            this.listWidgets.clear();
            this.maxVisibleBrowserEntries = 1;
            int x = this.posX + 2;
            int y = this.posY + 4 + this.browserEntriesOffsetY;
            this.listWidgets.add(this.createListEntryWidget(x, y, -1, false, ConfigVec3iTupleList.Entry.ZERO));
            this.scrollBar.setMaxValue(0);
        } else {
            super.reCreateListEntryWidgets();
        }
    }

    @Override
    protected WidgetVec3iTupleListEditEntry createListEntryWidget(int x, int y, int listIndex, boolean isOdd, ConfigVec3iTupleList.Entry entry) {
        ConfigVec3iTupleList config = this.parent.getConfig();

        if (listIndex >= 0 && listIndex < config.getVec3iTupleList().size()) {
            ConfigVec3iTupleList.Entry defaultValue = config.getDefaultVec3iTupleList().size() > listIndex
                    ? config.getDefaultVec3iTupleList().get(listIndex) : ConfigVec3iTupleList.Entry.ZERO;
            return new WidgetVec3iTupleListEditEntry(x, y, this.browserEntryWidth, this.browserEntryHeight, listIndex,
                    isOdd, config.getVec3iTupleList().get(listIndex), defaultValue, this);
        } else {
            return new WidgetVec3iTupleListEditEntry(x, y, this.browserEntryWidth, this.browserEntryHeight, listIndex,
                    isOdd, ConfigVec3iTupleList.Entry.ZERO, ConfigVec3iTupleList.Entry.ZERO, this);
        }
    }
}
