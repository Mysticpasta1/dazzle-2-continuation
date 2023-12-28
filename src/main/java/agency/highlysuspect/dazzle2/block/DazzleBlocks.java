package agency.highlysuspect.dazzle2.block;

import agency.highlysuspect.dazzle2.Init;
import agency.highlysuspect.dazzle2.LampStyle;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.state.property.Properties;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.registry.Registry;
import net.minecraft.world.BlockView;
import net.minecraftforge.registries.DeferredRegister;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class DazzleBlocks {
	public static DeferredRegister<Block> BLOCKS = DeferredRegister.create(RegistryKeys.BLOCK, "dazzle");
	public static final Map<String, Supplier<LampBlock>> LAMPS = LampStyle.ALL.stream()
		.collect(Collectors.toMap(a -> a.toName(), style -> style.instantiateBlock(AbstractBlock.Settings.copy(Blocks.REDSTONE_LAMP)));

	public static final LightSensorBlock LIGHT_SENSOR = new LightSensorBlock(FabricBlockSettings.copyOf(Blocks.OBSERVER));
	public static final InvisibleTorchBlock INVISIBLE_TORCH = new InvisibleTorchBlock(FabricBlockSettings.copyOf(Blocks.TORCH)
		.nonOpaque().noCollision().breakInstantly()
		.luminance(state -> state.get(InvisibleTorchBlock.LIGHT))
		.suffocates((state, world, pos) -> false)
		.blockVision((state, world, pos) -> false)
	);
	public static final LightAirBlock LIGHT_AIR = new LightAirBlock(FabricBlockSettings.copyOf(Blocks.AIR)
		.nonOpaque().noCollision().breakInstantly()
		.luminance(state -> state.get(LightAirBlock.LIGHT))
		.suffocates((state, world, pos) -> false)
		.blockVision((state, world, pos) -> false)
		.air() //is this a good idea?
		.dropsNothing()
		.ticksRandomly()
	);
	public static final ProjectedLightPanelBlock PROJECTED_LIGHT_PANEL = new ProjectedLightPanelBlock(FabricBlockSettings.copyOf(Blocks.BONE_BLOCK)
		.luminance(state -> state.get(ProjectedLightPanelBlock.POWER))
		.ticksRandomly()
	);
	
	public static final RedstoneTorchBlock DIM_REDSTONE_TORCH = new RedstoneTorchBlock(FabricBlockSettings.copyOf(Blocks.REDSTONE_TORCH)
		.luminance(state -> state.get(Properties.LIT) ? 2 : 0)
		.breakInstantly()
		.drops(Init.id("blocks/dim_redstone_torch")) //Idk why, but if I don't have this the loot table is the same as air block
	) {
		//Protected constructor lmao, also i need to override this anyways
		@Override
		public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
			return super.getWeakRedstonePower(state, world, pos, direction) == 15 ? 1 : 0;
		}
	};
	
	public static final RedstoneTorchBlock DIM_REDSTONE_WALL_TORCH = new WallRedstoneTorchBlock(FabricBlockSettings.copyOf(Blocks.REDSTONE_WALL_TORCH)
		.luminance(state -> state.get(Properties.LIT) ? 2 : 0)
		.dropsLike(DIM_REDSTONE_TORCH)
		.breakInstantly()
	) {
		//Protected constructor lmao, also i need to override this anyways
		@Override
		public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
			return super.getWeakRedstonePower(state, world, pos, direction) == 15 ? 1 : 0;
		}
	};
	
	public static final EnumMap<DyeColor, FlareBlock> FLARES = Util.make(new EnumMap<>(DyeColor.class), map -> {
		for(DyeColor color : DyeColor.values()) {
			map.put(color, new FlareBlock(color, FabricBlockSettings.copyOf(INVISIBLE_TORCH).luminance(15)));
		}
	});
	
	public static final EnumMap<DyeColor, ColorHolderBlock.Simple> DYED_SHROOMLIGHTS = sixteenColors(color -> new ColorHolderBlock.Simple(color, FabricBlockSettings.copyOf(Blocks.SHROOMLIGHT).requiresTool().materialColor(color)));
	public static final Block POLISHED_SHROOMLIGHT = new Block(FabricBlockSettings.copyOf(Blocks.SHROOMLIGHT).requiresTool());
	public static final EnumMap<DyeColor, ColorHolderBlock.Simple> DYED_POLISHED_SHROOMLIGHTS = sixteenColors(color -> new ColorHolderBlock.Simple(color, FabricBlockSettings.copyOf(Blocks.SHROOMLIGHT).requiresTool().materialColor(color)));
	
	public static final EnumMap<DyeColor, DyedEndRodBlock> DYED_END_RODS = sixteenColors(color -> new DyedEndRodBlock(color, FabricBlockSettings.copyOf(Blocks.END_ROD).materialColor(color)));
	
	public static void onInitialize() {
		for(LampBlock lamp : LAMPS) {
			Registry.register(Registries.BLOCK, lamp.style.toIdentifier(), lamp);
		}
		
		Registry.register(Registries.BLOCK, Init.id("light_sensor"), LIGHT_SENSOR);
		Registry.register(Registries.BLOCK, Init.id("invisible_torch"), INVISIBLE_TORCH);
		Registry.register(Registries.BLOCK, Init.id("light_air"), LIGHT_AIR);
		Registry.register(Registries.BLOCK, Init.id("projected_light_panel"), PROJECTED_LIGHT_PANEL);
		Registry.register(Registries.BLOCK, Init.id("dim_redstone_torch"), DIM_REDSTONE_TORCH);
		Registry.register(Registries.BLOCK, Init.id("dim_redstone_wall_torch"), DIM_REDSTONE_WALL_TORCH);
		
		FLARES.forEach((color, block) -> Registry.register(Registries.BLOCK, Init.id(color.asString() + "_flare"), block));
		
		DYED_SHROOMLIGHTS.forEach((color, block) -> Registry.register(Registries.BLOCK, Init.id(color.asString() + "_shroomlight"), block));
		Registry.register(Registries.BLOCK, Init.id("polished_shroomlight"), POLISHED_SHROOMLIGHT);
		DYED_POLISHED_SHROOMLIGHTS.forEach((color, block) -> Registry.register(Registries.BLOCK, Init.id(color.asString() + "_polished_shroomlight"), block));
		
		DYED_END_RODS.forEach((color, block) -> Registry.register(Registries.BLOCK, Init.id(color.asString() + "_end_rod"), block));
	}
	
	private static <T> EnumMap<DyeColor, T> sixteenColors(Function<DyeColor, T> maker) {
		EnumMap<DyeColor, T> map = new EnumMap<>(DyeColor.class);
		for(DyeColor color : DyeColor.values()) {
			map.put(color, maker.apply(color));
		}
		return map;
	}
}
