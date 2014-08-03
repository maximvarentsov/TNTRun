/**
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 */

package tntrun.commands.setup.arena;

import org.bukkit.entity.Player;

import tntrun.TNTRun;
import tntrun.arena.Arena;
import tntrun.commands.setup.CommandHandlerInterface;
import tntrun.arena.ArenasManager;
import tntrun.messages.Message;
import tntrun.messages.Messages;
import tntrun.selectionget.PlayerCuboidSelection;
import tntrun.selectionget.PlayerSelection;

public class SetArena implements CommandHandlerInterface {

	private final ArenasManager arenas;
	private final PlayerSelection selection;

    public SetArena(final TNTRun plugin, final PlayerSelection playerSelection) {
		arenas = plugin.arenas;
		selection = playerSelection;
	}

	@Override
	public boolean handleCommand(final Player player, final String[] args) {
		Arena arena = arenas.get(args[0]);

        if (arena == null) {
            Messages.send(player, Message.arena_not_found, args[0]);
            return true;
        }

        if (arena.getStatusManager().isArenaEnabled()) {
            player.sendMessage("Disable arena first");
            return true;
        }

        PlayerCuboidSelection sel = selection.getPlayerSelection(player);

        if (sel != null) {
            arena.getStructureManager().setArenaPoints(sel.getMinimumLocation(), sel.getMaximumLocation());
            player.sendMessage("Arena bounds set");
        } else {
            player.sendMessage("Locations are wrong or not defined");
        }

		return true;
	}

    @Override
    public int getMinArgsLength() {
        return 1;
    }
}
