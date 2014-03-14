package tntrun.commands.setup.arena;

import org.bukkit.entity.Player;

import tntrun.TNTRun;
import tntrun.arena.Arena;
import tntrun.commands.setup.CommandHandlerInterface;

public class Disable implements CommandHandlerInterface {

	private TNTRun plugin;
	public Disable(TNTRun plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean handleCommand(Player player, String[] args) {
		Arena arena = plugin.amanager.getArenaByName(args[0]);
		if (arena != null) {
			if (arena.getStatusManager().isArenaEnabled()) {
				arena.getStatusManager().disableArena();
				player.sendMessage("Arena disabled");
			} else {
				player.sendMessage("Arena already disabled");
			}
		} else {
			player.sendMessage("Arena does not exist");
		}
		return true;
	}

}
