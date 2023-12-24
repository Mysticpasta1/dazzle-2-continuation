package agency.highlysuspect.dazzle2.block;

import agency.highlysuspect.dazzle2.Init;
import net.minecraft.block.Block;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.Registry;

public class DazzleBlockTags {
	public static final TagKey<Block> MAKE_INVISIBLE_TORCH = TagKey.of(Registry.BLOCK_KEY, Init.id("make_invisible_torches"));
	
	public static void onInitialize() {
		//run static init
	}
}
