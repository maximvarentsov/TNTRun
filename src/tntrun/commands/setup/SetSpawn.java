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

package tntrun.commands.setup;

import org.bukkit.entity.Player;

import tntrun.TNTRun;
import tntrun.arena.Arena;
import tntrun.commands.CommandHandler;
import tntrun.messages.Message;
import tntrun.messages.Messages;

public class SetSpawn implements CommandHandler {

	private final TNTRun plugin;

	public SetSpawn(final TNTRun plugin) {
		this.plugin = plugin;
	}

	@Override
	public String handleCommand(Player player, String[] args) {
		Arena arena = plugin.arenas.get(args[0]);

        if (arena == null) {
            return Messages.getMessage(Message.arena_not_found, args[0]);
        }

        if (arena.getStatusManager().isArenaEnabled()) {
            return Messages.getMessage(Message.disable_arena_first, args[0]);
        }

        if (arena.getStructureManager().setSpawnPoint(player.getLocation())) {
            return "Spawnpoint set";
        }

        return "Spawnpoint should be in arena bounds";
	}

    @Override
    public int getMinArgsLength() {
        return 1;
    }
}