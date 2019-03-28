package landmaster.landcore.api.packet;

import io.netty.buffer.*;
import net.minecraft.client.*;
import net.minecraft.entity.*;
import net.minecraft.util.math.*;
import net.minecraftforge.fml.common.network.simpleimpl.*;

public class PacketEntityMove implements IMessage {
	private int entityID;
	private Vec3d pos;
	
	public PacketEntityMove() {}
	public PacketEntityMove(int entityID, Vec3d pos) {
		this.entityID = entityID;
		this.pos = pos;
	}
	
	public IMessage handle(MessageContext ctx) {
		Minecraft.getMinecraft().addScheduledTask(() -> {
			Entity ent = Minecraft.getMinecraft().world.getEntityByID(entityID);
			if (ent != null) {
				ent.setLocationAndAngles(pos.x, pos.y, pos.z, ent.rotationYaw, ent.rotationPitch);
			}
		});
		return null;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		this.entityID = buf.readInt();
		this.pos = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(entityID).writeDouble(pos.x).writeDouble(pos.y).writeDouble(pos.z);
	}
	
}
