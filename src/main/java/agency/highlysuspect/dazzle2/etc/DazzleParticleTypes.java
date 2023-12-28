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

import java.util.function.IntFunction;

public class DazzleParticleTypes {
	public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(RegistryKeys.PARTICLE_TYPE, "dazzle");
	public static final RegistryObject<ColorEffectParticleType<FlareParticleEffect>> FLARE = PARTICLE_TYPES.register("flare", () -> new ColorEffectParticleType<>(false, FlareParticleEffect::new));
	public static final RegistryObject<ColorEffectParticleType<DyedEndRodParticleEffect>> DYED_END_ROD = PARTICLE_TYPES.register("dyed_end_rod", () -> new ColorEffectParticleType<>(false, DyedEndRodParticleEffect::new));

	public static void onInitialize(IEventBus bus) {
		PARTICLE_TYPES.register(bus);
	}
}
