package landmaster.landcore.util;

import java.util.*;

import net.minecraft.util.*;

public enum OreType implements IStringSerializable {
	THORIUM, TUNGSTEN;

	@Override
	public String getName() {
		return name().toLowerCase(Locale.US);
	}
	
	@Override
	public String toString() {
		return getName();
	}
}