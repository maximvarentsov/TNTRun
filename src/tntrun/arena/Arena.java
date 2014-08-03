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

import tntrun.TNTRun;
import tntrun.arena.handlers.GameHandler;
import tntrun.arena.handlers.PlayerHandler;
import tntrun.arena.status.PlayersManager;
import tntrun.arena.status.StatusManager;
import tntrun.arena.structure.StructureManager;
import tntrun.messages.Message;
import tntrun.messages.Messages;

import java.io.File;

public class Arena {

    private String name;
    private File file;
    private GameHandler gameHandler;
    private PlayerHandler playerHandler;
    private StatusManager statusManager = new StatusManager(this);
    private StructureManager structureManager = new StructureManager(this);
    private PlayersManager playersManager = new PlayersManager();

    public Arena(String name, TNTRun plugin) {
		this.name = name;
		gameHandler = new GameHandler(plugin, this);
		playerHandler = new PlayerHandler(plugin, this);
		file = new File(plugin.getDataFolder(), "arenas" + File.separator + name);
	}

    public void broadcast(Message message, Object...args) {
        getPlayerHandler().leavePlayer(player, "", Messages.playerlefttoothers);
    }

    public String getArenaName() {
		return name;
	}

	public File getArenaFile() {
		return file;
	}

	public GameHandler getGameHandler() {
		return gameHandler;
	}

	public PlayerHandler getPlayerHandler() {
		return playerHandler;
	}

	public StatusManager getStatusManager() {
		return statusManager;
	}

	public StructureManager getStructureManager() {
		return structureManager;
	}

	public PlayersManager getPlayersManager() {
		return playersManager;
	}

    public void disable() {
        getStatusManager().disableArena();
        getStructureManager().saveToConfig();
    }

}
