package landmaster.landcore;

import java.util.*;

import org.apache.commons.lang3.*;

import landmaster.landcore.api.item.*;
import landmaster.landcore.block.*;
import landmaster.landcore.command.*;
import landmaster.landcore.config.*;
import landmaster.landcore.entity.*;
import landmaster.landcore.item.*;
import landmaster.landcore.proxy.*;
import landmaster.landcore.util.*;
import landmaster.landcore.world.*;
import mcjty.lib.compat.*;
import mcjty.lib.tools.*;
import net.minecraft.entity.*;
import net.minecraft.init.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraftforge.common.*;
import net.minecraftforge.common.util.*;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.Mod.*;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.registry.*;
import net.minecraftforge.oredict.*;

@Mod(modid = LandCore.MODID, name = LandCore.NAME, version = LandCore.VERSION, dependencies = LandCore.DEPENDS, useMetadata = true, acceptedMinecraftVersions = "[1.9,1.12)")
public class LandCore {
	public static final String MODID = "landcore";
	public static final String NAME = "LandCore";
	public static final String VERSION = "1.3.5.0";
	public static final String DEPENDS = "required-after:compatlayer@[0.2.8,)";
	
	@Mod.Instance(MODID)
	public static LandCore INSTANCE;
	
	@SidedProxy(serverSide = "landmaster.landcore.proxy.CommonProxy", clientSide = "landmaster.landcore.proxy.ClientProxy")
	public static CommonProxy proxy;
	
	public static Config config;
	
	public static final CompatCreativeTabs creativeTab = new CompatCreativeTabs(MODID) {
		@Override
		public Item getItem() {
			return itemIngot;
		}
	};
	
	public static final BlockOre blockOre = new BlockOre();
	public static final BlockMetal blockMetal = new BlockMetal();
	
	public static final ItemIngot itemIngot = new ItemIngot();
	public static final ItemEnergyWand itemEnergyWand = new ItemEnergyWand();
	
	public static final Item.ToolMaterial thoriumToolMaterial = EnumHelper.addToolMaterial("THORIUM", 2, 500, 6.5f, 2.5f, 17);
	public static final Item.ToolMaterial tungstenToolMaterial = EnumHelper.addToolMaterial("TUNGSTEN", 2, 700, 9.0f, 2.75f, 15);
	
	public static final ItemModSword thoriumSword = new ItemModSword(thoriumToolMaterial, "item_thorium_sword");
	public static final ItemModPickaxe thoriumPick = new ItemModPickaxe(thoriumToolMaterial, "item_thorium_pickaxe");
	public static final ItemModAxe thoriumAxe = new ItemModAxe(thoriumToolMaterial, 8.0f, -3.1f, "item_thorium_axe");
	public static final ItemModShovel thoriumShovel = new ItemModShovel(thoriumToolMaterial, "item_thorium_shovel");
	public static final ItemModHoe thoriumHoe = new ItemModHoe(thoriumToolMaterial, "item_thorium_hoe");
	
	public static final ItemModSword tungstenSword = new ItemModSword(tungstenToolMaterial, "item_tungsten_sword");
	public static final ItemModPickaxe tungstenPick = new ItemModPickaxe(tungstenToolMaterial, "item_tungsten_pickaxe");
	public static final ItemModAxe tungstenAxe = new ItemModAxe(tungstenToolMaterial, 8.0f, -3.1f, "item_tungsten_axe");
	public static final ItemModShovel tungstenShovel = new ItemModShovel(tungstenToolMaterial, "item_tungsten_shovel");
	public static final ItemModHoe tungstenHoe = new ItemModHoe(tungstenToolMaterial, "item_tungsten_hoe");
	
	public static final ItemModBow landiumBow = new ItemModBow(5, 1500, 3f, 8, "item_landium_bow");
	
	public static final ItemArmor.ArmorMaterial thoriumArmorMaterial = EnumHelper.addArmorMaterial(
			"THORIUM", MODID+":thorium", 19, new int[] {2,7,6,2},
			13, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.5f);
	public static final ItemArmor.ArmorMaterial tungstenArmorMaterial = EnumHelper.addArmorMaterial(
			"TUNGSTEN", MODID+":tungsten", 25, new int[] {3,7,6,3},
			10, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 1.0f);
	
	public static final ItemModArmor thoriumHelmet = new ItemModArmor(
			thoriumArmorMaterial, EntityEquipmentSlot.HEAD, "item_thorium_helmet");
	public static final ItemModArmor thoriumChestplate = new ItemModArmor(
			thoriumArmorMaterial, EntityEquipmentSlot.CHEST, "item_thorium_chestplate");
	public static final ItemModArmor thoriumLeggings = new ItemModArmor(
			thoriumArmorMaterial, EntityEquipmentSlot.LEGS, "item_thorium_leggings");
	public static final ItemModArmor thoriumBoots = new ItemModArmor(
			thoriumArmorMaterial, EntityEquipmentSlot.FEET, "item_thorium_boots");
	
	public static final ItemModArmor tungstenHelmet = new ItemModArmor(
			tungstenArmorMaterial, EntityEquipmentSlot.HEAD, "item_tungsten_helmet");
	public static final ItemModArmor tungstenChestplate = new ItemModArmor(
			tungstenArmorMaterial, EntityEquipmentSlot.CHEST, "item_tungsten_chestplate");
	public static final ItemModArmor tungstenLeggings = new ItemModArmor(
			tungstenArmorMaterial, EntityEquipmentSlot.LEGS, "item_tungsten_leggings");
	public static final ItemModArmor tungstenBoots = new ItemModArmor(
			tungstenArmorMaterial, EntityEquipmentSlot.FEET, "item_tungsten_boots");
	
	public static final List<List<Item>> toolItems = new ArrayList<>();
	public static final List<List<Item>> armorItems = new ArrayList<>();
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		(config = new Config(event)).sync();
		
		initMetals();
		initTools();
		initArmor();
		
		proxy.preInitEntities();
		
		GameRegistry.registerWorldGenerator(new WorldGen(), 3);
	}
	
	private void initMetals() {
		GameRegistry.register(blockOre);
		GameRegistry.register(new ItemBlockMeta(blockOre), blockOre.getRegistryName());
		
		GameRegistry.register(itemIngot);
		
		GameRegistry.register(blockMetal);
		GameRegistry.register(new ItemBlockMeta(blockMetal), blockMetal.getRegistryName());
		
		{
			OreType[] values = OreType.values();
			for (int i=0; i<values.length; ++i) {
				proxy.registerItemRenderer(Item.getItemFromBlock(blockOre), i,
						blockOre.getRegistryName().getResourcePath(), "type="+values[i]);
				OreDictionary.registerOre("ore"+StringUtils.capitalize(values[i].toString()),
						new ItemStack(blockOre, 1, i));
				proxy.registerItemRenderer(itemIngot, i, values[i]+"ingot");
				OreDictionary.registerOre("ingot"+StringUtils.capitalize(values[i].toString()),
						new ItemStack(itemIngot, 1, i));
				proxy.registerItemRenderer(Item.getItemFromBlock(blockMetal), i,
						blockMetal.getRegistryName().getResourcePath(), "type="+values[i]);
				OreDictionary.registerOre("block"+StringUtils.capitalize(values[i].toString()),
						new ItemStack(blockMetal, 1, i));
			}
		}
	}
	
	private void initTools() {
		toolItems.add(Config.thoriumTools
				? Arrays.asList(thoriumSword, thoriumPick, thoriumAxe, thoriumShovel, thoriumHoe)
						: null);
		toolItems.add(Config.tungstenTools
				? Arrays.asList(tungstenSword, tungstenPick, tungstenAxe, tungstenShovel, tungstenHoe)
						: null);
		toolItems.add(null);
		
		{
			OreType[] values = OreType.values();
			String[] toolTypes = new String[] {"sword", "pickaxe", "axe", "shovel", "hoe"};
			for (int i=0; i<values.length; ++i) {
				List<Item> tools = toolItems.get(i);
				if (tools == null) continue;
				tools.forEach(item -> GameRegistry.register(item));
				for (int j=0; j<toolTypes.length; ++j) {
					proxy.registerItemRenderer(tools.get(j), 0, "tool/"+values[i]+toolTypes[j]);
				}
			}
		}
		
		if (Config.energyWand) {
			GameRegistry.register(itemEnergyWand);
			proxy.registerItemRenderer(itemEnergyWand, -1, "tool/energywand");
		}
		
		if (Config.landiumBow) {
			GameRegistry.register(landiumBow);
			proxy.registerItemRenderer(landiumBow, -1, "tool/landiumbow");
		}
	}
	
	private void initArmor() {
		armorItems.add(Config.thoriumArmor
				? Arrays.asList(thoriumHelmet, thoriumChestplate, thoriumLeggings, thoriumBoots)
						: null);
		armorItems.add(Config.tungstenArmor
				? Arrays.asList(tungstenHelmet, tungstenChestplate, tungstenLeggings, tungstenBoots)
						: null);
		armorItems.add(null);
		
		{
			OreType[] values = OreType.values();
			String[] armorTypes = new String[] {"helmet", "chestplate", "leggings", "boots"};
			for (int i=0; i<values.length; ++i) {
				List<Item> armorParts = armorItems.get(i);
				if (armorParts == null) continue;
				armorParts.forEach(item -> GameRegistry.register(item));
				for (int j=0; j<armorTypes.length; ++j) {
					proxy.registerItemRenderer(armorParts.get(j), 0, "armor/"+values[i]+armorTypes[j]);
				}
			}
		}
	}
	
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		if (Config.energyWand) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(itemEnergyWand),
					"T", "D", "W",
					'T', "ingotThorium", 'D', "gemDiamond", 'W', "ingotTungsten"));
		}
		
		if (Config.landiumBow) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(landiumBow),
					" IS", "I S", " IS",
					'I', "ingotLandium", 'S', Items.STRING));
		}
		
		for (int i=0; i<OreType.values().length; ++i) {
			final String ingotName = "ingot"+StringUtils.capitalize(OreType.values()[i].toString());
			
			GameRegistry.addSmelting(new ItemStack(blockOre, 1, i),
					new ItemStack(itemIngot, 1, i), 0.85f);
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockMetal, 1, i),
					"III", "III", "III",
					'I', ingotName));
			GameRegistry.addShapelessRecipe(new ItemStack(itemIngot, 9, i), new ItemStack(blockMetal, 1, i));
			
			List<Item> tools = toolItems.get(i);
			if (tools != null) {
				GameRegistry.addRecipe(new ShapedOreRecipe(tools.get(0),
						"I", "I", "S",
						'I', ingotName, 'S', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(tools.get(1),
						"III", " S ", " S ",
						'I', ingotName, 'S', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(tools.get(2),
						"II", "IS", " S",
						'I', ingotName, 'S', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(tools.get(3),
						"I", "S", "S",
						'I', ingotName, 'S', "stickWood"));
				GameRegistry.addRecipe(new ShapedOreRecipe(tools.get(4),
						"II", " S", " S",
						'I', ingotName, 'S', "stickWood"));
			}
			
			List<Item> armorParts = armorItems.get(i);
			if (armorParts != null) {
				GameRegistry.addRecipe(new ShapedOreRecipe(armorParts.get(0),
						"III", "I I",
						'I', ingotName));
				GameRegistry.addRecipe(new ShapedOreRecipe(armorParts.get(1),
						"I I", "III", "III",
						'I', ingotName));
				GameRegistry.addRecipe(new ShapedOreRecipe(armorParts.get(2),
						"III", "I I", "I I",
						'I', ingotName));
				GameRegistry.addRecipe(new ShapedOreRecipe(armorParts.get(3),
						"I I", "I I",
						'I', ingotName));
			}
		}
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		if (Config.spawnLandlord) {
			EntityRegistry.addSpawn(EntityLandlord.class, 14, 1, 3, EnumCreatureType.MONSTER, BiomeTools.getBiomesForType(BiomeDictionary.Type.NETHER));
		}
	}
	
	@EventHandler
    public void serverLoad(FMLServerStartingEvent event) {
		event.registerServerCommand(new TeleportCommand());
	}
}
