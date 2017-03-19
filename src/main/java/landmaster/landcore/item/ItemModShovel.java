package landmaster.landcore.item;

import landmaster.landcore.*;
import net.minecraft.item.*;

public class ItemModShovel extends ItemSpade {
	public ItemModShovel(ToolMaterial material, String name) {
		super(material);
		setUnlocalizedName(name).setRegistryName(name);
		setCreativeTab(LandCore.creativeTab);
	}
}
