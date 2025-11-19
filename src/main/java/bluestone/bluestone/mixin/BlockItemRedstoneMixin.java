package bluestone.bluestone.mixin;

import bluestone.bluestone.Bluestone; // your mod class with SIGNED_REDSTONE_WIRE
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public abstract class BlockItemRedstoneMixin {

    /**
     * Intercepts placement of redstone dust and replaces it with our signed wire block.
     */
    @Inject(method = "place", at = @At("HEAD"), cancellable = true)
    private void bluestone$placeSignedWire(ItemPlacementContext context,
                                           CallbackInfoReturnable<ActionResult> cir) {

        BlockItem self = (BlockItem)(Object)this;

        // Only intercept redstone dust
        if (self.getBlock() != Blocks.REDSTONE_WIRE) {
            return;
        }

        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        BlockState target = world.getBlockState(pos);

        // Vanilla behavior: place in the clicked block if replaceable, otherwise offset
        if (!target.canReplace(context)) {
            pos = pos.offset(context.getSide());
            target = world.getBlockState(pos);
        }

        if (!target.canReplace(context)) {
            cir.setReturnValue(ActionResult.FAIL);
            return;
        }

        BlockState newWire = Bluestone.SIGNED_REDSTONE_WIRE.getDefaultState();

        if (!world.isClient()) {
            world.setBlockState(pos, newWire);

            if (context.getPlayer() != null && !context.getPlayer().getAbilities().creativeMode) {
                context.getStack().decrement(1);
            }
        }

        cir.setReturnValue(ActionResult.SUCCESS);
    }
}
