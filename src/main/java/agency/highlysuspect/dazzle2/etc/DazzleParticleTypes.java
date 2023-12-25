package agency.highlysuspect.dazzle2.etc;

import agency.highlysuspect.dazzle2.Init;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class DazzleParticleTypes {
	public static final ParticleType<FlareParticleEffect> FLARE = FabricParticleTypes.complex(FlareParticleEffect.FactoryThingie.INSTANCE);
	public static final ParticleType<DyedEndRodParticleEffect> DYED_END_ROD = FabricParticleTypes.complex(DyedEndRodParticleEffect.FactoryThingie.INSTANCE);
	
	public static void onInitialize() {
		Registry.register(Registries.PARTICLE_TYPE, Init.id("flare"), FLARE);
		Registry.register(Registries.PARTICLE_TYPE, Init.id("dyed_end_rod"), DYED_END_ROD);
	}
}
