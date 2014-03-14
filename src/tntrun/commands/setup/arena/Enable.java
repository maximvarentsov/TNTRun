package tntrun.commands.setup.arena;

import org.bukkit.entity.Player;

import tntrun.TNTRun;
import tntrun.arena.Arena;
import tntrun.commands.setup.CommandHandlerInterface;

public class Enable implements CommandHandlerInterface {

	private TNTRun plugin;
	public Enable(TNTRun plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean handleCommand(Player player, String[] args) {
		Arena arena = plugin.amanager.getArenaByName(args[0]);
		if (arena != null) {
			if (arena.getStatusManager().isArenaEnabled()) {
				player.sendMessage("Arena already enabled.");
			} else {
				if (arena.getStatusManager().enableArena()) {
					player.sendMessage("Arena enabled");
				} else {
					player.sendMessage("Arena is not configured. Reason: " + arena.getStructureManager().isArenaConfigured());
				}
			}
		} else {
			player.sendMessage("Arena does not exist");
		}
		return true;
	}

}
