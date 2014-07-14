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

import com.google.common.collect.ImmutableList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import tntrun.TNTRun;
import tntrun.arena.Arena;
import tntrun.datahandler.ArenasManager;

import java.util.Collection;

public class RestrictionHandler implements Listener {

    private final ArenasManager arenas;
    private final Collection<String> allowedcommands = ImmutableList.of(
            "/tntrun leave", "/tntrun vote", "/tr leave", "/tr vote"
    );

    public RestrictionHandler(final TNTRun plugin) {
        arenas = plugin.amanager;
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	@SuppressWarnings("unused")
    void onPlayerCommand(final PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		Arena arena = arenas.getPlayerArena(player.getName());
		if (arena != null) {
            if (player.hasPermission("tntrun.cmdblockbypass")) {
                return;
            }
            if (!allowedcommands.contains(event.getMessage().toLowerCase())) {
                event.setCancelled(true);
            }
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	@SuppressWarnings("unused")
    void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		Arena arena = arenas.getPlayerArena(player.getName());
		if (arena != null) {
            event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    @SuppressWarnings("unused")
    void onBlockPlace(final BlockPlaceEvent event) {
		Player player = event.getPlayer();
		Arena arena = arenas.getPlayerArena(player.getName());
		if (arena != null) {
            event.setCancelled(true);
		}
	}
}
