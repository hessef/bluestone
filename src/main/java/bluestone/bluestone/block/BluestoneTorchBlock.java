package bluestone.bluestone.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneTorchBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

/**
 * Torch that outputs a constant -15 signed power in all directions,
 * but appears as 15 power to vanilla devices.
 */
public class BluestoneTorchBlock extends RedstoneTorchBlock implements SignedSignalSource {

    public BluestoneTorchBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    // ===== SignedSignalSource implementation =====

    @Override
    public int getSignedPower(BlockState state, BlockView world,
                              BlockPos pos, Direction toDirection) {
        // Always -15, regardless of direction, for now.
        return -15;
    }

    // ===== Vanilla redstone power (magnitude only) =====

    @Override
    public boolean emitsRedstonePower(BlockState state) {
        return true;
    }

    @Override
    public int getWeakRedstonePower(BlockState state, BlockView world,
                                    BlockPos pos, Direction direction) {
        return 15; // vanilla sees magnitude 15
    }

    @Override
    public int getStrongRedstonePower(BlockState state, BlockView world,
                                      BlockPos pos, Direction direction) {
        return 15;
    }
}