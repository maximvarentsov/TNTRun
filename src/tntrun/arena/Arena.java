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

package tntrun.arena;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import tntrun.TNTRun;
import tntrun.arena.handlers.GameHandler;
import tntrun.arena.handlers.PlayerHandler;
import tntrun.arena.structure.GameLevel;
import tntrun.arena.structure.StructureManager;
import tntrun.messages.Messages;

public class Arena {

	public TNTRun plugin;
	public GameHandler arenagh;
	public PlayerHandler arenaph;

	public Arena(String name, TNTRun plugin) {
		this.arenaname = name;
		this.plugin = plugin;
		arenagh = new GameHandler(plugin, this);
		arenaph = new PlayerHandler(plugin, this);
		structureManager.setArenaFile(new File(plugin.getDataFolder() + File.separator + "arenas" + File.separator + arenaname + ".yml"));
	}
	
	private StructureManager structureManager = new StructureManager();
	public StructureManager getStructureManager() {
		return structureManager;
	}
	
	private PlayersManager playersManager = new PlayersManager();
	public PlayersManager getPlayersManager() {
		return playersManager;
	}
	public class PlayersManager {
		private HashSet<String> players = new HashSet<String>();
		public boolean isPlayerInArena(String name) {
			return players.contains(name);
		}
		public int getPlayersCount() {
			return players.size();
		}
		public Set<String> getPlayersInArena() {
			return Collections.unmodifiableSet(players);
		}
		public void addPlayerToArena(String name) {
			players.add(name);
		}
		public void removePlayerFromArena(String name) {
			players.remove(name);
		}
	}

	private boolean enabled = false;
	private boolean starting = false;
	private boolean running = false;
	private boolean regenerating = false;

	private String arenaname;

	public String getArenaName() {
		return arenaname;
	}

	// arena status handler
	public boolean isArenaEnabled() {
		return enabled;
	}

	public boolean enableArena() {
		if (getStructureManager().isArenaConfigured().equalsIgnoreCase("yes")) {
			enabled = true;
			arenagh.startArenaAntiLeaveHandler();
			plugin.signEditor.modifySigns(getArenaName());
			return true;
		}
		return false;
	}

	public void disableArena() {
		enabled = false;
		// drop players
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (getPlayersManager().isPlayerInArena(player.getName())) {
				arenaph.leavePlayer(player, Messages.arenadisabling, "");
			}
		}
		// stop arena
		arenagh.stopArena();
		// stop countdown
		arenagh.stopArenaCountdown();
		// stop antileave handler
		arenagh.stopArenaAntiLeaveHandler();
		// regen gamelevels
		for (GameLevel gl : getStructureManager().getGameLevels()) {
			gl.regen();
		}
		// modify signs
		plugin.signEditor.modifySigns(getArenaName());
	}

	public boolean isArenaStarting() {
		return starting;
	}

	public void setStarting(boolean starting) {
		this.starting = starting;
	}

	public boolean isArenaRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public boolean isArenaRegenerating() {
		return regenerating;
	}

	public void setRegenerating(boolean regenerating) {
		this.regenerating = regenerating;
	}

}
