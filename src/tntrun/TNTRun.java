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
import tntrun.bars.Bars;
import tntrun.commands.GameCommands;
import tntrun.commands.setup.SetupCommandsHandler;
import tntrun.datahandler.ArenasManager;
import tntrun.eventhandler.PlayerLeaveArenaChecker;
import tntrun.eventhandler.PlayerStatusHandler;
import tntrun.eventhandler.RestrictionHandler;
import tntrun.messages.Messages;
import tntrun.signs.SignHandler;
import tntrun.signs.editor.SignEditor;

import java.io.File;

public final class TNTRun extends JavaPlugin {

	public ArenasManager arenas;
	public SignEditor signEditor;

	@Override
	public void onEnable() {
		signEditor = new SignEditor(this);
		Messages.loadMessages(this);
		Bars.loadBars(this);
		arenas = new ArenasManager();

        new SetupCommandsHandler(this);
		new GameCommands(this);

        new PlayerStatusHandler(this);
		new RestrictionHandler(this);
		new PlayerLeaveArenaChecker(this);
		new SignHandler(this);

		File arenas = new File(getDataFolder(), "arenas");
		if (!arenas.exists()) {
            arenas.mkdirs();
        }

        for (String file : arenas.list()) {
            Arena arena = new Arena(file.substring(0, file.length() - 4), this);
            arena.getStructureManager().loadFromConfig();
            arena.getStatusManager().enableArena();
            this.arenas.registerArena(arena);
        }
        signEditor.loadConfiguration();
	}

	@Override
	public void onDisable() {
		for (Arena arena : arenas.getArenas()) {
			arena.getStatusManager().disableArena();
			arena.getStructureManager().saveToConfig();
		}
        signEditor.saveConfiguration();
	}
}
