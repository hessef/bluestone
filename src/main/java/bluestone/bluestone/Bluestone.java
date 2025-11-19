package bluestone.bluestone;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.block.AbstractBlock;

//imports from minecraft
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

//imports from java
import java.util.function.Function;

//import blocks
import bluestone.bluestone.block.*;

public class Bluestone implements ModInitializer {
	public static final String MOD_ID = "bluestone";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	//blocks added
	public static Block SIGNED_REDSTONE_WIRE;
    public static Block BLUESTONE_TORCH;


	@Override
	public void onInitialize() {
		
		// Register blocks using the 1.21.2+ pattern with registry keys in Settings
        SIGNED_REDSTONE_WIRE = registerBlock(
                "signed_redstone_wire",
                s -> new SignedRedstoneWireBlock(s),
                AbstractBlock.Settings.copy(Blocks.REDSTONE_WIRE)
        );

        BLUESTONE_TORCH = registerBlock(
                "bluestone_torch",
                s -> new BluestoneTorchBlock(s),
                AbstractBlock.Settings.copy(Blocks.REDSTONE_TORCH)
        );
    }

    /**
     * 1.21.2+ recommended way:
     * - create a RegistryKey<Block>
     * - write that key into AbstractBlock.Settings
     * - Blocks.register(...) both sets the key and registers the block
     * - Items.register(block) creates and registers a BlockItem automatically
     */
    private static Block registerBlock(
            String path,
            Function<AbstractBlock.Settings, Block> factory,
            AbstractBlock.Settings settings
    ) {
        Identifier id = Identifier.of(MOD_ID, path);
        RegistryKey<Block> key = RegistryKey.of(RegistryKeys.BLOCK, id);

        // Blocks.register will set the registry key into settings and create/register the block
        Block block = Blocks.register(key, factory, settings);

        // This makes a BlockItem for it and registers that under the same id
        Items.register(block);

        return block;
	}
}
