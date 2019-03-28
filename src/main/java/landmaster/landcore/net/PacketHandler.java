package landmaster.landcore.net;

import landmaster.landcore.*;
import landmaster.landcore.api.packet.*;
import net.minecraftforge.fml.common.network.*;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.*;

public class PacketHandler {
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(LandCore.MODID);
	
	public static void init() {
		INSTANCE.registerMessage(PacketEntityMove::handle, PacketEntityMove.class, 0, Side.CLIENT);
	}
}
