package bluestone.bluestone;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//imports to make the mod work
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

//import blocks
import bluestone.bluestone.block.*;

public class Bluestone implements ModInitializer {
	public static final String MOD_ID = "bluestone";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	//blocks added
	public static final Block SIGNED_REDSTONE_WIRE =
            new SignedRedstoneWireBlock(Block.Settings.create().noCollision().strength(0).nonOpaque());

    public static final Block BLUESTONE_TORCH =
            new BluestoneTorchBlock(Block.Settings.create().strength(0).luminance(state->7));


	@Override
	public void onInitialize() {
		//register blocks on bootup
		register("signed_redstone_wire", SIGNED_REDSTONE_WIRE, false);
        register("bluestone_torch", BLUESTONE_TORCH, true);
	}

	//function to register blocks
	private static void register(String name, Block block, boolean item) {
        Identifier id = Identifier.of(MOD_ID, name);
        Registry.register(Registries.BLOCK, id, block);
        if (item) Registry.register(Registries.ITEM, id, new BlockItem(block, new Item.Settings()));
    }
}
