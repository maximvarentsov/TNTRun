package tntrun.commands.setup.arena;

import java.io.File;

import org.bukkit.entity.Player;

import tntrun.TNTRun;
import tntrun.arena.Arena;
import tntrun.commands.setup.CommandHandlerInterface;

public class Delete implements CommandHandlerInterface {

	private TNTRun plugin;
	public Delete(TNTRun plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean handleCommand(Player player, String[] args) {
		Arena arena = plugin.amanager.getArenaByName(args[0]);
		if (arena == null) {
			player.sendMessage("Arena does not exist");
			return true;
		}
		if (arena.getStatusManager().isArenaEnabled()) {
			player.sendMessage("Disable arena first");
			return true;
		}
		new File(plugin.getDataFolder() + File.separator + "arenas" + File.separator + arena.getArenaName() + ".yml").delete();
		plugin.signEditor.removeArena(arena.getArenaName());
		plugin.amanager.unregisterArena(arena);
		player.sendMessage("Arena deleted");
		return true;
	}

}