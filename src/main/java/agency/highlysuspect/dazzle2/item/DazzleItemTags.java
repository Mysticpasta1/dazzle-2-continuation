package agency.highlysuspect.dazzle2.item;

import agency.highlysuspect.dazzle2.Init;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;

public class DazzleItemTags {
	public static final TagKey<Item> WRENCHES = TagKey.of(Registries.ITEM.getKey(), Init.id("wrenches"));
	
	public static void onInitialize() {
		//run static init
	}
}
