package landmaster.landcore.proxy;

import landmaster.landcore.*;
import landmaster.landcore.entity.*;
import net.minecraft.item.*;
import net.minecraft.world.storage.loot.*;
import net.minecraftforge.fml.common.registry.*;

public class CommonProxy {
	public void registerItemRenderer(Item item, int meta, String id) {
	}
	
	public void registerItemRenderer(Item item, int meta, String id, String variant) {
	}
	
	public void preInitEntities() {
		int id = 0;
		EntityRegistry.registerModEntity(EntityLandlord.class, "landlord", id++, LandCore.INSTANCE, 64, 3, true, 0xFF0000, 0x000000);
		
		LootTableList.register(EntityLandlord.LOOT);
	}
}
