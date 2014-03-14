package tntrun.commands.setup.arena;

import org.bukkit.entity.Player;

import tntrun.TNTRun;
import tntrun.arena.Arena;
import tntrun.commands.setup.CommandHandlerInterface;

public class Create implements CommandHandlerInterface {

	private TNTRun plugin;
	public Create(TNTRun plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean handleCommand(Player player, String[] args) {
		Arena arenac = plugin.amanager.getArenaByName(args[0]);
		if (arenac != null) {
			player.sendMessage("Arena already exists");
			return true;
		}
		Arena arena = new Arena(args[0], plugin);
		plugin.amanager.registerArena(arena);
		player.sendMessage("Arena created");
		return true;
	}

}