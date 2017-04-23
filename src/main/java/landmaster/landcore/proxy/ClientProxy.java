package landmaster.landcore.proxy;

import landmaster.landcore.*;
import landmaster.landcore.entity.*;
import landmaster.landcore.entity.render.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.model.*;
import net.minecraftforge.common.*;
import net.minecraftforge.fml.client.registry.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class ClientProxy extends CommonProxy {
	public ClientProxy() {
		MinecraftForge.EVENT_BUS.register(this);
	}
	
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
	
	@Override
	public void preInitEntities() {
		super.preInitEntities();
		
		RenderingRegistry.registerEntityRenderingHandler(EntityLandlord.class, RenderEntityLandlord.FACTORY);
		RenderingRegistry.registerEntityRenderingHandler(EntityLandlordMagicFireball.class, RenderEntityLandlordMagicFireball.FACTORY);
	}
	
	@SubscribeEvent(priority = EventPriority.LOW)
	public void createCustomTextures(TextureStitchEvent.Pre event) {
		TextureMap texturemap = Minecraft.getMinecraft().getTextureMapBlocks();
		texturemap.registerSprite(new ResourceLocation("landcore:blocks/purple_fire_layer_0"));
		texturemap.registerSprite(new ResourceLocation("landcore:blocks/purple_fire_layer_1"));
	}
}
