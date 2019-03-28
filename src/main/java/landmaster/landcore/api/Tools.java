package landmaster.landcore.api;

import java.util.*;

import com.google.common.base.*;

import landmaster.landcore.api.packet.*;
import landmaster.landcore.net.*;
import net.minecraft.block.state.*;
import net.minecraft.block.state.pattern.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.world.gen.feature.*;

public class Tools {
	public static ResourceLocation suffix(ResourceLocation original, Object suffix) {
		return new ResourceLocation(original.getNamespace(), original.getPath()+suffix);
	}
	
	public static ResourceLocation underscoreSuffix(ResourceLocation original, Object suffix) {
		return suffix(original, "_"+suffix);
	}
	
	public static boolean canTeleportTo(EntityPlayer player, Coord4D dest) {
		if (dest == null || dest.world() == null) return false;
		for (int i=1; i<=2; ++i) {
			if (dest.add(0, i, 0).blockState().getCollisionBoundingBox(dest.world(), dest.pos()) != null) {
				return false;
			}
		}
		return true;
	}
	
	public static NBTTagCompound getTagSafe(ItemStack is, boolean set) {
		if (is.isEmpty()) {
			return new NBTTagCompound();
		}
		NBTTagCompound nbt = is.getTagCompound();
		if (nbt == null) {
			nbt = new NBTTagCompound();
			if (set) is.setTagCompound(nbt);
		}
		return nbt;
	}
	
	// thanks Mekanism
	public static void teleportPlayerTo(EntityPlayerMP player, Coord4D coord) {
		if (player.dimension != coord.dimensionId) {
			player.changeDimension(coord.dimensionId, (world, entity, yaw) -> entity.setPositionAndUpdate(coord.xCoord +0.5, coord.yCoord +1, coord.zCoord +0.5));
		} else {
			player.setPositionAndUpdate(coord.xCoord +0.5, coord.yCoord +1, coord.zCoord +0.5);
		}
		player.world.updateEntityWithOptionalForce(player, true);
	}
	
	public static void teleportEntityTo(Entity entity, Coord4D coord) {
		if(entity.world.provider.getDimension() != coord.dimensionId) {
			entity.changeDimension(coord.dimensionId, (world, entity2, yaw) -> entity2.setPositionAndUpdate(coord.xCoord +0.5, coord.yCoord +1, coord.zCoord +0.5));
		} else {
			entity.setPositionAndUpdate(coord.xCoord +0.5, coord.yCoord +1, coord.zCoord +0.5);
			PacketHandler.INSTANCE.sendToAllTracking(new PacketEntityMove(entity.getEntityId(), entity.getPositionVector()), entity);
		}
	}

	public static void generateOre(IBlockState ore, World world, Random random, int x, int z, int minY, int maxY, int minSize, int maxSize, int chances) {
		Tools.generateOre(ore, world, BlockMatcher.forBlock(Blocks.STONE), random, x, z, minY, maxY, minSize, maxSize, chances);
	}

	public static void generateOre(IBlockState ore, World world, Predicate<IBlockState> matcher, Random random, int x, int z, int minY, int maxY, int minSize, int maxSize, int chances) {
		int deltaY = maxY - minY;
		int deltaSize = maxSize - minSize;
		for (int i = 0; i < chances; i++) {
			BlockPos pos = new BlockPos(x + random.nextInt(16), minY + random.nextInt(deltaY), z + random.nextInt(16));
			WorldGenMinable generator = new WorldGenMinable(ore, minSize + random.nextInt(deltaSize+1), matcher);
			generator.generate(world, random, pos);
		}
	}
}
