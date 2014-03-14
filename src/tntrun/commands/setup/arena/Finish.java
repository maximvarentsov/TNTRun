package tntrun.commands.setup.arena;

import org.bukkit.entity.Player;

import tntrun.TNTRun;
import tntrun.arena.Arena;
import tntrun.commands.setup.CommandHandlerInterface;

public class Finish implements CommandHandlerInterface {

	private TNTRun plugin;
	public Finish(TNTRun plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean handleCommand(Player player, String[] args) {
		Arena arena = plugin.amanager.getArenaByName(args[0]);
		if (arena != null) {
			if (!arena.getStatusManager().isArenaEnabled()) {
				if (arena.getStructureManager().isArenaConfigured().equalsIgnoreCase("yes")) {
					arena.getStructureManager().saveToConfig();
					plugin.amanager.registerArena(arena);
					arena.getStatusManager().enableArena();
					player.sendMessage("Arena saved and enabled");
				} else {
					player.sendMessage("Arena is not configured. Reason: " + arena.getStructureManager().isArenaConfigured());
				}
			} else {
				player.sendMessage("Disable arena first");
			}
		} else {
			player.sendMessage("Arena does not exist");
		}
		return true;
	}

}