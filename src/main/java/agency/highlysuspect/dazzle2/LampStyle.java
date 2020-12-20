package agency.highlysuspect.dazzle2;

import agency.highlysuspect.dazzle2.block.LampBlock;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.commons.lang3.text.WordUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public class LampStyle {
	private LampStyle(Color color, Theme theme, Mode mode) {
		this.color = color;
		this.theme = theme;
		this.mode = mode;
	}
	
	private LampStyle(List<Prop> list) {
		//Mfw no heterogenous tuples
		this((Color) list.get(0), (Theme) list.get(1), (Mode) list.get(2));
	}
	
	public static LampStyle fromName(String name) {
		return LOOKUP.get(name);
	}
	
	public static LampStyle fromIdentifier(Identifier id) {
		return LOOKUP.get(id.getPath());
	}
	
	public final Color color;
	public final Theme theme;
	public final Mode mode;
	
	public String toName() {
		return color.color.getName() + '_' + theme.name + '_' + mode.name + "_lamp";
	}
	
	public Identifier toIdentifier() {
		return Init.id(toName());
	}
	
	public String englishLocalization(boolean spellGrayWithAnE) {
		return color.englishLocalization(spellGrayWithAnE) + " " + theme.englishLocalization() + " " + mode.englishLocalization() + " Lamp";
	}
	
	public LampBlock instantiateBlock(Block.Settings settings) {
		return mode.constructor.apply(this, theme.processSettings(settings));
	}
	
	public LampStyle withMode(LampStyle.Mode newMode) {
		//This sucks
		LampStyle other = new LampStyle(color, theme, newMode);
		for(LampStyle a : ALL) {
			if(a.equals(other)) return a; //"""""cache"""""
		}
		return other;
	}
	
	public static List<LampStyle> ALL = Lists.cartesianProduct(Color.ALL, Theme.ALL, Mode.ALL).stream().map(LampStyle::new).collect(Collectors.toList());
	public static Map<String, LampStyle> LOOKUP = ALL.stream().collect(Collectors.toMap(LampStyle::toName, Function.identity()));
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		
		LampStyle lampStyle = (LampStyle) o;
		
		if(!color.equals(lampStyle.color)) return false;
		if(!theme.equals(lampStyle.theme)) return false;
		return mode.equals(lampStyle.mode);
	}
	
	@Override
	public int hashCode() {
		int result = color.hashCode();
		result = 31 * result + theme.hashCode();
		result = 31 * result + mode.hashCode();
		return result;
	}
	
	public interface Prop {}
	
	//btw, because these have a private constructor (they're basically enums) there's no need to override equals and hashcode.
	public static class Color implements Prop {
		private Color(DyeColor color) {
			this.color = color;
		}
		
		public final DyeColor color;
		
		public static final List<Color> ALL = Arrays.stream(DyeColor.values()).map(Color::new).collect(Collectors.toList());
		
		public String englishLocalization(boolean spellGrayWithAnE) {
			String nameLowercase = color.getName().replace('_', ' ');
			if(spellGrayWithAnE) nameLowercase = nameLowercase.replaceAll("gray", "grey");
			return WordUtils.capitalizeFully(nameLowercase);
		}
		
		public Identifier itemId() {
			if(COLORS_TO_ITEM_IDS.containsKey(color)) {
				return COLORS_TO_ITEM_IDS.get(color);
			}
			
			//I heard talk of mods that add their own dye colors by extending the enum.
			//Who knows if that will come to fruition, but let's assume this hypothetical mod uses the
			//same name format to the other minecraft dyes (that is, "somemod:flurple_dye", for example)
			String path = color.getName() + "_dye";
			for(ModContainer mc : FabricLoader.getInstance().getAllMods()) {
				Identifier maybeId = new Identifier(mc.getMetadata().getId(), path);
				if(Registry.ITEM.containsId(maybeId)) {
					COLORS_TO_ITEM_IDS.put(color, maybeId);
					return maybeId;
				}
			}
			
			throw new IllegalStateException("Can't find any item corresponding to the dye " + color);
		}
		
		private static final Map<DyeColor, Identifier> COLORS_TO_ITEM_IDS = new HashMap<>();
		
		static {
			COLORS_TO_ITEM_IDS.put(DyeColor.WHITE, new Identifier("minecraft", "white_dye"));
			COLORS_TO_ITEM_IDS.put(DyeColor.ORANGE, new Identifier("minecraft", "orange_dye"));
			COLORS_TO_ITEM_IDS.put(DyeColor.MAGENTA, new Identifier("minecraft", "magenta_dye"));
			COLORS_TO_ITEM_IDS.put(DyeColor.LIGHT_BLUE, new Identifier("minecraft", "light_blue_dye"));
			COLORS_TO_ITEM_IDS.put(DyeColor.YELLOW, new Identifier("minecraft", "yellow_dye"));
			COLORS_TO_ITEM_IDS.put(DyeColor.LIME, new Identifier("minecraft", "lime_dye"));
			COLORS_TO_ITEM_IDS.put(DyeColor.PINK, new Identifier("minecraft", "pink_dye"));
			COLORS_TO_ITEM_IDS.put(DyeColor.GRAY, new Identifier("minecraft", "gray_dye"));
			COLORS_TO_ITEM_IDS.put(DyeColor.LIGHT_GRAY, new Identifier("minecraft", "light_gray_dye"));
			COLORS_TO_ITEM_IDS.put(DyeColor.CYAN, new Identifier("minecraft", "cyan_dye"));
			COLORS_TO_ITEM_IDS.put(DyeColor.PURPLE, new Identifier("minecraft", "purple_dye"));
			COLORS_TO_ITEM_IDS.put(DyeColor.BLUE, new Identifier("minecraft", "blue_dye"));
			COLORS_TO_ITEM_IDS.put(DyeColor.BROWN, new Identifier("minecraft", "brown_dye"));
			COLORS_TO_ITEM_IDS.put(DyeColor.GREEN, new Identifier("minecraft", "green_dye"));
			COLORS_TO_ITEM_IDS.put(DyeColor.RED, new Identifier("minecraft", "red_dye"));
			COLORS_TO_ITEM_IDS.put(DyeColor.BLACK, new Identifier("minecraft", "black_dye"));
		}
	}
	
	public static class Theme implements Prop {
		private Theme(String name, boolean isTransparent) {
			this.name = name;
			this.isTransparent = isTransparent;
		}
		
		public AbstractBlock.Settings processSettings(AbstractBlock.Settings in) {
			if(isTransparent) {
				return in.nonOpaque();
			} else return in;
		}
		
		public final String name;
		public final boolean isTransparent; //kind of a hack rn
		
		public static final Theme CLASSIC = new Theme("classic", false);
		public static final Theme MODERN = new Theme("modern", false);
		public static final Theme LANTERN = new Theme("lantern", false);
		public static final Theme PULSATING = new Theme("pulsating", false);
		public static final Theme ICY = new Theme("icy", true);
		
		public static final List<Theme> ALL = ImmutableList.of(CLASSIC, MODERN, LANTERN, PULSATING, ICY);
		
		public String englishLocalization() {
			return WordUtils.capitalizeFully(name);
		}
	}
	
	public static class Mode implements Prop {
		private Mode(String name, BiFunction<LampStyle, Block.Settings, LampBlock> constructor) {
			this.name = name;
			this.constructor = constructor;
		}
		
		public final String name;
		public final BiFunction<LampStyle, Block.Settings, LampBlock> constructor;
		
		public static final Mode DIGITAL = new Mode("digital", LampBlock.Digital::new);
		public static final Mode ANALOG = new Mode("analog", LampBlock.Analog::new);
		
		public static final List<Mode> ALL = ImmutableList.of(DIGITAL, ANALOG);
		
		public String englishLocalization() {
			return WordUtils.capitalizeFully(name);
		}
	}
}
