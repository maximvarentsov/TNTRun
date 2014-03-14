package tntrun.commands.setup.selection;

import org.bukkit.entity.Player;

import tntrun.commands.setup.CommandHandlerInterface;
import tntrun.selectionget.PlayerSelection;

public class Clear implements CommandHandlerInterface {

	private PlayerSelection selection;
	public Clear(PlayerSelection selection) {
		this.selection = selection;
	}

	@Override
	public boolean handleCommand(Player player, String[] args) {
		selection.clearSelectionPoints(player);
		player.sendMessage("points cleared");
		return true;
	}

}