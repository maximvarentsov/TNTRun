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
import tntrun.TNTRun;
import tntrun.arena.Arena;
import tntrun.bars.Bars;
import tntrun.messages.Messages;
import tntrun.signs.editor.SignEditor;

import java.util.HashSet;
import java.util.Set;

public class PlayerHandler {

	private final SignEditor signEditor;
	private final Arena arena;
    private final Set<String> votes = new HashSet<>();

	public PlayerHandler(final TNTRun plugin, final Arena arena) {
		this.signEditor = plugin.signEditor;
		this.arena = arena;
	}

	// check if player can join the arena
	public boolean checkJoin(final Player player) {

        if (arena.getStructureManager().getWorld() == null) {
			player.sendMessage("Arena world is unloaded, can't join arena");
			return false;
		}

        if (!arena.getStatusManager().isArenaEnabled()) {
			Messages.sendMessage(player, Messages.arenadisabled);
			return false;
		}

        if (arena.getStatusManager().isArenaRunning() || arena.getStatusManager().isArenaRegenerating()) {
			Messages.sendMessage(player, Messages.arenarunning);
			return false;
		}

		if (arena.getPlayersManager().getCount() == arena.getStructureManager().getMaxPlayers()) {
			Messages.sendMessage(player, Messages.limitreached);
			return false;
		}

        return true;
	}

	@SuppressWarnings("deprecation")
	public void spawnPlayer(final Player player, String msgtoplayer, String msgtoarenaplayers) {

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

        Messages.sendMessage(player, msgtoplayer);
		for (Player oplayer : arena.getPlayersManager().getPlayers()) {
			msgtoarenaplayers = msgtoarenaplayers.replace("{PLAYER}", player.getName());
			Messages.sendMessage(oplayer, msgtoarenaplayers);
		}
		// set player on arena data
		arena.getPlayersManager().add(player);
		// send message about arena player count
		String message = Messages.playerscountinarena;
		message = message.replace("{COUNT}", String.valueOf(arena.getPlayersManager().getCount()));
		Messages.sendMessage(player, message);
		// modify signs
		signEditor.modifySigns(arena.getArenaName());
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

	// remove player from arena
	public void leavePlayer(Player player, String msgtoplayer, String msgtoarenaplayers) {
		// remove player from arena and restore his state
		removePlayerFromArenaAndRestoreState(player, false);
		// send message to player
		Messages.sendMessage(player, msgtoplayer);
		// modify signs
		signEditor.modifySigns(arena.getArenaName());
		// send message to other players and update bars
		for (Player oplayer : arena.getPlayersManager().getPlayers()) {
			msgtoarenaplayers = msgtoarenaplayers.replace("{PLAYER}", player.getName());
			Messages.sendMessage(oplayer, msgtoarenaplayers);
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
		// modify signs
		signEditor.modifySigns(arena.getArenaName());
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
