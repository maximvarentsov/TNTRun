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
import tntrun.commands.setup.*;
import tntrun.selectionget.PlayerSelection;

import java.util.List;

public class SetupCommands extends Commands {

	public SetupCommands(final TNTRun plugin) {
        super(plugin, "tntrunsetup");

        PlayerSelection selection = new PlayerSelection();

        commands.put("setp1", new SetP1(selection));
		commands.put("setp2", new SetP2(selection));
		commands.put("create", new CreateArena(plugin));
		commands.put("delete", new DeleteArena(plugin));
		commands.put("setarena", new SetArena(plugin, selection));
		commands.put("setgameleveldestroydelay", new SetGameLevelDestroyDelay(plugin));
		commands.put("setloselevel", new SetLoseLevel(plugin, selection));
		commands.put("setspawn", new SetSpawn(plugin));
        commands.put("setspectate", new SetSpectatorSpawn(plugin));
        commands.put("delspectate", new DeleteSpectatorSpawn(plugin));
		commands.put("setmaxplayers", new SetMaxPlayers(plugin));
		commands.put("setminplayers", new SetMinPlayers(plugin));
		commands.put("setvotepercent", new SetVotePercent(plugin));
		commands.put("setcountdown", new SetCountdown(plugin));
		commands.put("setmoneyrewards", new SetMoneyRewards(plugin));
		commands.put("addkit", new AddKit(plugin));
		commands.put("deleteKit", new DeleteKit(plugin));
		commands.put("settimelimit", new SetTimeLimit(plugin));
        commands.put("setdamage", new SetDamage(plugin));
		commands.put("finish", new FinishArena(plugin));
		commands.put("disable", new DisableArena(plugin));
		commands.put("enable", new EnableArena(plugin));
	}

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] strings) {
        return ImmutableList.of();
    }
}
