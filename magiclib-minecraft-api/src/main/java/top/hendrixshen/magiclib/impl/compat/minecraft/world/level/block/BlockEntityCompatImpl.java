package top.hendrixshen.magiclib.impl.compat.minecraft.world.level.block;

import org.jetbrains.annotations.NotNull;

// CHECKSTYLE.OFF: ImportOrder
//#if MC >= 12106
//$$ import org.slf4j.Logger;
//#endif
// CHECKSTYLE.ON: ImportOrder

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;

// CHECKSTYLE.OFF: ImportOrder
//#if MC >= 12106
//$$ import com.mojang.logging.LogUtils;
//$$ import net.minecraft.core.RegistryAccess;
//$$ import net.minecraft.util.ProblemReporter;
//$$ import net.minecraft.world.level.storage.TagValueInput;
//$$ import net.minecraft.world.level.storage.ValueInput;
//#endif

//#if MC > 12004
//$$ import net.minecraft.core.HolderLookup;
//#endif
// CHECKSTYLE.ON: ImportOrder

import top.hendrixshen.magiclib.api.compat.AbstractCompat;
import top.hendrixshen.magiclib.api.compat.minecraft.world.level.block.BlockEntityCompat;

// CHECKSTYLE.OFF: ImportOrder
//#if 11700 > MC && MC > 11502
import java.util.Objects;
//#endif
// CHECKSTYLE.ON: ImportOrder

public class BlockEntityCompatImpl extends AbstractCompat<BlockEntity> implements BlockEntityCompat {
    //#if MC >= 12106
    //$$ private static final Logger LOGGER = LogUtils.getLogger();
    //#endif

    public BlockEntityCompatImpl(@NotNull BlockEntity type) {
        super(type);
    }

    @Override
    public void load(
            // CHECKSTYLE.OFF: NoWhitespaceBefore
            // CHECKSTYLE.OFF: SeparatorWrap
            @NotNull CompoundTag compoundTag
            //#if MC > 12004
            //$$ , HolderLookup.Provider provider
            //#endif
            // CHECKSTYLE.ON: SeparatorWrap
            // CHECKSTYLE.ON: NoWhitespaceBefore
    ) {
        //#if MC >= 12106
        //$$ BlockEntity self = this.get();
        //$$
        //$$ try (ProblemReporter.ScopedCollector collector = new ProblemReporter.ScopedCollector(self.problemPath(), BlockEntityCompatImpl.LOGGER)) {
        //$$     RegistryAccess access = self.getLevel().registryAccess();
        //$$     ValueInput input = TagValueInput.create(collector, access, compoundTag);
        //$$     self.loadWithComponents(input);
        //$$ } catch (Exception e) {
        //$$     BlockEntityCompatImpl.LOGGER.error("Failed to load block entity from CompoundTag", e);
        //$$ }
        //#elseif MC > 12004
        //$$ this.get().loadWithComponents(compoundTag, provider);
        //#elseif MC > 11605 || MC < 11600
        //$$ this.get().load(compoundTag);
        //#else
        BlockEntity blockEntity = this.get();
        blockEntity.load(Objects.requireNonNull(blockEntity.getLevel())
                .getBlockState(this.get().getBlockPos()), compoundTag);
        //#endif
    }
}
