package agency.highlysuspect.dazzle2.item;

import agency.highlysuspect.dazzle2.Init;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.Registry;

public class DazzleItemTags {
	public static final TagKey<Item> WRENCHES = TagKey.of(Registry.ITEM_KEY, Init.id("wrenches"));
	
	public static void onInitialize() {
		//run static init
	}
}
