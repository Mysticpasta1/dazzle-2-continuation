package agency.highlysuspect.dazzle2.etc;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;

import java.util.function.IntFunction;

public abstract class ColorEffect<F extends ColorEffect> implements ParticleEffect {
    private final int color;

    public ColorEffect(int color) {
        this.color = color;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeInt(color);
    }

    @Override
    public String asString() {
        return Registries.PARTICLE_TYPE.getId(getType()).toString();
    }

    public int getColor() {
        return color;
    }

    public static record ColorFactory<T extends ColorEffect<ColorEffect>>(IntFunction<T> function) implements ParticleEffect.Factory<T> {

        @Override
        public T read(ParticleType<T> type, StringReader reader) throws CommandSyntaxException {
            //TODO
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException().create("i dont wanna write a command parser lol");
        }

        @Override
        public T read(ParticleType<T> type, PacketByteBuf buf) {
            int color = buf.readInt();
            return function().apply(color);
        }
    }
}
