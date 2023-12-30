package agency.highlysuspect.dazzle2.etc;

import net.minecraft.particle.ParticleType;

public class FlareParticleEffect extends ColorEffect<FlareParticleEffect> {
	public FlareParticleEffect(int color) {
		super(color);
	}

	@Override
	public ParticleType<?> getType() {
		return DazzleParticleTypes.FLARE.get();
	}
}
