package landmaster.landcore.config;

import net.minecraftforge.common.config.*;
import net.minecraftforge.fml.common.event.*;

public class Config extends Configuration {
	public static boolean thoriumTools;
	public static boolean tungstenTools;
	
	public static boolean thoriumArmor;
	public static boolean tungstenArmor;
	
	public static boolean energyWand;
	public static boolean landiumBow;
	
	public static int thoriumAmount, tungstenAmount, landiumAmount;
	
	public static boolean spawnLandlord;
	
	public Config(FMLPreInitializationEvent event) {
		super(event.getSuggestedConfigurationFile());
	}
	
	public void sync() {
		thoriumTools = getBoolean("Enable Thorium tools", "tools", true, "Enable Thorium tools");
		tungstenTools = getBoolean("Enable Tungsten tools", "tools", true, "Enable Tungsten tools");
		
		thoriumArmor = getBoolean("Enable Thorium armor", "armor", true, "Enable Thorium armor");
		tungstenArmor = getBoolean("Enable Tungsten armor", "armor", true, "Enable Tungsten armor");
		
		energyWand = getBoolean("Enable Energy wand", "tools", true, "Enable Energy wand");
		landiumBow = getBoolean("Enable Landium bow", "tools", true, "Enable Landium bow");
		
		thoriumAmount = getInt("Thorium veins per chunk", "generation", 9, 0, Integer.MAX_VALUE, "Thorium veins per chunk");
		tungstenAmount = getInt("Tungsten veins per chunk", "generation", 8, 0, Integer.MAX_VALUE, "Thorium veins per chunk");
		landiumAmount = getInt("Landium veins per chunk", "generation", 20, 0, Integer.MAX_VALUE, "Landium veins per chunk");
		
		spawnLandlord = getBoolean("Spawn Landlord", "spawn", true, "Enable Landlord spawn");
		
		if (hasChanged()) save();
	}
}
