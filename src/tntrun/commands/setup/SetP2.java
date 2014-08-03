package tntrun.commands.setup;

import org.bukkit.entity.Player;
import tntrun.commands.CommandHandlerInterface;
import tntrun.messages.Message;
import tntrun.messages.Messages;
import tntrun.selectionget.PlayerSelection;

public class SetP2 implements CommandHandlerInterface {

	private final PlayerSelection selection;

    public SetP2(final PlayerSelection selection) {
		this.selection = selection;
	}

	@Override
	public String handleCommand(final Player player, final String[] args) {
		selection.setSelectionPoint2(player);
        return Messages.getMessage(Message.p2_saved);
	}

    @Override
    public int getMinArgsLength() {
        return 0;
    }
}