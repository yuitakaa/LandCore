package landmaster.landcore.command;

import landmaster.landcore.api.*;
import net.minecraft.command.*;
import net.minecraft.entity.player.*;
import net.minecraft.server.*;

public class TeleportCommand extends CommandBase {
	@Override
	public String getName() {
		return "tpinterdim";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length != 4) {
			throw new WrongUsageException(this.getUsage(sender));
		}
		if (sender instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP)sender;
			try {
				Coord4D coord = new Coord4D(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
				if (Tools.canTeleportTo(player, coord)) {
					Tools.teleportPlayerTo(player, coord);
				} else {
					throw new CommandException("message.command.noteleport");
				}
			} catch (NumberFormatException e) {
				throw new WrongUsageException(this.getUsage(sender));
			}
		}
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "tpinterdim <x> <y> <z> <id>";
	}
}
