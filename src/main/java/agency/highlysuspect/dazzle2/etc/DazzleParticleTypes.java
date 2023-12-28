package agency.highlysuspect.dazzle2.etc;

import agency.highlysuspect.dazzle2.Init;
import com.mojang.serialization.Codec;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class DazzleParticleTypes {
	public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(RegistryKeys.PARTICLE_TYPE, "dazzle");
	public static final RegistryObject<ParticleType<FlareParticleEffect>> FLARE = PARTICLE_TYPES.register("flare", () -> new ParticleType<FlareParticleEffect>(false, FlareParticleEffect.FactoryThingie.INSTANCE) {
		@Override
		public Codec<FlareParticleEffect> getCodec() {
			return FlareParticleEffect.CODEC;
		}
	});

	public static final RegistryObject<ParticleType<DyedEndRodParticleEffect>> DYED_END_ROD = PARTICLE_TYPES.register("dyed_end_rod", () -> new ParticleType<DyedEndRodParticleEffect>(false, DyedEndRodParticleEffect.FactoryThingie.INSTANCE) {

		@Override
		public Codec<DyedEndRodParticleEffect> getCodec() {
			return DyedEndRodParticleEffect.CODEC;
		}
	});
	
	public static void onInitialize(IEventBus bus) {
		PARTICLE_TYPES.register(bus);
	}
}
