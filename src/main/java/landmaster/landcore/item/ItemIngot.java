package landmaster.landcore.item;

import java.util.*;

import landmaster.landcore.*;
import landmaster.landcore.util.*;
import mcjty.lib.compat.*;
import net.minecraft.creativetab.*;
import net.minecraft.item.*;
import net.minecraftforge.fml.relauncher.*;

public class ItemIngot extends CompatItem {
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
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
		for (int i=0; i<OreType.values().length; ++i) {
			subItems.add(new ItemStack(itemIn, 1, i));
		}
	}
}
