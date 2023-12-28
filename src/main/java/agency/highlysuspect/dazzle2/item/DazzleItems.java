package agency.highlysuspect.dazzle2.item;

import agency.highlysuspect.dazzle2.block.DazzleBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.*;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.Direction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class DazzleItems {
	public static DeferredRegister<ItemGroup> ITEM_GROUPS = DeferredRegister.create(RegistryKeys.ITEM_GROUP, "dazzle");
	public static DeferredRegister<Item> ITEMS = DeferredRegister.create(RegistryKeys.ITEM, "dazzle");


	public static final List<Supplier<? extends ItemConvertible>> MAIN_BLOCKS = new ArrayList<>();
	public static final RegistryObject<ItemGroup> owo = ITEM_GROUPS.register("owo", () -> ItemGroup.builder().icon(DazzleItems::icon).displayName(Text.literal("Dazzle 2")).entries((displayContext, entries) -> {
		MAIN_BLOCKS.forEach((itemLike -> entries.add(itemLike.get())));
	}).build());

	public static <T extends ItemConvertible> RegistryObject<T> addToMainTab(RegistryObject<T> itemLike) {
		MAIN_BLOCKS.add(itemLike);
		return itemLike;
	}

	public static final List<RegistryObject<BlockItem>> LAMPS = DazzleBlocks.LAMPS.stream().map(DazzleItems::blockItem).collect(Collectors.toList());
	public static final RegistryObject<BlockItem> LIGHT_SENSOR = blockItem(DazzleBlocks.LIGHT_SENSOR);
	public static final RegistryObject<BlockItem> INVISIBLE_TORCH = blockItem(DazzleBlocks.INVISIBLE_TORCH);
	public static final RegistryObject<BlockItem> PROJECTED_LIGHT_PANEL = blockItem(DazzleBlocks.PROJECTED_LIGHT_PANEL);
	
	public static final RegistryObject<VerticallyAttachableBlockItem> DIM_REDSTONE_TORCH = ITEMS.register("dim_redstone_torch", () -> new VerticallyAttachableBlockItem(DazzleBlocks.DIM_REDSTONE_TORCH.get(), DazzleBlocks.DIM_REDSTONE_WALL_TORCH.get(), settings(), Direction.NORTH));
	
	public static final EnumMap<DyeColor, RegistryObject<BlockItem>> FLARES = sixteenColorBlockItems(DazzleBlocks.FLARES);
	
	public static final EnumMap<DyeColor, RegistryObject<BlockItem>> DYED_SHROOMLIGHTS = sixteenColorBlockItems(DazzleBlocks.DYED_SHROOMLIGHTS);
	public static final RegistryObject<BlockItem> POLISHED_SHROOMLIGHT = blockItem(DazzleBlocks.POLISHED_SHROOMLIGHT);
	public static final EnumMap<DyeColor, RegistryObject<BlockItem>> DYED_POLISHED_SHROOMLIGHTS = sixteenColorBlockItems(DazzleBlocks.DYED_POLISHED_SHROOMLIGHTS);
	
	public static final EnumMap<DyeColor, RegistryObject<BlockItem>> DYED_END_RODS = sixteenColorBlockItems(DazzleBlocks.DYED_END_RODS);
	
	public static void onInitialize(IEventBus bus) {
		ITEM_GROUPS.register(bus);
		ITEMS.register(bus);
	}
	
	private static Item.Settings settings() {
		return new Item.Settings();
	}
	
	private static <T extends Block> RegistryObject<BlockItem> blockItem(RegistryObject<T> b) {
		return addToMainTab(ITEMS.register(b.getId().getPath(), () -> new BlockItem(b.get(), settings())));
	}
	
	private static ItemStack icon() {
		//Oh Java, you and your "illegal forward reference"s.
		return new ItemStack(LIGHT_SENSOR.get());
	}
	
	private static <T> EnumMap<DyeColor, RegistryObject<T>> sixteenColors(Function<DyeColor, RegistryObject<T>> maker) {
		EnumMap<DyeColor, RegistryObject<T>> map = new EnumMap<>(DyeColor.class);
		for(DyeColor color : DyeColor.values()) {
			map.put(color, maker.apply(color));
		}
		return map;
	}
	
	private static <T extends Block> EnumMap<DyeColor, RegistryObject<BlockItem>> sixteenColorBlockItems(EnumMap<DyeColor, RegistryObject<T>> blockMap) {
		return sixteenColors(color -> blockItem(blockMap.get(color)));
	}
}
