package tntrun.commands.setup.reload;

import org.bukkit.entity.Player;

import tntrun.TNTRun;
import tntrun.commands.setup.CommandHandlerInterface;
import tntrun.messages.Messages;

public class ReloadMSG implements CommandHandlerInterface {

	private TNTRun plugin;
	public ReloadMSG(TNTRun plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean handleCommand(Player player, String[] args) {
		Messages.loadMessages(plugin);
		player.sendMessage("Messages reloaded");
		return true;
	}

}