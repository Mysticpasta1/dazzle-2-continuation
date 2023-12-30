package agency.highlysuspect.dazzle2.block;

import agency.highlysuspect.dazzle2.LampStyle;
import net.minecraft.block.*;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.state.property.Properties;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class DazzleBlocks {
	public static DeferredRegister<Block> BLOCKS = DeferredRegister.create(RegistryKeys.BLOCK, "dazzle");
	public static final List<RegistryObject<LampBlock>> LAMPS = LampStyle.ALL.stream()
		.map(style -> BLOCKS.register(style.toName(), style.instantiateBlock(AbstractBlock.Settings.copy(Blocks.REDSTONE_LAMP)))).toList();

	public static final RegistryObject<LightSensorBlock> LIGHT_SENSOR = BLOCKS.register("light_sensor", () -> new LightSensorBlock(AbstractBlock.Settings.copy(Blocks.OBSERVER)));
	public static final RegistryObject<InvisibleTorchBlock> INVISIBLE_TORCH = BLOCKS.register("invisible_torch", () -> new InvisibleTorchBlock(AbstractBlock.Settings.copy(Blocks.TORCH)
		.nonOpaque().noCollision().breakInstantly()
		.luminance(state -> state.get(InvisibleTorchBlock.LIGHT))
		.suffocates((state, world, pos) -> false)
		.blockVision((state, world, pos) -> false)
	));

	public static final RegistryObject<LightAirBlock> LIGHT_AIR = BLOCKS.register("light_air", () -> new LightAirBlock(AbstractBlock.Settings.copy(Blocks.AIR)
		.nonOpaque().noCollision().breakInstantly()
		.luminance(state -> state.get(LightAirBlock.LIGHT))
		.suffocates((state, world, pos) -> false)
		.blockVision((state, world, pos) -> false)
		.air() //is this a good idea?
		.dropsNothing()
		.ticksRandomly()
	));

	public static final RegistryObject<ProjectedLightPanelBlock> PROJECTED_LIGHT_PANEL = BLOCKS.register("projected_light_panel", () -> new ProjectedLightPanelBlock(AbstractBlock.Settings.copy(Blocks.BONE_BLOCK)
		.luminance(state -> state.get(ProjectedLightPanelBlock.POWER))
		.ticksRandomly()
	));
	
	public static final RegistryObject<RedstoneTorchBlock> DIM_REDSTONE_TORCH = BLOCKS.register("dim_redstone_torch", () -> new RedstoneTorchBlock(Block.Settings.copy(Blocks.REDSTONE_TORCH)
		.luminance(state -> state.get(Properties.LIT) ? 2 : 0)
		.breakInstantly()
//			.dropsLike(Init.id("blocks/dim_redstone_torch")) //Idk why, but if I don't have this the loot table is the same as air block
	) {
		//Protected constructor lmao, also i need to override this anyways
		@Override
		public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
			return super.getWeakRedstonePower(state, world, pos, direction) == 15 ? 1 : 0;
		}
	});
	
	public static final RegistryObject<RedstoneTorchBlock> DIM_REDSTONE_WALL_TORCH = BLOCKS.register("dim_redstone_wall_torch", () -> new WallRedstoneTorchBlock(AbstractBlock.Settings.copy(Blocks.REDSTONE_WALL_TORCH)
		.luminance(state -> state.get(Properties.LIT) ? 2 : 0)
			.lootFrom(DIM_REDSTONE_TORCH)
		.breakInstantly()
	) {
		//Protected constructor lmao, also i need to override this anyways
		@Override
		public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
			return super.getWeakRedstonePower(state, world, pos, direction) == 15 ? 1 : 0;
		}
	});
	
	public static final EnumMap<DyeColor, RegistryObject<FlareBlock>> FLARES = sixteenColors(color -> BLOCKS.register(color.asString() + "_flare", () -> new FlareBlock(color, AbstractBlock.Settings.copy(INVISIBLE_TORCH.get()).luminance(a -> 15))));

	public static final EnumMap<DyeColor, RegistryObject<ColorHolderBlock.Simple>> DYED_SHROOMLIGHTS = sixteenColors(color -> BLOCKS.register(color.asString() + "_shroomlight", () -> new ColorHolderBlock.Simple(color, AbstractBlock.Settings.copy(Blocks.SHROOMLIGHT).requiresTool().mapColor(color))));
	public static final RegistryObject<Block> POLISHED_SHROOMLIGHT = BLOCKS.register("polished_shroomlight", () -> new Block(AbstractBlock.Settings.copy(Blocks.SHROOMLIGHT).requiresTool()));
	public static final EnumMap<DyeColor, RegistryObject<ColorHolderBlock.Simple>> DYED_POLISHED_SHROOMLIGHTS = sixteenColors(color -> BLOCKS.register(color.asString() + "_polished_shroomlight", () -> new ColorHolderBlock.Simple(color, AbstractBlock.Settings.copy(Blocks.SHROOMLIGHT).requiresTool().mapColor(color))));
	
	public static final EnumMap<DyeColor, RegistryObject<DyedEndRodBlock>> DYED_END_RODS = sixteenColors(color -> BLOCKS.register(color.asString() + "_end_rod", ()  -> new DyedEndRodBlock(color, AbstractBlock.Settings.copy(Blocks.END_ROD).mapColor(color))));
	
	public static void onInitialize(IEventBus bus) {
		BLOCKS.register(bus);
	}
	
	private static <T> EnumMap<DyeColor, RegistryObject<T>> sixteenColors(Function<DyeColor, RegistryObject<T>> maker) {
		EnumMap<DyeColor, RegistryObject<T>> map = new EnumMap<>(DyeColor.class);
		for(DyeColor color : DyeColor.values()) {
			map.put(color, maker.apply(color));
		}
		return map;
	}
}
