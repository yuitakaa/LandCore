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
		ModelResourceLocation rl = new ModelResourceLocation(LandCore.MODID + ":" + id, variant);
		if (meta >= 0)  {
			ModelLoader.setCustomModelResourceLocation(item, meta, rl);
		} else {
			ModelLoader.setCustomMeshDefinition(item, stack -> rl);
			ModelBakery.registerItemVariants(item, rl);
		}
	}
}
