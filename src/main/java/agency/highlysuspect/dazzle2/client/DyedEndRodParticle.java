package agency.highlysuspect.dazzle2.client;

import agency.highlysuspect.dazzle2.etc.DyedEndRodParticleEffect;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.OnlyIn;

import static net.minecraftforge.api.distmarker.Dist.CLIENT;

//Based on a copy-and-paste of EndRodParticle because lol private constructor.
//Added an extra parameter for setTargetColor.
@OnlyIn(CLIENT)
public class DyedEndRodParticle extends SpriteBillboardParticle {

	public DyedEndRodParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, int tint) {
		super(world, x, y, z);
		this.velocityX = velocityX;
		this.velocityY = velocityY;
		this.velocityZ = velocityZ;
		this.scale *= 0.75F;
		this.maxAge = 60 + this.random.nextInt(12);
		
		float magicNumber = tint == DyeColor.WHITE.getMapColor().color ? 0.9f : 0.6f;
		
		int r = (tint & 0xFF0000) >> 16;
		int g = (tint & 0x00FF00) >> 8;
		int b = tint & 0x0000FF;
		
		r *= magicNumber;
		r = MathHelper.clamp(r, 0, 255);
		g *= magicNumber;
		g = MathHelper.clamp(g, 0, 255);
		b *= magicNumber;
		b = MathHelper.clamp(b, 0, 255);
		
		setColor(r, g, b);
	}

	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
	}

	public void move(double dx, double dy, double dz) {
		this.setBoundingBox(this.getBoundingBox().offset(dx, dy, dz));
		this.repositionFromBoundingBox();
	}
	
	@OnlyIn(CLIENT)
	public static class Factory implements ParticleFactory<DyedEndRodParticleEffect> {
		
		public Particle createParticle(DyedEndRodParticleEffect ef, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
			return new DyedEndRodParticle(clientWorld, d, e, f, g, h, i, ef.getColor());
		}
	}
}
