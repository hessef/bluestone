package bluestone.bluestone;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import bluestone.bluestone.block.SignedRedstoneWireBlock;

public class BluestoneClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {

		//change color of redstone to blue if it has negative polarity
		ColorProviderRegistry.BLOCK.register(
			(state, world, pos, tint) -> {
				int signed = SignedRedstoneWireBlock.getSignedPower(state);
				int mag = Math.abs(signed);
				float f = mag / 15f;

				if (signed < 0) return ((int)(30*f) << 16) | ((int)(90*f)<<8) | (255);
				return ((int)(255*f) << 16) | ((int)(60*f)<<8) | ((int)(60*f));
			},
			bluestone.bluestone.Bluestone.SIGNED_REDSTONE_WIRE
		);			
	}
}
