package bluestone.bluestone;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.render.BlockRenderLayer;
import bluestone.bluestone.block.SignedRedstoneWireBlock;

public class BluestoneClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		//render wire like vanilla redstone
		BlockRenderLayerMap.putBlock(Bluestone.SIGNED_REDSTONE_WIRE, BlockRenderLayer.CUTOUT);
		
		//change color of redstone to blue if it has negative polarity
		ColorProviderRegistry.BLOCK.register(
                (state, world, pos, tintIndex) -> {
                    if (state == null) return 0xFFFFFF;

                    int mag = state.get(SignedRedstoneWireBlock.POWER);
                    boolean negative = state.get(SignedRedstoneWireBlock.NEGATIVE);

                    if (mag == 0) {
                        // low glow when unpowered
                        return negative ? 0x202040 : 0x202020;
                    }

                    float f = mag / 15.0f;

                    if (negative) {
                        int r = (int)(40 * f);
                        int g = (int)(100 * f);
                        int b = (int)(255 * f);
                        return (r << 16) | (g << 8) | b;
                    } else {
                        int r = (int)(255 * f);
                        int g = (int)(60 * f);
                        int b = (int)(60 * f);
                        return (r << 16) | (g << 8) | b;
                    }
                },
                Bluestone.SIGNED_REDSTONE_WIRE
		);

	}
}
