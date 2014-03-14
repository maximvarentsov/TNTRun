package tntrun.commands.setup.selection;

import org.bukkit.entity.Player;

import tntrun.commands.setup.CommandHandlerInterface;
import tntrun.selectionget.PlayerSelection;

public class SetP1 implements CommandHandlerInterface {

	private PlayerSelection selection;
	public SetP1(PlayerSelection selection) {
		this.selection = selection;
	}

	@Override
	public boolean handleCommand(Player player, String[] args) {
		selection.setSelectionPoint1(player);
		player.sendMessage("p1 saved");
		return true;
	}

}