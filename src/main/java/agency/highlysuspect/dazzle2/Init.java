package agency.highlysuspect.dazzle2;

import agency.highlysuspect.dazzle2.block.DazzleBlockTags;
import agency.highlysuspect.dazzle2.block.DazzleBlocks;
import agency.highlysuspect.dazzle2.block.entity.DazzleBlockEntityTypes;
import agency.highlysuspect.dazzle2.etc.DazzleParticleTypes;
import agency.highlysuspect.dazzle2.item.DazzleItemTags;
import agency.highlysuspect.dazzle2.item.DazzleItems;
import net.minecraft.util.Identifier;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Init.MODID)
public class Init {
	public static final String MODID = "dazzle";
	public static final Logger LOGGER = LogManager.getLogger(MODID);
	
	public static final boolean DEBUG = false;
	
	public static Identifier id(String path) {
		return new Identifier(MODID, path);
	}
	
	public static void log(String msg, Object... fmt) {
		LOGGER.info(msg, fmt);
	}

	public Init() {
		var bus = FMLJavaModLoadingContext.get().getModEventBus();
		DazzleBlocks.onInitialize(bus);
		DazzleBlockTags.onInitialize();
		DazzleBlockEntityTypes.onInitialize(bus);
		
		DazzleItems.onInitialize(bus);
		DazzleItemTags.onInitialize();

		DazzleParticleTypes.onInitialize(bus);
	}
}
