package top.hendrixshen.magiclib.compat.minecraft.world.item;

//#if MC <= 11605
//$$ import net.minecraft.world.item.Item;
//#endif

public interface ItemStackCompatApi {
    //#if MC <= 11605
    //$$ default boolean is(Item item) {
    //$$     throw new UnsupportedOperationException();
    //$$ }
    //#endif
}
