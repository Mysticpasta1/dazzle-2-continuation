package agency.highlysuspect.dazzle2.etc;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;

//Copypaste of FlareParticleEffect
//Don't learn how to write particles from this. I'm just slapping stuff together.
public record DyedEndRodParticleEffect(int color) implements ParticleEffect {
	public int getColor() {
		return color;
	}
	
	@Override
	public ParticleType<?> getType() {
		return DazzleParticleTypes.DYED_END_ROD;
	}
	
	@Override
	public void write(PacketByteBuf buf) {
		buf.writeInt(color);
	}
	
	@SuppressWarnings("ConstantConditions") //The particle was registered.
	@Override
	public String asString() {
		return Registries.PARTICLE_TYPE.getId(DazzleParticleTypes.DYED_END_ROD).toString();
	}
	
	public static class FactoryThingie implements Factory<DyedEndRodParticleEffect> {
		public static final FactoryThingie INSTANCE = new FactoryThingie();
		
		@Override
		public DyedEndRodParticleEffect read(ParticleType<DyedEndRodParticleEffect> type, StringReader reader) throws CommandSyntaxException {
			//TODO
			throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException().create("i dont wanna write a command parser lol");
		}
		
		@Override
		public DyedEndRodParticleEffect read(ParticleType<DyedEndRodParticleEffect> type, PacketByteBuf buf) {
			int color = buf.readInt();
			return new DyedEndRodParticleEffect(color);
		}
	}
}
