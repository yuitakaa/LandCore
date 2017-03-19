package landmaster.landcore.item;

import landmaster.landcore.*;
import net.minecraft.item.*;

public class ItemModSword extends ItemSword {
	public ItemModSword(ToolMaterial material, String name) {
		super(material);
		setUnlocalizedName(name).setRegistryName(name);
		setCreativeTab(LandCore.creativeTab);
	}
}
