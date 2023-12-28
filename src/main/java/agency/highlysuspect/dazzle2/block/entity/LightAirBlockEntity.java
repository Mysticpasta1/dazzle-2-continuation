package agency.highlysuspect.dazzle2.block.entity;

import agency.highlysuspect.dazzle2.Init;
import agency.highlysuspect.dazzle2.block.DazzleBlocks;
import agency.highlysuspect.dazzle2.block.LightAirBlock;
import agency.highlysuspect.dazzle2.block.ProjectedLightPanelBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.AutomaticItemPlacementContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;

import java.util.Optional;

public class LightAirBlockEntity extends BlockEntity {
	public LightAirBlockEntity(BlockPos pos, BlockState state) {
		super(DazzleBlockEntityTypes.LIGHT_AIR.get(), pos, state);
	}
	
	//todo this isn't extensible to other reasons that nonplaceable hidden lights might be created, which was the intention.
	// might want to try something else instead
	private BlockPos panelPos;
	
	private static Optional<Direction> findDirBetween(BlockPos selfPos, BlockPos panelPos) {
		BlockPos diff = selfPos.subtract(panelPos);
		
		if(diff.getX() == 0 && diff.getY() == 0 && diff.getZ() == 0) return Optional.empty();
			
			//x
		else if(diff.getY() == 0 && diff.getZ() == 0) return Optional.of(diff.getX() > 0 ? Direction.EAST : Direction.WEST);
			//y
		else if(diff.getX() == 0 && diff.getZ() == 0) return Optional.of(diff.getY() > 0 ? Direction.UP : Direction.DOWN);
			//z
		else if(diff.getX() == 0 && diff.getY() == 0)
			return Optional.of(diff.getZ() > 0 ? Direction.SOUTH : Direction.NORTH);
		
		else return Optional.empty();
	}
	
	//Returns TRUE when the light should stay, FALSE when it has been removed, and DEFAULT when it's indeterminate
	public void check() {
		assert world != null;
		assert pos != null;
		
		if(!world.getBlockState(pos).isOf(DazzleBlocks.LIGHT_AIR.get())) {
			//what happpened here?
			Init.log("stale light BE at {}?", pos);
			return;
		}
		
		//Make sure that I'm in line with the panel
		Optional<Direction> dirBetween_ = findDirBetween(pos, panelPos);
		if(dirBetween_.isEmpty()) return;
		Direction dirBetween = dirBetween_.get();
		
		//don't worry about it right now, wait for the panel to be loaded as well
		if(!world.isChunkLoaded(panelPos.getX() >> 4, panelPos.getZ() >> 4)) return;
		
		//Make sure it's actually a light panel at this location
		BlockState panelState = world.getBlockState(panelPos);
		if(!panelState.isOf(DazzleBlocks.PROJECTED_LIGHT_PANEL.get())) {
			bail();
			return;
		}
		
		//Make sure the panel is pointing towards me
		Direction panelDirection = panelState.get(ProjectedLightPanelBlock.FACING);
		if(dirBetween != panelDirection) {
			bail();
			return;
		}
		
		//Make sure I have the right power level for the panel's power level
		int distance = pos.getManhattanDistance(panelPos);
		int stepsAway = MathHelper.ceil(distance / (float) ProjectedLightPanelBlock.BEAM_SEGMENT_LENGTH);
		if(16 - stepsAway <= 0) { //light can't reach here even at full power
			bail();
			return;
		}
		int panelPower = panelState.get(ProjectedLightPanelBlock.POWER);
		int expectedLightLevel = panelPower - stepsAway + 1;
		if(expectedLightLevel <= 0) {
			bail();
			return;
		} else if(getCachedState().get(LightAirBlock.LIGHT) != expectedLightLevel) {
			world.setBlockState(pos, getCachedState().with(LightAirBlock.LIGHT, expectedLightLevel));
		}
		
		//Make sure the path to the panel is not obstructed
		ItemPlacementContext haha = new AutomaticItemPlacementContext(world, pos, Direction.UP, ItemStack.EMPTY, Direction.UP);
		for(BlockPos iterpos : BlockPos.iterate(pos, panelPos)) {
			BlockState there = world.getBlockState(iterpos);
			if(there.isOf(DazzleBlocks.PROJECTED_LIGHT_PANEL.get()) || there.isOf(DazzleBlocks.LIGHT_AIR.get()) || there.canReplace(haha))
				continue;
			
			bail();
			return;
		}
	}
	
	private void bail() {
		assert world != null;
		world.setBlockState(pos, Blocks.AIR.getDefaultState());
	}
	
	public boolean belongsTo(BlockPos panelPos) {
		if(this.panelPos == null) return false; //hrm
		return this.panelPos.equals(panelPos);
	}
	
	public BlockPos getOwner() {
		return panelPos;
	}
	
	public void setOwner(BlockPos panelPos) {
		this.panelPos = panelPos;
		markDirty();
	}
	
	@Override
	public void readNbt(NbtCompound tag) {
		super.readNbt(tag);
		panelPos = NbtHelper.toBlockPos(tag.getCompound("LightPanelPos"));
	}
	
	@Override
	public void writeNbt(NbtCompound tag) {
		tag.put("LightPanelPos", NbtHelper.fromBlockPos(panelPos));
		super.writeNbt(tag);
	}
}
