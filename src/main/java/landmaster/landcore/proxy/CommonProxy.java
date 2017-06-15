package landmaster.landcore.proxy;

import landmaster.landcore.*;
import landmaster.landcore.entity.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.world.storage.loot.*;
import net.minecraftforge.fml.common.registry.*;

public class CommonProxy {
	public void registerItemRenderer(Item item, int meta, String id) {
	}
	
	public void registerItemRenderer(Item item, int meta, String id, String variant) {
	}
	
	public void preInitEntities() {
		EntityRegistry.registerModEntity(new ResourceLocation(LandCore.MODID, "landlord"),
				EntityLandlord.class, "landlord", 0, LandCore.INSTANCE, 64, 3, true, 0xFF0000, 0x000000);
		EntityRegistry.registerModEntity(new ResourceLocation(LandCore.MODID, "landlord_magic_fireball"),
				EntityLandlordMagicFireball.class, "landlord_magic_fireball", 1, LandCore.INSTANCE,
				64, 1, true);
		
		LootTableList.register(EntityLandlord.LOOT);
	}
}
