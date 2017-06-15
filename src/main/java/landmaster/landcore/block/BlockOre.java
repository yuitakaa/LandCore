package landmaster.landcore.block;

import landmaster.landcore.*;
import landmaster.landcore.api.block.*;
import landmaster.landcore.util.*;
import net.minecraft.block.Block;
import net.minecraft.block.material.*;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.creativetab.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.relauncher.*;

public class BlockOre extends Block implements IMetaBlockName {
	public static final PropertyEnum<OreType> TYPE = PropertyEnum.create("type", OreType.class);
	
	public BlockOre() {
		super(Material.ROCK);
		setDefaultState(blockState.getBaseState().withProperty(TYPE, OreType.THORIUM));
		setHardness(3f);
		setResistance(5f);
		setHarvestLevel("pickaxe", 1, getDefaultState().withProperty(TYPE, OreType.THORIUM));
		setHarvestLevel("pickaxe", 2, getDefaultState().withProperty(TYPE, OreType.TUNGSTEN));
		setHarvestLevel("pickaxe", 3, getDefaultState().withProperty(TYPE, OreType.LANDIUM));
		setUnlocalizedName("block_ore").setRegistryName("block_ore");
		setCreativeTab(LandCore.creativeTab);
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
	    return new BlockStateContainer(this, TYPE);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(TYPE, OreType.values()[meta]);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		OreType type = (OreType)state.getValue(TYPE);
		return type.ordinal();
	}
	
	@Override
	public int damageDropped(IBlockState state) {
	    return getMetaFromState(state);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
		for (int i=0; i<OreType.values().length; ++i) {
			items.add(new ItemStack(this, 1, i));
		}
	}
	
	@Override
	public String getSpecialName(ItemStack stack) {
		return OreType.values()[stack.getMetadata()].toString();
	}
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(Item.getItemFromBlock(this), 1, getMetaFromState(state));
	}
}
