package landmaster.landcore.world;

import java.util.*;

import com.google.common.base.*;

import landmaster.landcore.*;
import landmaster.landcore.block.*;
import landmaster.landcore.config.*;
import landmaster.landcore.util.*;
import net.minecraft.block.state.*;
import net.minecraft.block.state.pattern.*;
import net.minecraft.init.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.world.chunk.*;
import net.minecraft.world.gen.*;
import net.minecraft.world.gen.feature.*;
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
	
	private void generateOres(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		generateOre(
				LandCore.blockOre.getDefaultState()
				.withProperty(BlockOre.TYPE, OreType.THORIUM),
				world, random, chunkX*16, chunkZ*16, 10, 52, 4, 7, Config.thoriumAmount);
		generateOre(
				LandCore.blockOre.getDefaultState()
				.withProperty(BlockOre.TYPE, OreType.TUNGSTEN),
				world, random, chunkX*16, chunkZ*16, 10, 37, 3, 6, Config.tungstenAmount);
	}
	
	private void generateNetherOres(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		generateOre(
				LandCore.blockOre.getDefaultState()
				.withProperty(BlockOre.TYPE, OreType.LANDIUM),
				world, BlockMatcher.forBlock(Blocks.NETHERRACK),
				random, chunkX*16, chunkZ*16, 7, 117, 1, 4, Config.landiumAmount);
	}
	
	private void generateOre(IBlockState ore, World world, Random random, int x, int z, int minY, int maxY, int minSize, int maxSize, int chances) {
		generateOre(ore, world, BlockMatcher.forBlock(Blocks.STONE), random, x, z, minY, maxY, minSize, maxSize, chances);
	}
	
	private void generateOre(IBlockState ore, World world, Predicate<IBlockState> matcher, Random random, int x, int z, int minY, int maxY, int minSize, int maxSize, int chances) {
		int deltaY = maxY - minY;
		int deltaSize = maxSize - minSize;
		for (int i = 0; i < chances; i++) {
			BlockPos pos = new BlockPos(x + random.nextInt(16), minY + random.nextInt(deltaY), z + random.nextInt(16));
			WorldGenMinable generator = new WorldGenMinable(ore, minSize + random.nextInt(deltaSize+1), matcher);
			generator.generate(world, random, pos);
		}
	}
}
