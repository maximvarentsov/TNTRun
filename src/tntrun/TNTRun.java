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
import tntrun.arena.ArenasManager;
import tntrun.bars.Bars;
import tntrun.commands.GameCommands;
import tntrun.commands.setup.SetupCommandsHandler;
import tntrun.messages.Messages;

public final class TNTRun extends JavaPlugin {

	public ArenasManager arenas;

	@Override
	public void onEnable() {

        saveDefaultConfig();

        new Messages(getConfig());

		Bars.loadBars(this);
		arenas = new ArenasManager(this);

        new Listeners(this);
        new SetupCommandsHandler(this);
		new GameCommands(this);
	}

	@Override
	public void onDisable() {
        arenas.disable();
	}
}
