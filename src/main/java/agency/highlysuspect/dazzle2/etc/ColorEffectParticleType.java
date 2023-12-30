package agency.highlysuspect.dazzle2.etc;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.particle.ParticleType;

import java.util.function.IntFunction;

public class ColorEffectParticleType<T extends ColorEffect> extends ParticleType<T> {

    private final Codec<T> codec;

    public ColorEffectParticleType(boolean alwaysShow, IntFunction<T> function) {
        super(alwaysShow, new ColorEffect.ColorFactory<>(function));
        this.codec = RecordCodecBuilder.create(inst -> inst.group(Codec.INT.fieldOf("color").forGetter(ColorEffect::getColor)).apply(inst, function::apply));
    }

    @Override
    public Codec<T> getCodec() {
        return codec;
    }
}
