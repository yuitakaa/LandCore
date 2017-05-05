package landmaster.landcore.world;

import java.util.*;

import landmaster.landcore.*;
import landmaster.landcore.api.Tools;
import landmaster.landcore.block.*;
import landmaster.landcore.config.*;
import landmaster.landcore.util.*;
import net.minecraft.block.state.pattern.*;
import net.minecraft.init.*;
import net.minecraft.world.*;
import net.minecraft.world.chunk.*;
import net.minecraft.world.gen.*;
import net.minecraftforge.fml.common.*;

public class WorldGen implements IWorldGenerator {
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
		if (!(chunkGenerator instanceof ChunkProviderHell) && !(chunkGenerator instanceof ChunkProviderEnd)) {
			generateOres(random, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
		}
		if (!(chunkGenerator instanceof ChunkProviderOverworld) && !(chunkGenerator instanceof ChunkProviderEnd)) {
			generateNetherOres(random, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
		}
	}
	
	private static void generateOres(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		Tools.generateOre(
				LandCore.blockOre.getDefaultState()
				.withProperty(BlockOre.TYPE, OreType.THORIUM),
				world, random, chunkX*16, chunkZ*16, 10, 52, 4, 7, Config.thoriumAmount);
		Tools.generateOre(
				LandCore.blockOre.getDefaultState()
				.withProperty(BlockOre.TYPE, OreType.TUNGSTEN),
				world, random, chunkX*16, chunkZ*16, 10, 37, 3, 6, Config.tungstenAmount);
	}
	
	private static void generateNetherOres(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		Tools.generateOre(
				LandCore.blockOre.getDefaultState()
				.withProperty(BlockOre.TYPE, OreType.LANDIUM),
				world, BlockMatcher.forBlock(Blocks.NETHERRACK),
				random, chunkX*16, chunkZ*16, 7, 117, 1, 4, Config.landiumAmount);
	}
}
