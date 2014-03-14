package tntrun.commands.setup.arena;

import org.bukkit.entity.Player;

import tntrun.TNTRun;
import tntrun.arena.Arena;
import tntrun.arena.structure.StructureManager.TeleportDestination;
import tntrun.commands.setup.CommandHandlerInterface;

public class SetTeleport implements CommandHandlerInterface {

	private TNTRun plugin;
	public SetTeleport(TNTRun plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean handleCommand(Player player, String[] args) {
		Arena arena = plugin.amanager.getArenaByName(args[0]);
		if (arena != null) {
			if (arena.getStatusManager().isArenaEnabled()) {
				player.sendMessage("Disable arena first");
				return true;
			}
			if (args[1].equals("previous")) {
				arena.getStructureManager().setTeleportDestination(TeleportDestination.PREVIOUS);
			} else if (args[1].equals("lobby")) {
				if (plugin.globallobby.isLobbyLocationSet()) {
					arena.getStructureManager().setTeleportDestination(TeleportDestination.LOBBY);
				} else {
					player.sendMessage("Global lobby is not set");
				}
			}
			player.sendMessage("Teleport destination set");
		} else {
			player.sendMessage("Arena does not exist");
		}
		return true;
	}

}
