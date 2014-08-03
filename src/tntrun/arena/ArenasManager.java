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

import org.bukkit.entity.Player;
import tntrun.TNTRun;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ArenasManager implements Iterable<Arena> {

	private final Map<String, Arena> arenas = new HashMap<>();

    public ArenasManager(final TNTRun plugin) {
        File arenas = new File(plugin.getDataFolder(), "arenas");
        if (arenas.exists()) {
            for (String file : arenas.list()) {
                Arena arena = new Arena(file, plugin);
                arena.getStructureManager().loadFromConfig();
                arena.getStatusManager().enableArena();
                this.add(arena);
            }
        } else {
            arenas.mkdirs();
        }
    }

    public void disable() {
        for (Arena arena : this) {
            arena.disable();
        }
    }

	public void add(final Arena arena) {
		arenas.put(arena.getArenaName(), arena);
	}

	public void remove(final Arena arena) {
		arenas.remove(arena.getArenaName());
	}

	public Arena get(final String name) {
		return arenas.get(name);
	}

	public Arena get(final Player player) {
		for (Arena arena : this) {
			if (arena.getPlayersManager().isInArena(player.getName())) {
				return arena;
			}
		}
		return null;
	}

    public boolean contains(final Player player) {
        for (Arena arena : this) {
            if (arena.getPlayersManager().isInArena(player.getName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<Arena> iterator() {
        return arenas.values().iterator();
    }
}
