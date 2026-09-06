package top.hendrixshen.magiclib.util.collect;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

/**
 * A node of a tree used to render nested text messages, e.g. the dependency check failure tree.
 *
 * <p>
 * A node carries a text name and an ordered list of children. Creating a node with a non-null parent
 * automatically attaches it to that parent, and {@link InfoNode#moveTo(InfoNode)} re-attaches a node to
 * another parent. The tree is rendered by {@link InfoNode#toString()} as one line per node, indented by one
 * tab per depth level.
 * </p>
 */
public class InfoNode {
    @Getter
    @Setter
    private String name;
    @Getter
    private InfoNode parent;
    private final List<InfoNode> children = Lists.newArrayList();

    /**
     * Creates a node and attaches it to the given parent.
     *
     * <p>
     * When {@code parent} is not null, this node is appended to the parent's children immediately. The name
     * must not be null; an empty name renders no visible text on its own line.
     * </p>
     *
     * @param parent The parent node, or {@code null} to create a root node.
     * @param name The text of this node.
     */
    public InfoNode(@Nullable InfoNode parent, String name) {
        this.name = Objects.requireNonNull(name, "name");
        this.parent = parent;

        if (parent != null) {
            parent.addChild(this);
        }
    }

    /**
     * Re-attaches this node to the given parent.
     *
     * <p>
     * This node is removed from its current parent's children, if any, and then appended to the new
     * parent's children. Passing {@code null} detaches this node into a root.
     * </p>
     *
     * @param newParent The new parent node, or {@code null} to detach this node.
     */
    public void moveTo(@Nullable InfoNode newParent) {
        if (this.parent != null) {
            this.parent.children.remove(this);
        }

        this.parent = newParent;

        if (newParent != null) {
            newParent.addChild(this);
        }
    }

    /**
     * Gets a snapshot of this node's children.
     *
     * <p>
     * The returned list is a defensive copy rather than a live view, so the tree can be modified safely
     * while iterating the snapshot. This is required by the re-attaching flow that moves the root children
     * under a composite node.
     * </p>
     *
     * @return The children of this node.
     */
    public @NotNull ImmutableList<InfoNode> getChildren() {
        return ImmutableList.copyOf(this.children);
    }

    private void addChild(InfoNode infoNode) {
        this.children.add(infoNode);
    }

    private void appendTo(@NotNull StringBuilder builder, @NotNull String prefix) {
        builder.append(prefix).append(this.name).append('\n');
        String childPrefix = prefix + "\t";

        for (InfoNode child : this.children) {
            child.appendTo(builder, childPrefix);
        }
    }

    @Override
    public @NotNull String toString() {
        StringBuilder builder = new StringBuilder();
        this.appendTo(builder, "");

        while (builder.length() > 0 && builder.charAt(builder.length() - 1) == '\n') {
            builder.setLength(builder.length() - 1);
        }

        return builder.toString();
    }
}
