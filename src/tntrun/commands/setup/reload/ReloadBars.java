package tntrun.commands.setup.reload;

import org.bukkit.entity.Player;

import tntrun.TNTRun;
import tntrun.bars.Bars;
import tntrun.commands.setup.CommandHandlerInterface;

public class ReloadBars implements CommandHandlerInterface {

	private TNTRun plugin;
	public ReloadBars(TNTRun plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean handleCommand(Player player, String[] args) {
		Bars.loadBars(plugin);
		player.sendMessage("nars reloaded");
		return true;
	}
	
}