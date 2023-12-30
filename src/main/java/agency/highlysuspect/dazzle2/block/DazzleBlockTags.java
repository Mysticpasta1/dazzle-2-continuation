package agency.highlysuspect.dazzle2.block;

import agency.highlysuspect.dazzle2.Init;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;

public class DazzleBlockTags {
	public static final TagKey<Block> MAKE_INVISIBLE_TORCH = TagKey.of(Registries.BLOCK.getKey(), Init.id("make_invisible_torches"));
	
	public static void onInitialize() {
		//run static init
	}
}
