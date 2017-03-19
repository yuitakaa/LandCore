package landmaster.landcore.item;

import landmaster.landcore.*;
import net.minecraft.inventory.*;

public class ItemModArmor extends net.minecraft.item.ItemArmor {
	public ItemModArmor(ArmorMaterial materialIn, EntityEquipmentSlot equipmentSlotIn, String name) {
		super(materialIn, 0, equipmentSlotIn);
		setUnlocalizedName(name).setRegistryName(name);
		setCreativeTab(LandCore.creativeTab);
	}
}
