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

import tntrun.TNTRun;
import tntrun.arena.handlers.GameHandler;
import tntrun.arena.handlers.PlayerHandler;
import tntrun.arena.structure.StructureManager;

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
	
	private String arenaname;
	public String getArenaName() {
		return arenaname;
	}
	
	private StatusManager statusManager = new StatusManager(this);
	public StatusManager getStatusManager() {
		return statusManager;
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

}
