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

public class CreateArena implements CommandHandlerInterface {

	private final TNTRun plugin;

    public CreateArena(final TNTRun plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean handleCommand(Player player, String[] args) {
		Arena arenac = plugin.arenas.get(args[0]);
		if (arenac != null) {
			player.sendMessage("Arena already exists");
			return true;
		}
		Arena arena = new Arena(args[0], plugin);
		plugin.arenas.add(arena);
		player.sendMessage("Arena created");
		return true;
	}

    @Override
    public int getMinArgsLength() {
        return 1;
    }
}
