package agency.highlysuspect.dazzle2.etc;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;

public class DyedEndRodParticleEffect extends ColorEffect<DyedEndRodParticleEffect> {

	public DyedEndRodParticleEffect(int color) {
		super(color);
	}

	@Override
	public ParticleType<?> getType() {
		return DazzleParticleTypes.DYED_END_ROD.get();
	}
}
