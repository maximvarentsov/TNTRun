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

package tntrun.eventhandler;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import tntrun.TNTRun;
import tntrun.arena.Arena;
import tntrun.datahandler.ArenasManager;
import tntrun.messages.Messages;

public class PlayerLeaveArenaChecker implements Listener {

	private final ArenasManager arenas;

	public PlayerLeaveArenaChecker(final TNTRun plugin) {
		arenas = plugin.amanager;
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	@SuppressWarnings("unused")
    void onPlayerQuitEvent(final PlayerQuitEvent event) {
		Player player = event.getPlayer();
		Arena arena = arenas.getPlayerArena(player.getName());
		if (arena != null) {
            arena.getPlayerHandler().leavePlayer(player, "", Messages.playerlefttoothers);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    @SuppressWarnings("unused")
	void onPlayerDeathEvent(final PlayerDeathEvent event) {
		Player player = event.getEntity();
		Arena arena = arenas.getPlayerArena(player.getName());
		if (arena != null) {
            arena.getPlayerHandler().leavePlayer(player, "", Messages.playerlefttoothers);
		}
	}

}
