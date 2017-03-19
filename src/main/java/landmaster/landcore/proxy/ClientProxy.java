package landmaster.landcore.proxy;

import landmaster.landcore.*;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.item.*;
import net.minecraftforge.client.model.*;

public class ClientProxy extends CommonProxy {
	@Override
	public void registerItemRenderer(Item item, int meta, String id) {
	    registerItemRenderer(item, meta, id, "inventory");
	}
	
	@Override
	public void registerItemRenderer(Item item, int meta, String id, String variant) {
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(LandCore.MODID + ":" + id, variant));
	}
}
