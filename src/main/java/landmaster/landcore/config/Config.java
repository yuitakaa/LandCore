package landmaster.landcore.config;

import net.minecraftforge.common.config.*;
import net.minecraftforge.fml.common.event.*;

public class Config extends Configuration {
	public static boolean thoriumTools;
	public static boolean tungstenTools;
	
	public static boolean thoriumArmor;
	public static boolean tungstenArmor;
	
	public Config(FMLPreInitializationEvent event) {
		super(event.getSuggestedConfigurationFile());
	}
	
	public void sync() {
		thoriumTools = getBoolean("Enable Thorium tools", "tools", true, "Enable Thorium tools");
		tungstenTools = getBoolean("Enable Tungsten tools", "tools", true, "Enable Tungsten tools");
		
		thoriumArmor = getBoolean("Enable Thorium armor", "armor", true, "Enable Thorium armor");
		tungstenArmor = getBoolean("Enable Tungsten armor", "armor", true, "Enable Tungsten armor");
		
		if (hasChanged()) save();
	}
}
