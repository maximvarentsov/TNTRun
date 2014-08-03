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

import org.bukkit.plugin.java.JavaPlugin;
import tntrun.arena.Arena;
import tntrun.arena.ArenasManager;
import tntrun.bars.Bars;
import tntrun.commands.GameCommands;
import tntrun.commands.setup.SetupCommandsHandler;
import tntrun.messages.Messages;

import java.io.File;

public final class TNTRun extends JavaPlugin {

	public ArenasManager arenas;

	@Override
	public void onEnable() {

        saveDefaultConfig();

		Bars.loadBars(this);
		arenas = new ArenasManager();

        new Messages(getConfig());
        new Listeners(this);

        new SetupCommandsHandler(this);
		new GameCommands(this);

		File arenas = new File(getDataFolder(), "arenas");

        if (arenas.exists()) {
            for (String file : arenas.list()) {
                Arena arena = new Arena(file, this);
                arena.getStructureManager().loadFromConfig();
                arena.getStatusManager().enableArena();
                this.arenas.add(arena);
            }
        } else {
            arenas.mkdirs();
        }
	}

	@Override
	public void onDisable() {
		for (Arena arena : arenas) {
			arena.getStatusManager().disableArena();
			arena.getStructureManager().saveToConfig();
		}
	}
}
