package agency.highlysuspect.dazzle2.client;

import agency.highlysuspect.dazzle2.block.ColorHolderBlock;
import agency.highlysuspect.dazzle2.block.DazzleBlocks;
import agency.highlysuspect.dazzle2.block.LampBlock;
import agency.highlysuspect.dazzle2.etc.DazzleParticleTypes;
import agency.highlysuspect.dazzle2.etc.DyedEndRodParticleEffect;
import agency.highlysuspect.dazzle2.etc.FlareParticleEffect;
import agency.highlysuspect.dazzle2.item.DazzleItems;
import com.google.common.eventbus.Subscribe;
import net.minecraft.block.Block;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;

@Mod.EventBusSubscriber(modid = "dazzle", value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientInit {

	@SubscribeEvent
	public static void onInitializeClient(FMLClientSetupEvent event) {
		assignBlockLayers();
	}

	private static void assignBlockLayers() {
		final RenderLayer cutout = RenderLayer.getCutout();
		final RenderLayer cutoutMipped = RenderLayer.getCutoutMipped();
		final RenderLayer translucent = RenderLayer.getTranslucent();
		
		//need at least cutoutmipped so color providers work
		DazzleBlocks.LAMPS.forEach(b -> RenderLayers.setRenderLayer(b.get(), b.get().style.theme.isTransparent ? translucent : cutoutMipped));
		
		RenderLayers.setRenderLayer(DazzleBlocks.INVISIBLE_TORCH.get(), cutout);
		RenderLayers.setRenderLayer(DazzleBlocks.LIGHT_AIR.get(), cutout);
		RenderLayers.setRenderLayer(DazzleBlocks.PROJECTED_LIGHT_PANEL.get(), cutout);

		RenderLayers.setRenderLayer(DazzleBlocks.DIM_REDSTONE_TORCH.get(), cutout);
		RenderLayers.setRenderLayer(DazzleBlocks.DIM_REDSTONE_WALL_TORCH.get(), cutout);

		//need at least cutoutmipped so color providers work
		//these use very odd opacity blending so i'll try the translucent layer
		DazzleBlocks.DYED_SHROOMLIGHTS.values().forEach(b -> RenderLayers.setRenderLayer(b.get(), translucent));
		DazzleBlocks.DYED_POLISHED_SHROOMLIGHTS.values().forEach(b -> RenderLayers.setRenderLayer(b.get(), translucent));

		DazzleBlocks.DYED_END_RODS.values().forEach(b -> RenderLayers.setRenderLayer(b.get(), cutoutMipped));
	}

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void registerBlockColor(RegisterColorHandlersEvent.Block event) {
		//Redstone lamps
		event.register((state, world, pos, tintIndex) -> {
			LampBlock lamp = (LampBlock) state.getBlock();
			return lamp(tintIndex, lamp.getColor(), lamp.lightFromState(state));
		}, blocks(DazzleBlocks.LAMPS));

		//Flares
		//Even though the block model itself is invisible, this is visible on blockcrack particles.
		event.register((state, world, pos, tintIndex) -> ((ColorHolderBlock) state.getBlock()).getColor().getMapColor().color,
			blocks(DazzleBlocks.FLARES.values()));

		//Dyed shroomlights
		event.register((state, world, pos, tintIndex) -> shroom(tintIndex, ((ColorHolderBlock) state.getBlock()).getColor()),
			blocks(DazzleBlocks.DYED_SHROOMLIGHTS.values(), DazzleBlocks.DYED_POLISHED_SHROOMLIGHTS.values()));

		//End rods
		event.register((state, world, pos, tintIndex) -> rod(tintIndex, ((ColorHolderBlock) state.getBlock()).getColor()),
			blocks(DazzleBlocks.DYED_END_RODS.values()));
	}

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void registerItemColor(RegisterColorHandlersEvent.Item event) {
		event.register((stack, tintIndex) -> {
			LampBlock lamp = (LampBlock) ((BlockItem) stack.getItem()).getBlock();
			return lamp(tintIndex, lamp.getColor(), 15);
		}, items(DazzleItems.LAMPS));

		event.register((stack, tintIndex) -> {
			if(tintIndex == 1) {
				return ((ColorHolderBlock) ((BlockItem) stack.getItem()).getBlock()).getColor().getMapColor().color; //color
			} else return 0xFFFFFF;
		}, items(DazzleItems.FLARES.values()));

		event.register((stack, tintIndex) -> shroom(tintIndex, ((ColorHolderBlock) ((BlockItem) stack.getItem()).getBlock()).getColor()),
				items(DazzleItems.DYED_SHROOMLIGHTS.values(), DazzleItems.DYED_POLISHED_SHROOMLIGHTS.values()));

		event.register((stack, tintIndex) -> rod(tintIndex, ((ColorHolderBlock) ((BlockItem) stack.getItem()).getBlock()).getColor()),
				items(DazzleItems.DYED_END_RODS.values()));
	}

	private static int multiplyAll(int color, float mult) {
		return multiplyRgb(color, mult, mult, mult);
	}
	
	private static int multiplyRgb(int color, float multR, float multG, float multB) {
		int r = (color & 0xFF0000) >> 16;
		int g = (color & 0x00FF00) >> 8;
		int b = color & 0x0000FF;
		
		r *= multR;
		r = MathHelper.clamp(r, 0, 255);
		g *= multG;
		g = MathHelper.clamp(g, 0, 255);
		b *= multB;
		b = MathHelper.clamp(b, 0, 255);
		
		return (r << 16) | (g << 8) | b;
	}
	
	private static int lamp(int tintIndex, DyeColor color, int power) {
		if(tintIndex == 0) return multiplyAll(color.getMapColor().color, power / 15f * 0.8f + 0.2f);
		else return 0xFFFFFF;
	}
	
	private static int shroom(int tintIndex, DyeColor color) {
		if(tintIndex == 0) return shroomColorTable[color.ordinal()];
		else return 0xFFFFFF;
	}
	
	private static int rod(int tintIndex, DyeColor color) {
		if(tintIndex == 0) return shroomColorTable[color.ordinal()];
		else if(tintIndex == 1) return multiplyAll(color.getMapColor().color, 0.5f);
		else return 0xFFFFFF;
	}
	
	private static final int[] shroomColorTable;
	
	static {
		shroomColorTable = new int[DyeColor.values().length];
		for(DyeColor color : DyeColor.values()) {
			shroomColorTable[color.ordinal()] = multiplyRgb(color.getMapColor().color, 1.7f, 1.5f, 1.4f);
		}
		
		//Orange looks kinda yellowish so uhh poke it back down a bit
		int orange = DyeColor.ORANGE.ordinal();
		shroomColorTable[orange] = multiplyRgb(shroomColorTable[orange], 0.9f, 0.85f, 1f);
	}
	
	//Quick functions because java varargs are so unergonomic w/ collections
	@SafeVarargs
	private static <T> T[] conv(Class<T> javaSucks, Collection<RegistryObject<T>>... listOfLists) {
		//noinspection unchecked
		return Arrays.stream(listOfLists).flatMap(Collection::stream).map(RegistryObject::get).toArray(i -> (T[]) Array.newInstance(javaSucks, i));
	}
	
	@SafeVarargs
	private static Block[] blocks(Collection<RegistryObject<Block>>... listOfLists) {
		return conv(Block.class, listOfLists);
	}
	
	@SafeVarargs
	private static Item[] items(Collection<RegistryObject<Item>>... listOfLists) {
		return conv(Item.class, listOfLists);
	}

	@Subscribe
	private static void registerParticles(RegisterParticleProvidersEvent event) {
		event.registerSpecial(DazzleParticleTypes.FLARE.get(), new ParticleFactory<>() {
            @Nullable
            @Override
            public Particle createParticle(FlareParticleEffect parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
                return new FlareParticle(world, x, y, z, velocityX, velocityY, velocityZ, parameters.getColor());
            }
        });
		event.registerSpecial(DazzleParticleTypes.DYED_END_ROD.get(), new ParticleFactory<>() {
			@Nullable
			@Override
			public Particle createParticle(DyedEndRodParticleEffect parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
				return new DyedEndRodParticle(world, x, y, z, velocityX, velocityY, velocityZ, parameters.getColor());
			}
		});
	}
}