package landmaster.landcore.item;

import landmaster.landcore.*;
import net.minecraft.item.*;

public class ItemModHoe extends ItemHoe {
	public ItemModHoe(ToolMaterial material, String name) {
		super(material);
		setUnlocalizedName(name).setRegistryName(name);
		setCreativeTab(LandCore.creativeTab);
	}
}
