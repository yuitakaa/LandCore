package landmaster.landcore.item;

import java.util.*;

import landmaster.landcore.*;
import net.minecraft.client.resources.*;
import net.minecraft.creativetab.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.text.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.relauncher.*;

public class ItemEnergyWand extends ItemEnergyBase {
	public static final int CAP = 120000;
	public static final int MAX_INPUT = 150;
	public static final int ENERGY_COST = 600;
	
	public ItemEnergyWand() {
		super(CAP, MAX_INPUT, ENERGY_COST);
		setMaxStackSize(1);
		setUnlocalizedName("item_energy_wand").setRegistryName("item_energy_wand");
		setCreativeTab(LandCore.creativeTab);
	}
	
	@Override
    public boolean showDurabilityBar(ItemStack itemStack) {
		return true;
	}
	
	@Override
    public double getDurabilityForDisplay(ItemStack stack){
		return (double)(getMaxEnergyStored(stack) - getEnergyStored(stack)) / getMaxEnergyStored(stack);
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.BOW;
	}
	
	@Override
	protected ActionResult<ItemStack> clOnItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
		if (extractEnergy(playerIn.getHeldItem(hand), ENERGY_COST, true) < ENERGY_COST) {
			return new ActionResult<>(EnumActionResult.FAIL, playerIn.getHeldItem(hand));
		}
		extractEnergy(playerIn.getHeldItem(hand), ENERGY_COST, false);
		Vec3d vec = playerIn.getLookVec().scale(10);
		EntityFireball efb = new EntitySmallFireball(worldIn, playerIn, vec.xCoord, vec.yCoord, vec.zCoord);
		efb.posY += 1.0;
		worldIn.spawnEntityInWorld(efb);
		return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(hand));
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	protected void clGetSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
		ItemStack empty = new ItemStack(this), full = empty.copy();
		full.setTagCompound(new NBTTagCompound());
		full.getTagCompound().setInteger("Energy", CAP);
		subItems.add(empty);
		subItems.add(full);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		tooltip.add(TextFormatting.BLUE+I18n.format("tooltip.item_energy_wand.info"));
		tooltip.add(TextFormatting.GREEN+String.format("%d RF / %d RF", getEnergyStored(stack), getMaxEnergyStored(stack)));
	}
}
