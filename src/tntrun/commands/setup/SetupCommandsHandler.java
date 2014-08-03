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

package tntrun.commands.setup;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import tntrun.TNTRun;
import tntrun.commands.setup.arena.*;
import tntrun.commands.setup.selection.SetP1;
import tntrun.commands.setup.selection.SetP2;
import tntrun.messages.Message;
import tntrun.messages.Messages;
import tntrun.selectionget.PlayerSelection;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SetupCommandsHandler implements CommandExecutor {

	private final PlayerSelection plselection = new PlayerSelection();
	private final Map<String, CommandHandlerInterface> commandHandlers = new HashMap<>();

	public SetupCommandsHandler(final TNTRun plugin) {

        PluginCommand pluginCommand = plugin.getCommand("tntrunsetup");
        pluginCommand.setExecutor(this);

		commandHandlers.put("setp1", new SetP1(plselection));
		commandHandlers.put("setp2", new SetP2(plselection));
		commandHandlers.put("create", new CreateArena(plugin));
		commandHandlers.put("delete", new DeleteArena(plugin));
		commandHandlers.put("setarena", new SetArena(plugin, plselection));
		commandHandlers.put("setgameleveldestroydelay", new SetGameLevelDestroyDelay(plugin));
		commandHandlers.put("setloselevel", new SetLoseLevel(plugin, plselection));
		commandHandlers.put("setspawn", new SetSpawn(plugin));
        commandHandlers.put("setspectate", new SetSpectatorSpawn(plugin));
        commandHandlers.put("delspectate", new DeleteSpectatorSpawn(plugin));
		commandHandlers.put("setmaxplayers", new SetMaxPlayers(plugin));
		commandHandlers.put("setminplayers", new SetMinPlayers(plugin));
		commandHandlers.put("setvotepercent", new SetVotePercent(plugin));
		commandHandlers.put("setcountdown", new SetCountdown(plugin));
		commandHandlers.put("setmoneyrewards", new SetMoneyRewards(plugin));
		commandHandlers.put("addkit", new AddKit(plugin));
		commandHandlers.put("deleteKit", new DeleteKit(plugin));
		commandHandlers.put("settimelimit", new SetTimeLimit(plugin));
        commandHandlers.put("setdamage", new SetDamage(plugin));
		commandHandlers.put("finish", new FinishArena(plugin));
		commandHandlers.put("disable", new DisableArena(plugin));
		commandHandlers.put("enable", new EnableArena(plugin));
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
			sender.sendMessage("Player is expected");
			return true;
		}

        Player player = (Player) sender;

        if (!player.hasPermission("tntrun.setup")) {
            Messages.send(player, Message.nopermission);
			return true;
		}

        // get command
		if (args.length > 0 && commandHandlers.containsKey(args[0])) {
			CommandHandlerInterface commandh = commandHandlers.get(args[0]);
            //check args length
            if (args.length - 1 < commandh.getMinArgsLength()) {
                Messages.sendMessage(player, ChatColor.RED + "Not enough args");
                return false;
            }
            //execute command
            return commandh.handleCommand(player, Arrays.copyOfRange(args, 1, args.length));
		}

        return false;
	}

}
