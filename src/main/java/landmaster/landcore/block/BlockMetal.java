package landmaster.landcore.block;

import java.util.*;

import landmaster.landcore.*;
import landmaster.landcore.util.*;
import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.creativetab.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.relauncher.*;

public class BlockMetal extends Block implements IMetaBlockName {
	public static final PropertyEnum<OreType> TYPE = PropertyEnum.create("type", OreType.class);
	
	public BlockMetal() {
		super(Material.IRON);
		setHarvestLevel("pickaxe", -1);
		setHardness(5);
		setDefaultState(blockState.getBaseState().withProperty(TYPE, OreType.THORIUM));
		setUnlocalizedName("block_metal").setRegistryName("block_metal");
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
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
		for (int i=0; i<OreType.values().length; ++i) {
			list.add(new ItemStack(itemIn, 1, i));
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
