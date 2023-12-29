package agency.highlysuspect.dazzle2.block.entity;

import agency.highlysuspect.dazzle2.block.DazzleBlocks;
import agency.highlysuspect.dazzle2.block.FlareBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.RegistryKeys;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class DazzleBlockEntityTypes {
	private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(RegistryKeys.BLOCK_ENTITY_TYPE, "dazzle");

	public static final RegistryObject<BlockEntityType<LightSensorBlockEntity>> LIGHT_SENSOR = BLOCK_ENTITIES.register("light_sensor", () -> BlockEntityType.Builder.create(LightSensorBlockEntity::new, DazzleBlocks.LIGHT_SENSOR.get()).build(null));
	public static final RegistryObject<BlockEntityType<LightAirBlockEntity>> LIGHT_AIR = BLOCK_ENTITIES.register("light_air", () -> BlockEntityType.Builder.create(LightAirBlockEntity::new, DazzleBlocks.LIGHT_AIR.get()).build(null));
	public static final RegistryObject<BlockEntityType<FlareBlockEntity>> FLARE = BLOCK_ENTITIES.register("flare", () -> BlockEntityType.Builder.create(FlareBlockEntity::new, DazzleBlocks.FLARES.values().stream().map(RegistryObject::get).toArray(FlareBlock[]::new)).build(null));
	
	public static void onInitialize(IEventBus bus) {
		BLOCK_ENTITIES.register(bus);
	}
}
