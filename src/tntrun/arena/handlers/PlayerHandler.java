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

package tntrun.arena.handlers;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import tntrun.arena.Arena;
import tntrun.bars.Bars;
import tntrun.messages.Messages;

import java.util.HashSet;
import java.util.Set;

public class PlayerHandler {

	private final Arena arena;
    private final Set<String> votes = new HashSet<>();

	public PlayerHandler(final Arena arena) {
		this.arena = arena;
	}


	public void join(final Player player) {

        player.teleport(arena.getStructureManager().getSpawnPoint());

        for (Player aplayer : Bukkit.getOnlinePlayers()) {
			aplayer.showPlayer(player);
		}

        player.setGameMode(GameMode.SURVIVAL);
		player.setFlying(false);
		player.setAllowFlight(false);
        player.getActivePotionEffects().forEach(e -> player.removePotionEffect(e.getType()));
        player.getInventory().clear();
        player.setFoodLevel(20);
		player.updateInventory();

		// set player on arena data
		arena.getPlayersManager().add(player);
		// send message about arena player count
		String message = Messages.playerscountinarena;
		message = message.replace("{COUNT}", String.valueOf(arena.getPlayersManager().getCount()));
		Messages.sendMessage(player, message);
		// modify bars
		if (!arena.getStatusManager().isArenaStarting()) {
			for (Player oplayer : arena.getPlayersManager().getPlayers()) {
				Bars.setBar(oplayer, Bars.waiting, arena.getPlayersManager().getCount(), 0, arena.getPlayersManager().getCount() * 100 / arena.getStructureManager().getMinPlayers());
			}
		}
		// check for game start
		if (!arena.getStatusManager().isArenaStarting() && arena.getPlayersManager().getCount() == arena.getStructureManager().getMinPlayers()) {
			arena.getGameHandler().runArenaCountdown();
		}
	}

    // move to spectators
    public void spectatePlayer(Player player, String msgtoplayer, String msgtoarenaplayers) {
        // remove form players
        arena.getPlayersManager().remove(player);
        // teleport to spectators spawn
        player.teleport(arena.getStructureManager().getSpectatorSpawn());
        // clear inventory
        player.getInventory().clear();
        player.getInventory().setArmorContents(new ItemStack[4]);
        // allow flight
        player.setAllowFlight(true);
        player.setFlying(true);
        // hide from others
        for (Player oplayer : Bukkit.getOnlinePlayers()) {
            oplayer.hidePlayer(player);
        }
        // send message to player
        Messages.sendMessage(player, msgtoplayer);
        // send message to other players and update bars
        for (Player oplayer : arena.getPlayersManager().getAllParticipantsCopy()) {
            msgtoarenaplayers = msgtoarenaplayers.replace("{PLAYER}", player.getName());
            Messages.sendMessage(oplayer, msgtoarenaplayers);
        }
        //add to spectators
        arena.getPlayersManager().add(player);
    }

	// remove player from arena
	public void leave(Player player) {
		// reset spectators
        boolean spectator = arena.getPlayersManager().isSpectator(player.getName());

        if (spectator) {
            arena.getPlayersManager().removeSpecator(player.getName());
            for (Player oplayer : Bukkit.getOnlinePlayers()) {
                oplayer.showPlayer(player);
            }
            player.setAllowFlight(false);
            player.setFlying(false);
            return;
        }
        // remove player from arena and restore his state
        removePlayerFromArenaAndRestoreState(player, false);
        // should not send messages and other things when player is a spectator
        // send message to other players and update bars
		for (Player oplayer : arena.getPlayersManager().getAllParticipantsCopy()) {
			if (!arena.getStatusManager().isArenaStarting() && !arena.getStatusManager().isArenaRunning()) {
				Bars.setBar(oplayer, Bars.waiting, arena.getPlayersManager().getCount(), 0, arena.getPlayersManager().getCount() * 100 / arena.getStructureManager().getMinPlayers());
			}
		}
        player.teleport(Bukkit.getServer().getWorlds().get(0).getSpawnLocation());
	}

	protected void leaveWinner(Player player, String msgtoplayer) {
		// remove player from arena and restore his state
		removePlayerFromArenaAndRestoreState(player, true);
		// send message to player
		Messages.sendMessage(player, msgtoplayer);
	}

	@SuppressWarnings("deprecation")
	private void removePlayerFromArenaAndRestoreState(Player player, boolean winner) {
		votes.remove(player.getName());
		Bars.removeBar(player);
		arena.getPlayersManager().remove(player);
		if (winner) {
			arena.getStructureManager().getRewards().rewardPlayer(player);
		}
		player.updateInventory();
	}

	public boolean vote(Player player) {
		if (!votes.contains(player.getName())) {
			votes.add(player.getName());
			if (!arena.getStatusManager().isArenaStarting() && arena.getPlayersManager().getCount() > 1 && votes.size() >= arena.getPlayersManager().getCount() * arena.getStructureManager().getVotePercent()) {
				arena.getGameHandler().runArenaCountdown();
			}
			return true;
		}
		return false;
	}

}
