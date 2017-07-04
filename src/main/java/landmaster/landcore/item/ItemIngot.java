package landmaster.landcore.item;

import landmaster.landcore.*;
import landmaster.landcore.util.*;
import net.minecraft.creativetab.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraftforge.fml.relauncher.*;

public class ItemIngot extends Item {
	public ItemIngot() {
		setHasSubtypes(true);
		setUnlocalizedName("item_ingot").setRegistryName("item_ingot");
		setCreativeTab(LandCore.creativeTab);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return super.getUnlocalizedName() + "." + OreType.values()[stack.getMetadata()];
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
		if (this.isInCreativeTab(tab)) {
			for (int i=0; i<OreType.values().length; ++i) {
				subItems.add(new ItemStack(this, 1, i));
			}
		}
	}
}
