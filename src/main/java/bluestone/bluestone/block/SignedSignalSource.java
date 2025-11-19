package bluestone.bluestone.block;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

/**
 * For blocks that output a signed redstone-like signal (-15..15).
 */
public interface SignedSignalSource {
    /**
     * @param state      BlockState of this block
     * @param world      world view
     * @param pos        position of this block
     * @param toDirection direction TOWARD the receiver (the block that is reading this)
     * @return signed power level in range -15..15
     */
    int getSignedPower(BlockState state, BlockView world, BlockPos pos, Direction toDirection);
}