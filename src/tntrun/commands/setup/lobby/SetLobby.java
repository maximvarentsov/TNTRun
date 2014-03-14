package tntrun.commands.setup.lobby;

import org.bukkit.entity.Player;

import tntrun.TNTRun;
import tntrun.commands.setup.CommandHandlerInterface;

public class SetLobby implements CommandHandlerInterface {
	
	private TNTRun plugin;
	public SetLobby(TNTRun plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean handleCommand(Player player, String[] args) {
		plugin.globallobby.setLobbyLocation(player.getLocation());
		player.sendMessage("Lobby set");
		return true;
	}

}