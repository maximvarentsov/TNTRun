package tntrun.commands.setup.arena;

import org.bukkit.entity.Player;

import tntrun.TNTRun;
import tntrun.arena.Arena;
import tntrun.commands.setup.CommandHandlerInterface;

public class SetSpawn implements CommandHandlerInterface {

	private TNTRun plugin;
	public SetSpawn(TNTRun plugin) {
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
			if (arena.getStructureManager().setSpawnPoint(player.getLocation())) {
				player.sendMessage("Spawnpoint set");
			} else {
				player.sendMessage("Spawnpoint should be in arena bounds");
			}
		} else {
			player.sendMessage("Arena does not exist");
		}
		return true;
	}

}