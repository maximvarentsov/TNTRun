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

package tntrun.commands;

import com.google.common.collect.ImmutableList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import tntrun.TNTRun;
import tntrun.commands.game.Join;
import tntrun.commands.game.Leave;
import tntrun.commands.game.Vote;

import java.util.List;

public class GameCommands extends Commands {

	public GameCommands(final TNTRun plugin) {
        super(plugin, "tntrun");

        commands.put("list", new tntrun.commands.game.List(plugin));
        commands.put("join", new Join(plugin));
        commands.put("leave", new Leave(plugin));
        commands.put("vote", new Vote(plugin));
	}

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] strings) {
        return null;
    }
}
