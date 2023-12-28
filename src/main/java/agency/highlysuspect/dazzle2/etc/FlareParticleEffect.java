package agency.highlysuspect.dazzle2.etc;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;

public record FlareParticleEffect(int color) implements ParticleEffect {
	public static final Codec<FlareParticleEffect> CODEC = RecordCodecBuilder.create(inst -> inst.group(Codec.INT.fieldOf("color").forGetter(FlareParticleEffect::color)).apply(inst, FlareParticleEffect::new));

	@Override
	public ParticleType<?> getType() {
		return DazzleParticleTypes.FLARE;
	}
	
	@Override
	public void write(PacketByteBuf buf) {
		buf.writeInt(color);
	}
	
	public int getColor() {
		return color;
	}
	
	@SuppressWarnings("ConstantConditions") //The particle was registered.
	@Override
	public String asString() {
		return Registries.PARTICLE_TYPE.getId(DazzleParticleTypes.FLARE).toString();
	}
	
	public static class FactoryThingie implements ParticleEffect.Factory<FlareParticleEffect> {
		public static final FactoryThingie INSTANCE = new FactoryThingie();
		
		@Override
		public FlareParticleEffect read(ParticleType<FlareParticleEffect> type, StringReader reader) throws CommandSyntaxException {
			//TODO
			throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException().create("i dont wanna write a command parser lol");
		}
		
		@Override
		public FlareParticleEffect read(ParticleType<FlareParticleEffect> type, PacketByteBuf buf) {
			int color = buf.readInt();
			return new FlareParticleEffect(color);
		}
	}
}
