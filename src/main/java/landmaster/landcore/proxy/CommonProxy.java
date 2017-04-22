package landmaster.landcore.proxy;

import landmaster.landcore.*;
import landmaster.landcore.entity.*;
import mcjty.lib.tools.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.world.storage.loot.*;

public class CommonProxy {
	public void registerItemRenderer(Item item, int meta, String id) {
	}
	
	public void registerItemRenderer(Item item, int meta, String id, String variant) {
	}
	
	public void preInitEntities() {
		int id = 0;
		EntityTools.registerModEntity(new ResourceLocation(LandCore.MODID, "landlord"),
				EntityLandlord.class, "landlord", id++, LandCore.INSTANCE, 64, 3, true, 0xFF0000, 0x000000);
		
		LootTableList.register(EntityLandlord.LOOT);
	}
}
