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
import org.bukkit.Location;
import org.bukkit.entity.Player;

import tntrun.TNTRun;
import tntrun.arena.Arena;
import tntrun.arena.structure.GameLevel;
import tntrun.bars.Bars;
import tntrun.messages.Messages;

public class GameHandler {

	private TNTRun plugin;
	private Arena arena;

	public GameHandler(TNTRun plugin, Arena arena) {
		this.plugin = plugin;
		this.arena = arena;
		count = arena.getStructureManager().getCountdown();
	}

	// arena leave handler
	private int leavetaskid;

	public void startArenaAntiLeaveHandler() {
		leavetaskid = Bukkit.getScheduler().scheduleSyncRepeatingTask(
			plugin,
			new Runnable() {
				@Override
				public void run() {
					for (Player player : arena.getPlayersManager().getPlayersCopy()) {
						if (!arena.getStructureManager().isInArenaBounds(player.getLocation())) {
							arena.getPlayerHandler().leavePlayer(player, Messages.playerlefttoplayer, Messages.playerlefttoothers);
						}
					}
				}
			},
			0, 1
		);
	}

	public void stopArenaAntiLeaveHandler() {
		Bukkit.getScheduler().cancelTask(leavetaskid);
	}

	// arena start handler (running status updater)
	int runtaskid;
	int count;

	public void runArenaCountdown() {
		arena.getStatusManager().setStarting(true);
		runtaskid = Bukkit.getScheduler().scheduleSyncRepeatingTask(
			plugin,
			new Runnable() {
				@Override
				public void run() {
					// check if countdown should be stopped for some various reasons
					if (arena.getPlayersManager().getCount() < arena.getStructureManager().getMinPlayers()) {
						for (Player player : arena.getPlayersManager().getPlayers()) {
							Bars.setBar(player, Bars.waiting, arena.getPlayersManager().getCount(), 0, arena.getPlayersManager().getCount() * 100 / arena.getStructureManager().getMinPlayers());
						}
						stopArenaCountdown();
					} else
					// start arena if countdown is 0
					if (count == 0) {
						stopArenaCountdown();
						startArena();
					} else
					// countdown
					{
						String message = Messages.arenacountdown;
						message = message.replace("{COUNTDOWN}", String.valueOf(count));
						for (Player player : arena.getPlayersManager().getPlayers()) {
							Messages.sendMessage(player, message);
							Bars.setBar(player, Bars.starting, 0, count, count * 100 / arena.getStructureManager().getCountdown());
						}
						count--;
					}
				}
			},
			0, 20
		);
	}

	public void stopArenaCountdown() {
		arena.getStatusManager().setStarting(false);
		count = arena.getStructureManager().getCountdown();
		Bukkit.getScheduler().cancelTask(runtaskid);
	}

	// main arena handler
	private int timelimit;
	private int arenahandler;

	public void startArena() {
		arena.getStatusManager().setRunning(true);
		String message = Messages.arenastarted;
		message = message.replace("{TIMELIMIT}", String.valueOf(arena.getStructureManager().getTimeLimit()));
		for (Player player : arena.getPlayersManager().getPlayers()) {
			Messages.sendMessage(player, message);
		}
		plugin.signEditor.modifySigns(arena.getArenaName());
		timelimit = arena.getStructureManager().getTimeLimit() * 20; // timelimit is in ticks
		arenahandler = Bukkit.getScheduler().scheduleSyncRepeatingTask(
			plugin,
			new Runnable() {
				@Override
				public void run() {
					if (timelimit < 0) {
						for (Player player : arena.getPlayersManager().getPlayersCopy()) {
							// kick player
							arena.getPlayerHandler().leavePlayer(player,Messages.arenatimeout, "");
						}
						// stop arena
						stopArena();
						return;
					}
					// stop arena if player count is 0 (just in case)
					if (arena.getPlayersManager().getCount() == 0) {
						// stop arena
						stopArena();
						return;
					}
					// handle players
					for (Player player : arena.getPlayersManager().getPlayersCopy()) {
						// update bar
						Bars.setBar(player, Bars.playing, arena.getPlayersManager().getCount(), timelimit / 20, timelimit * 5 / arena.getStructureManager().getTimeLimit());
						// handle player
						handlePlayer(player);
					}
					// decrease timelimit
					timelimit--;
				}
			},
			0, 1
		);
	}

	public void stopArena() {
		arena.getStatusManager().setRunning(false);
		Bukkit.getScheduler().cancelTask(arenahandler);
		plugin.signEditor.modifySigns(arena.getArenaName());
		if (arena.getStatusManager().isArenaEnabled()) {
			startArenaRegen();
		}
	}

	// player handlers
	public void handlePlayer(final Player player) {
		Location plloc = player.getLocation();
		Location plufloc = plloc.clone().add(0, -1, 0);
		// check for game location
		for (final GameLevel gl : arena.getStructureManager().getGameLevels()) {
			// remove block under player feet
			if (gl.isSandLocation(plufloc)) {
				gl.destroyBlock(plufloc, arena);
			}
		}
		// check for win
		if (arena.getPlayersManager().getCount() == 1) {
			// last player won
			arena.getPlayerHandler().leaveWinner(player, Messages.playerwontoplayer);
			broadcastWin(player);
			stopArena();
			return;
		}
		// check for lose
		if (arena.getStructureManager().getLoseLevel().isLooseLocation(plloc)) {
			// player lost
			arena.getPlayerHandler().leavePlayer(player, Messages.playerlosttoplayer, Messages.playerlosttoothers);
			return;
		}
	}

	private void broadcastWin(Player player) {
		String message = Messages.playerwonbroadcast;
		message = message.replace("{PLAYER}", player.getName());
		message = message.replace("{ARENA}", arena.getArenaName());
		Messages.broadcastMessage(message);
	}

	private void startArenaRegen() {
		// set arena is regenerating status
		arena.getStatusManager().setRegenerating(true);
		// modify signs
		plugin.signEditor.modifySigns(arena.getArenaName());
		// schedule gamelevels regen
		int delay = 1;
		for (final GameLevel gl : arena.getStructureManager().getGameLevels()) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(
				arena.plugin,
				new Runnable() {
					@Override
					public void run() {
						if (arena.getStatusManager().isArenaEnabled()) {
							gl.regen();
						}
					}
				},
				delay
			);
			delay++;
		}
		// schedule arena regen finished
		Bukkit.getScheduler().scheduleSyncDelayedTask(
			arena.plugin,
			new Runnable() {
				@Override
				public void run() {
					// set not regenerating status
					arena.getStatusManager().setRegenerating(false);
					// modify signs
					plugin.signEditor.modifySigns(arena.getArenaName());
				}
			},
			delay
		);
	}

}
