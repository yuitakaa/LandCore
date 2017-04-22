package landmaster.landcore.util;

import java.lang.reflect.*;
import java.util.*;

import com.google.common.base.*;

import net.minecraft.world.biome.*;
import net.minecraftforge.common.*;

public class Utils {
	@SuppressWarnings("unchecked")
	public static Biome[] getBiomesForType(BiomeDictionary.Type type) {
		try {
			Object obj = getBiomesImplM.invoke(null, type);
			if (obj instanceof Set) {
				return ((Set<Biome>)getBiomesImplM.invoke(null, type)).toArray(new Biome[0]);
			}
			return (Biome[])obj;
		} catch (Throwable e) {
			throw Throwables.propagate(e);
		}
	}
	
	private static final Method getBiomesImplM;
	static {
		try {
			Method temp;
			try {
				temp = BiomeDictionary.class.getMethod("getBiomes", BiomeDictionary.Type.class);
			} catch (NoSuchMethodException error) {
				temp = BiomeDictionary.class.getMethod("getBiomesForType", BiomeDictionary.Type.class);
			}
			getBiomesImplM = temp;
		} catch (Throwable e) {
			throw Throwables.propagate(e);
		}
	}
}
