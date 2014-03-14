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

import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import tntrun.TNTRun;
import tntrun.arena.Arena;
import tntrun.bars.Bars;
import tntrun.messages.Messages;

public class PlayerHandler {

	private TNTRun plugin;
	private Arena arena;

	public PlayerHandler(TNTRun plugin, Arena arena) {
		this.plugin = plugin;
		this.arena = arena;
	}

	// check if player can join the arena
	public boolean checkJoin(Player player) {
		if (arena.getStructureManager().getWorld() == null) {
			player.sendMessage("Arena world is unloaded, can't join arena");
			return false;
		}
		if (!arena.getStatusManager().isArenaEnabled()) {
			Messages.sendMessage(player, Messages.arenadisabled);
			return false;
		}
		if (arena.getStatusManager().isArenaRunning()) {
			Messages.sendMessage(player, Messages.arenarunning);
			return false;
		}
		if (arena.getStatusManager().isArenaRegenerating()) {
			Messages.sendMessage(player, Messages.arenarunning);
			return false;
		}
		if (player.isInsideVehicle()) {
			player.sendMessage("You can't join the game while sitting inside vehicle");
			return false;
		}
		if (arena.getPlayersManager().getPlayersCount() == arena.getStructureManager().getMaxPlayers()) {
			Messages.sendMessage(player, Messages.limitreached);
			return false;
		}
		return true;
	}

	// spawn player on arena
	@SuppressWarnings("deprecation")
	public void spawnPlayer(final Player player, String msgtoplayer, String msgtoarenaplayers) {
		// change player status
		plugin.pdata.storePlayerGameMode(player.getName());
		player.setFlying(false);
		player.setAllowFlight(false);
		plugin.pdata.storePlayerInventory(player.getName());
		plugin.pdata.storePlayerArmor(player.getName());
		plugin.pdata.storePlayerPotionEffects(player.getName());
		plugin.pdata.storePlayerHunger(player.getName());
		// teleport player to arena
		plugin.pdata.storePlayerLocation(player.getName());
		player.teleport(arena.getStructureManager().getSpawnPoint());
		// update inventory
		player.updateInventory();
		// send message to player
		Messages.sendMessage(player, msgtoplayer);
		// send message to other players and update bar
		for (String pname : arena.getPlayersManager().getPlayersInArena()) {
			Messages.sendMessage(Bukkit.getPlayerExact(pname), player.getName(), msgtoarenaplayers);
		}
		// set player on arena data
		arena.getPlayersManager().addPlayerToArena(player.getName());
		// send message about arena player count
		Messages.sendMessage(player, Messages.playerscount + arena.getPlayersManager().getPlayersCount());
		// modify signs
		plugin.signEditor.modifySigns(arena.getArenaName());
		// modify bars
		if (!arena.getStatusManager().isArenaStarting()) {
			for (Player oplayer : Bukkit.getOnlinePlayers()) {
				if (arena.getPlayersManager().isPlayerInArena(oplayer.getName())) {
					Bars.setBar(oplayer, Bars.waiting, arena.getPlayersManager().getPlayersCount(), 0, arena.getPlayersManager().getPlayersCount() * 100 / arena.getStructureManager().getMinPlayers());
				}
			}
		}
		// check for game start
		if (!arena.getStatusManager().isArenaStarting() && (arena.getPlayersManager().getPlayersCount() == arena.getStructureManager().getMaxPlayers() || arena.getPlayersManager().getPlayersCount() == arena.getStructureManager().getMinPlayers())) {
			arena.getGameHandler().runArenaCountdown();
		}
	}

	// remove player from arena
	public void leavePlayer(Player player, String msgtoplayer, String msgtoarenaplayers) {
		// remove player from arena and restore his state
		removePlayerFromArenaAndRestoreState(player, false);
		// send message to player
		Messages.sendMessage(player, msgtoplayer);
		// modify signs
		plugin.signEditor.modifySigns(arena.getArenaName());
		// send message to other players and update bars
		for (Player oplayer : Bukkit.getOnlinePlayers()) {
			if (arena.getPlayersManager().isPlayerInArena(oplayer.getName())) {
				Messages.sendMessage(oplayer, player.getName(), msgtoarenaplayers);
				if (!arena.getStatusManager().isArenaStarting() && !arena.getStatusManager().isArenaRunning()) {
					Bars.setBar(oplayer, Bars.waiting, arena.getPlayersManager().getPlayersCount(), 0, arena.getPlayersManager().getPlayersCount() * 100 / arena.getStructureManager().getMinPlayers());
				}
			}
		}
	}

	protected void leaveWinner(Player player, String msgtoplayer) {
		// remove player from arena and restore his state
		removePlayerFromArenaAndRestoreState(player, true);
		// send message to player
		Messages.sendMessage(player, msgtoplayer);
		// modify signs
		plugin.signEditor.modifySigns(arena.getArenaName());
	}

	@SuppressWarnings("deprecation")
	private void removePlayerFromArenaAndRestoreState(Player player, boolean winner) {
		// remove vote
		votes.remove(player.getName());
		// remove bar
		Bars.removeBar(player);
		// remove player on arena data
		arena.getPlayersManager().removePlayerFromArena(player.getName());
		// restore location
		plugin.pdata.restorePlayerLocation(player.getName());
		// restore player status
		plugin.pdata.restorePlayerHunger(player.getName());
		plugin.pdata.restorePlayerPotionEffects(player.getName());
		plugin.pdata.restorePlayerArmor(player.getName());
		plugin.pdata.restorePlayerInventory(player.getName());
		// reward player before restoring gamemode if player is winner
		if (winner) {
			arena.getStructureManager().getRewards().rewardPlayer(player);
		}
		plugin.pdata.restorePlayerGameMode(player.getName());
		// update inventory
		player.updateInventory();
	}

	// vote for game start
	private HashSet<String> votes = new HashSet<String>();

	public boolean vote(Player player) {
		if (!votes.contains(player.getName())) {
			votes.add(player.getName());
			if (!arena.getStatusManager().isArenaStarting() && (arena.getPlayersManager().getPlayersCount() > 1 && (votes.size() >= arena.getPlayersManager().getPlayersCount() * arena.getStructureManager().getVotePercent()))) {
				arena.getGameHandler().runArenaCountdown();
			}
			return true;
		}
		return false;
	}

}
