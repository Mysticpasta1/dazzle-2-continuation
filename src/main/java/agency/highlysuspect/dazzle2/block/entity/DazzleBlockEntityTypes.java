package agency.highlysuspect.dazzle2.block.entity;

import agency.highlysuspect.dazzle2.Init;
import agency.highlysuspect.dazzle2.block.DazzleBlocks;
import agency.highlysuspect.dazzle2.block.FlareBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class DazzleBlockEntityTypes {
	public static final BlockEntityType<LightSensorBlockEntity> LIGHT_SENSOR = FabricBlockEntityTypeBuilder.create(LightSensorBlockEntity::new, DazzleBlocks.LIGHT_SENSOR).build(null);
	public static final BlockEntityType<LightAirBlockEntity> LIGHT_AIR = FabricBlockEntityTypeBuilder.create(LightAirBlockEntity::new, DazzleBlocks.LIGHT_AIR).build(null);
	public static final BlockEntityType<FlareBlockEntity> FLARE = FabricBlockEntityTypeBuilder.create(FlareBlockEntity::new, DazzleBlocks.FLARES.values().toArray(new FlareBlock[0])).build(null);
	
	public static void onInitialize() {
		Registry.register(Registries.BLOCK_ENTITY_TYPE, Init.id("light_sensor"), LIGHT_SENSOR);
		Registry.register(Registries.BLOCK_ENTITY_TYPE, Init.id("light_air"), LIGHT_AIR);
		Registry.register(Registries.BLOCK_ENTITY_TYPE, Init.id("flare"), FLARE);
	}
}
