package agency.highlysuspect.dazzle2.item;

import agency.highlysuspect.dazzle2.Init;
import agency.highlysuspect.dazzle2.block.DazzleBlocks;
import net.fabricmc.fabric.impl.itemgroup.FabricItemGroup;
import net.fabricmc.fabric.impl.itemgroup.FabricItemGroupBuilderImpl;
import net.minecraft.block.Block;
import net.minecraft.block.WallMountedBlock;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class DazzleItems {
	public static final List<Supplier<? extends ItemConvertible>> MAIN_BLOCKS = new ArrayList<>();
	public static final ItemGroup owo = new FabricItemGroupBuilderImpl(Init.id("group")).icon(DazzleItems::icon).entries((displayContext, entries) -> {
		MAIN_BLOCKS.forEach((itemLike -> entries.add(itemLike.get())));
	}).build();

	public static ItemConvertible addToMainTab (ItemConvertible itemLike) {
		MAIN_BLOCKS.add(() -> itemLike);
		return itemLike;
	}

	public static final List<BlockItem> LAMPS = DazzleBlocks.LAMPS.stream().map(DazzleItems::blockItem).collect(Collectors.toList());
	public static final BlockItem LIGHT_SENSOR = blockItem(DazzleBlocks.LIGHT_SENSOR);
	public static final BlockItem INVISIBLE_TORCH = blockItem(DazzleBlocks.INVISIBLE_TORCH);
	public static final BlockItem PROJECTED_LIGHT_PANEL = blockItem(DazzleBlocks.PROJECTED_LIGHT_PANEL);
	
	public static final VerticallyAttachableBlockItem DIM_REDSTONE_TORCH = new VerticallyAttachableBlockItem(DazzleBlocks.DIM_REDSTONE_TORCH, DazzleBlocks.DIM_REDSTONE_WALL_TORCH, settings(), Direction.NORTH);
	
	public static final EnumMap<DyeColor, BlockItem> FLARES = sixteenColorBlockItems(DazzleBlocks.FLARES);
	
	public static final EnumMap<DyeColor, BlockItem> DYED_SHROOMLIGHTS = sixteenColorBlockItems(DazzleBlocks.DYED_SHROOMLIGHTS);
	public static final BlockItem POLISHED_SHROOMLIGHT = blockItem(DazzleBlocks.POLISHED_SHROOMLIGHT);
	public static final EnumMap<DyeColor, BlockItem> DYED_POLISHED_SHROOMLIGHTS = sixteenColorBlockItems(DazzleBlocks.DYED_POLISHED_SHROOMLIGHTS);
	
	public static final EnumMap<DyeColor, BlockItem> DYED_END_RODS = sixteenColorBlockItems(DazzleBlocks.DYED_END_RODS);
	
	public static void onInitialize() {
		registerBlockItems(LAMPS);
		
		registerBlockItem(LIGHT_SENSOR);
		registerBlockItem(INVISIBLE_TORCH);
		registerBlockItem(PROJECTED_LIGHT_PANEL);
		
		registerBlockItem(DIM_REDSTONE_TORCH);
		
		registerBlockItems(FLARES.values());
		
		registerBlockItems(DYED_SHROOMLIGHTS.values());
		registerBlockItem(POLISHED_SHROOMLIGHT);
		registerBlockItems(DYED_POLISHED_SHROOMLIGHTS.values());
		
		registerBlockItems(DYED_END_RODS.values());
	}
	
	private static Item.Settings settings() {
		return new Item.Settings();
	}
	
	private static BlockItem blockItem(Block b) {
		return new BlockItem(b, settings());
	}
	
	private static ItemStack icon() {
		//Oh Java, you and your "illegal forward reference"s.
		return new ItemStack(LIGHT_SENSOR);
	}
	
	private static <T> EnumMap<DyeColor, T> sixteenColors(Function<DyeColor, T> maker) {
		EnumMap<DyeColor, T> map = new EnumMap<>(DyeColor.class);
		for(DyeColor color : DyeColor.values()) {
			map.put(color, maker.apply(color));
		}
		return map;
	}
	
	private static EnumMap<DyeColor, BlockItem> sixteenColorBlockItems(EnumMap<DyeColor, ? extends Block> blockMap) {
		return sixteenColors(color -> blockItem(blockMap.get(color)));
	}
	
	private static void registerBlockItem(BlockItem item) {
		Identifier id = Registries.BLOCK.getId(item.getBlock());
		addToMainTab(item);
		Registry.register(Registries.ITEM, id, item);
	}
	
	private static void registerBlockItems(Collection<BlockItem> blockItems) {
		blockItems.forEach(DazzleItems::registerBlockItem);
	}
}
