package agency.highlysuspect.dazzle2;

import agency.highlysuspect.dazzle2.block.DazzleBlocks;
import agency.highlysuspect.dazzle2.block.LampBlock;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
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
	
	public final Color color;
	public final Theme theme;
	public final Mode mode;
	public static final List<LampStyle> ALL = Lists.cartesianProduct(Color.ALL, Theme.ALL, Mode.ALL).stream().map(LampStyle::new).collect(Collectors.toList());
	public static final Map<String, LampStyle> LOOKUP = ALL.stream().collect(Collectors.toMap(LampStyle::toName, Function.identity()));
	
	public static LampStyle fromName(String name) {
		return LOOKUP.get(name);
	}
	
	public static BlockState findLampBlockstate(LampStyle.Color color, LampStyle.Theme theme, LampStyle.Mode mode) {
		LampStyle yes = new LampStyle(color, theme, mode);
		for(var haha : DazzleBlocks.LAMPS) {
			if(haha.get().style.equals(yes)) return haha.get().getDefaultState();
		}
		
		return Blocks.AIR.getDefaultState();
	}
	
	public String toName() {
		return color.color.getName() + '_' + theme.name + '_' + mode.name + "_lamp";
	}
	
	public Identifier toIdentifier() {
		return Init.id(toName());
	}
	
	public Supplier<LampBlock> instantiateBlock(Block.Settings settings) {
		return mode.constructor.apply(this, theme.processSettings(settings));
	}
	
	public LampBlock lookupBlock() {
		return (LampBlock) Registries.BLOCK.get(toIdentifier());
	}

	public LampStyle withMode(LampStyle.Mode newMode) {
		//This sucks
		LampStyle other = new LampStyle(color, theme, newMode);
		for(LampStyle a : ALL) {
			if(a.equals(other)) return a; //"""""cache"""""
		}
		return other;
	}
	
	@Override
	public int hashCode() {
		int result = color.hashCode();
		result = 31 * result + theme.hashCode();
		result = 31 * result + mode.hashCode();
		return result;
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		
		LampStyle lampStyle = (LampStyle) o;
		
		if(!color.equals(lampStyle.color)) return false;
		if(!theme.equals(lampStyle.theme)) return false;
		return mode.equals(lampStyle.mode);
	}
	
	public interface Prop {}
	
	//btw, because these have a private constructor (they're basically enums) there's no need to override equals and hashcode.
	public static class Color implements Prop {
		private Color(DyeColor color) {
			this.color = color;
		}
		
		public final DyeColor color;
		
		public static final List<Color> ALL = Arrays.stream(DyeColor.values()).map(Color::new).collect(Collectors.toList());
		
		public String getName() {
			return color.getName();
		}
	}
	
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType") //bad Java design decision, imo.
	public static class Theme implements Prop {
		private Theme(String name, Optional<Item> ingredient, boolean isTransparent) {
			this.name = name;
			this.ingredient = ingredient;
			this.isTransparent = isTransparent;
		}
		
		public final String name;
		public final boolean isTransparent; //kind of a hack rn
		public final Optional<Item> ingredient;
		
		public static final Theme CLASSIC = new Theme("classic", Optional.empty(), false);
		public static final Theme MODERN = new Theme("modern", Optional.of(Items.STONE_PRESSURE_PLATE), false);
		public static final Theme LANTERN = new Theme("lantern", Optional.of(Items.PRISMARINE_CRYSTALS), false);
		public static final Theme PULSATING = new Theme("pulsating", Optional.of(Items.END_ROD), false);
		public static final Theme ICY = new Theme("icy", Optional.of(Items.ICE), true);
		public static final List<Theme> ALL = ImmutableList.of(CLASSIC, MODERN, LANTERN, PULSATING, ICY);
		
		public AbstractBlock.Settings processSettings(AbstractBlock.Settings in) {
			if(isTransparent) {
				return in.nonOpaque();
			} else return in;
		}
		
		public String getName() {
			return name;
		}
	}
	
	public static class Mode implements Prop {
		private Mode(String name, BiFunction<LampStyle, Block.Settings, Supplier<LampBlock>> constructor) {
			this.name = name;
			this.constructor = constructor;
		}
		
		public final String name;
		public final BiFunction<LampStyle, Block.Settings, Supplier<LampBlock>> constructor;
		
		public static final Mode DIGITAL = new Mode("digital", (style, settings) -> () -> new LampBlock.Digital(style, settings));
		public static final Mode ANALOG = new Mode("analog", (style, settings) -> () -> new LampBlock.Analog(style, settings));
		
		public static final List<Mode> ALL = ImmutableList.of(DIGITAL, ANALOG);
		
		public String getName() {
			return name;
		}
	}
}
