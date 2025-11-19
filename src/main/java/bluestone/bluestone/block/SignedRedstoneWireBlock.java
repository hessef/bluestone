package bluestone.bluestone.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.random.Random;

/**
 * Wire that carries a signed signal internally (-15..15),
 * but reports magnitude (0..15) to vanilla redstone.
 */
public class SignedRedstoneWireBlock extends Block {

    // Standard 0..15 power property
    public static final IntProperty POWER = Properties.POWER;

    // Whether the signal is negative (true) or positive (false)
    public static final BooleanProperty NEGATIVE = BooleanProperty.of("negative");

    public SignedRedstoneWireBlock(Settings settings) {
        super(settings);
        this.setDefaultState(
            this.getStateManager().getDefaultState()
                .with(POWER, 0)
                .with(NEGATIVE, false)
        );
    }

    // ========== Signed helpers ==========

    /** Get signed power in range -15..15 from the block state. */
    public static int getSignedPower(BlockState state) {
        int mag = state.get(POWER);
        if (mag == 0) return 0;
        return state.get(NEGATIVE) ? -mag : mag;
    }

    /** Return a new state with given signed power (-15..15). */
    public static BlockState withSignedPower(BlockState state, int signed) {
        if (signed > 15) signed = 15;
        if (signed < -15) signed = -15;

        if (signed == 0) {
            return state.with(POWER, 0).with(NEGATIVE, false);
        }

        boolean neg = signed < 0;
        int mag = Math.abs(signed);
        return state.with(POWER, mag).with(NEGATIVE, neg);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(POWER, NEGATIVE);
    }

    // ========== Vanilla redstone integration ==========

    @Override
    public boolean emitsRedstonePower(BlockState state) {
        return true;
    }

    /** Vanilla only sees the magnitude of the signed power. */
    @Override
    public int getWeakRedstonePower(BlockState state, BlockView world,
                                    BlockPos pos, Direction direction) {
        return Math.abs(getSignedPower(state));
    }

    @Override
    public int getStrongRedstonePower(BlockState state, BlockView world,
                                      BlockPos pos, Direction direction) {
        return getWeakRedstonePower(state, world, pos, direction);
    }

    // ========== Update logic ==========

    /** On tick, recalculate our signed power from neighbors. */
    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (world.isClient()) return;

        int oldSigned = getSignedPower(state);
        int newSigned = calculateNewSignedPower(world, pos);

        if (oldSigned != newSigned) {
            BlockState newState = withSignedPower(state, newSigned);
            world.setBlockState(pos, newState, Block.NOTIFY_ALL);

        }
    }

    /**
     * Very simple propagation rule:
     * - Look at all 6 neighbors.
     * - If neighbor is another wire: take its signed value - 1 (signal falloff).
     * - If neighbor is a SignedSignalSource: take its signed value.
     * - Choose the neighbor value with largest absolute value.
     */
    private int calculateNewSignedPower(World world, BlockPos pos) {
        int best = 0;

        for (Direction dir : Direction.values()) {
            BlockPos neighborPos = pos.offset(dir);
            BlockState neighborState = world.getBlockState(neighborPos);
            Block neighborBlock = neighborState.getBlock();

            // Neighbor is another signed wire
            if (neighborBlock instanceof SignedRedstoneWireBlock) {
                int p = SignedRedstoneWireBlock.getSignedPower(neighborState) - 1;
                if (Math.abs(p) > Math.abs(best)) best = p;
            }

            // Neighbor is a custom signed source (torch, transistor, etc.)
            if (neighborBlock instanceof SignedSignalSource source) {
                int p = source.getSignedPower(neighborState, world, neighborPos, dir.getOpposite());
                if (Math.abs(p) > Math.abs(best)) best = p;
            }
        }

        // Clamp
        if (best > 15) best = 15;
        if (best < -15) best = -15;
        return best;
    }
}