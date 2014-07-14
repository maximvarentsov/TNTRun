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

public class SetGameLevelDestroyDelay implements CommandHandlerInterface {

	private TNTRun plugin;
	public SetGameLevelDestroyDelay(TNTRun plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean handleCommand(Player player, String[] args) {
		Arena arena = plugin.arenas.getArenaByName(args[0]);
		if (arena != null) {
			if (arena.getStatusManager().isArenaEnabled()) {
				player.sendMessage("Disable arena first");
				return true;
			}
			arena.getStructureManager().setGameLevelDestroyDelay(Integer.valueOf(args[1]));
			player.sendMessage("GameLevel blocks destroy delay set");
		} else {
			player.sendMessage("Arena does not exist");
		}
		return true;
	}

}