package landmaster.landcore.item;

import java.util.*;
import java.util.Locale;

import javax.annotation.*;

import landmaster.landcore.*;
import landmaster.landcore.api.*;
import landmaster.landcore.entity.*;
import net.minecraft.client.resources.*;
import net.minecraft.creativetab.*;
import net.minecraft.entity.*;
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
	
	public static enum Mode {
		NORMAL, MAGIC;
		
		public String getUnlocalizedName() {
			return "wandmode."+this.name().toLowerCase(Locale.US);
		}
	}
	
	public ItemEnergyWand() {
		super(CAP, MAX_INPUT, ENERGY_COST);
		setMaxStackSize(1);
		setUnlocalizedName("item_energy_wand").setRegistryName("item_energy_wand");
		setCreativeTab(LandCore.creativeTab);
	}
	
	public @Nonnull Mode getMode(ItemStack is) {
		return Mode.values()[Tools.getTagSafe(is,true).getInteger("Mode")];
	}
	
	public void setMode(ItemStack is, @Nonnull Mode mode) {
		Tools.getTagSafe(is,true).setInteger("Mode", mode.ordinal());
	}
	
	public void rotateMode(ItemStack is) {
		final Mode[] values = Mode.values();
		setMode(is, values[(getMode(is).ordinal()+1) % values.length]);
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
		if (worldIn.isRemote) return new ActionResult<>(EnumActionResult.PASS, playerIn.getHeldItem(hand));
		if (!playerIn.isSneaking()) {
			if (extractEnergy(playerIn.getHeldItem(hand), ENERGY_COST, true) < ENERGY_COST) {
				return new ActionResult<>(EnumActionResult.FAIL, playerIn.getHeldItem(hand));
			}
			extractEnergy(playerIn.getHeldItem(hand), ENERGY_COST, false);
			Vec3d vec = playerIn.getLookVec().scale(10);
			Entity efb = null;
			switch (getMode(playerIn.getHeldItem(hand))) {
			case NORMAL:
				efb = new EntitySmallFireball(worldIn, playerIn, vec.xCoord, vec.yCoord, vec.zCoord);
				break;
			case MAGIC:
				efb = new EntityLandlordMagicFireball(worldIn, playerIn, vec.xCoord, vec.yCoord, vec.zCoord);
				break;
			}
			efb.posY += 1.0;
			worldIn.spawnEntityInWorld(efb);
			return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(hand));
		} else {
			rotateMode(playerIn.getHeldItem(hand));
			playerIn.addChatMessage(new TextComponentTranslation("message.wand.change",
					new TextComponentTranslation(getMode(playerIn.getHeldItem(hand)).getUnlocalizedName())));
			return new ActionResult<>(EnumActionResult.PASS, playerIn.getHeldItem(hand));
		}
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
	public void clAddInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		tooltip.add(TextFormatting.BLUE+I18n.format("tooltip.item_energy_wand.info"));
		tooltip.add(TextFormatting.GREEN+String.format("%d RF / %d RF", getEnergyStored(stack), getMaxEnergyStored(stack)));
		tooltip.add(I18n.format("tooltip.item_energy_wand.mode", I18n.format(getMode(stack).getUnlocalizedName())));
	}
}
