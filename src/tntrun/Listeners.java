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

package tntrun;

import com.google.common.collect.ImmutableList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import tntrun.arena.Arena;
import tntrun.arena.ArenasManager;
import tntrun.messages.Message;

import java.util.Collection;

class Listeners implements Listener {

	private final ArenasManager arenas;
    private final Collection<String> allowedcommands = ImmutableList.of(
            "/tntrun leave", "/tntrun vote", "/tr leave", "/tr vote"
    );

	public Listeners(final TNTRun plugin) {
		arenas = plugin.arenas;
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	@SuppressWarnings("unused")
    void onPlayerQuit(final PlayerQuitEvent event) {
		Player player = event.getPlayer();
		Arena arena = arenas.get(player.getName());
		if (arena != null) {
            arena.broadcast(Message.leave, player.getName());
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    @SuppressWarnings("unused")
	void onPlayerDeath(final PlayerDeathEvent event) {
		Player player = event.getEntity();
		Arena arena = arenas.get(player.getName());
		if (arena != null) {
            arena.broadcast(Message.leave, player.getName());
		}
	}
    /**
     * Handle damage based on arena settings.
     * Fall damage is always cancelled.
     *
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    @SuppressWarnings("unused")
    void onPlayerDamage(final EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Arena arena = arenas.get((Player) event.getEntity());
            if (arena != null) {
                if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                    event.setCancelled(true);
                    return;
                }
                switch (arena.getStructureManager().getDamageEnabled()) {
                    case YES:
                        break;
                    case NO:
                        event.setCancelled(true);
                        break;
                    case ZERO:
                        event.setDamage(0);
                        break;
                }
            }
        }
    }
    /**
     * Player should not be able to drop items while in arena.
     *
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    @SuppressWarnings("unused")
    void onPlayerItemDrop(final PlayerDropItemEvent event) {
        if (arenas.contains(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
    /**
     * Player have no hungry while play on arena.
     *
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    @SuppressWarnings("unused")
    void onFoodLevelChange(final FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (arenas.contains(player)) {
                event.setCancelled(true);
            }
        }
    }
    /**
     * player should not be able to issue any commands besides /tr leave and /tr vote while in arena.
     *
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    @SuppressWarnings("unused")
    void onPlayerCommand(final PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        Arena arena = arenas.get(player.getName());
        if (arena != null) {
            if (player.hasPermission("tntrun.cmdblockbypass")) {
                return;
            }
            if (!allowedcommands.contains(event.getMessage().toLowerCase())) {
                event.setCancelled(true);
            }
        }
    }
    /**
     * Player should not be able to break blocks while in arena.
     *
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    @SuppressWarnings("unused")
    void onBlockBreak(final BlockBreakEvent event) {
        if (arenas.contains(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
    /**
     * Player should not be able to place blocks while in arena.
     *
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    @SuppressWarnings("unused")
    void onBlockPlace(final BlockPlaceEvent event) {
        if (arenas.contains(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
    /**
     * Cancel all damage from and to spectators.
     *
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    @SuppressWarnings("unused")
    void onDamageByPlayer(final EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player player = (Player) event.getEntity();
            Player damager = (Player) event.getDamager();
            Arena arena = arenas.get(player);
            if (arena != null) {
                if (arena.getPlayersManager().isSpectator(player.getName()) || arena.getPlayersManager().isSpectator(damager.getName())) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
