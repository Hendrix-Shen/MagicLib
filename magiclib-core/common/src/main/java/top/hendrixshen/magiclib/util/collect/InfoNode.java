package top.hendrixshen.magiclib.util.collect;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class InfoNode {
    @Getter
    @Setter
    private String name;
    @Getter
    private InfoNode parent;
    private final List<InfoNode> children = Lists.newArrayList();

    public InfoNode(InfoNode parent, String name) {
        Objects.requireNonNull(name, "Null name");

        if (parent != null) {
            parent.addChild(this);
        }

        this.parent = parent;
        this.name = name;
    }

    public void moveTo(InfoNode newParent) {
        if (this.parent != null) {
            this.parent.children.remove(this);
        }

        this.parent = newParent;

        if (newParent != null) {
            newParent.addChild(this);
        }
    }

    private void addChild(InfoNode infoNode) {
        this.children.add(infoNode);
    }

    private @NotNull String getString(String prefix) {
        return prefix + this.name + "\n" + this.children.stream()
                .map(n -> n.getString(prefix + "\t"))
                .collect(Collectors.joining());
    }

    public @NotNull ImmutableList<InfoNode> getChildren() {
        return ImmutableList.copyOf(this.children);
    }

    @Override
    public @NotNull String toString() {
        return this.getString("").replaceAll("\n*$", "");
    }
}
