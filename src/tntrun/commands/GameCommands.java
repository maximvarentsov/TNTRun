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

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;

import tntrun.TNTRun;
import tntrun.arena.Arena;
import tntrun.messages.Messages;

public class GameCommands implements CommandExecutor {

	private final TNTRun plugin;

	public GameCommands(final TNTRun plugin) {

        PluginCommand pluginCommand = plugin.getCommand("tntrunsetup");
        pluginCommand.setExecutor(this);

        this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("A player is expected");
			return true;
		}
		Player player = (Player) sender;
		// handle commands
		// help command
		if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
			sender.sendMessage("/tr list - list all arenas");
			sender.sendMessage("/tr join {arena} - join arena");
			sender.sendMessage("/tr leave - leave current arena");
			sender.sendMessage("/tr vote - vote for current arena start");
			return true;
		}
		// list arenas
		else if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
			StringBuilder message = new StringBuilder(200);
			message.append(Messages.availablearenas);
			for (Arena arena : plugin.arenas.getArenas()) {
				if (arena.getStatusManager().isArenaEnabled()) {
					message.append("&a" + arena.getArenaName() + " ");
				} else {
					message.append("&c" + arena.getArenaName() + " ");
				}
			}
			Messages.sendMessage(player, message.toString());
			return true;
		}
		// join arena
		else if (args.length == 2 && args[0].equalsIgnoreCase("join")) {
			if (player.hasPermission("tntrun.onlysignjoin")) {
				player.sendMessage("You can join the game only by using a sign");
				return true;
			}
			Arena arena = plugin.arenas.getArenaByName(args[1]);
			if (arena != null) {
				boolean canJoin = arena.getPlayerHandler().checkJoin(player);
				if (canJoin) {
					arena.getPlayerHandler().spawnPlayer(player, Messages.playerjoinedtoplayer, Messages.playerjoinedtoothers);
				}
				return true;
			} else {
				sender.sendMessage("Arena does not exist");
				return true;
			}
		}
		// leave arena
		else if (args.length == 1 && args[0].equalsIgnoreCase("leave")) {
			Arena arena = plugin.arenas.getPlayerArena(player.getName());
			if (arena != null) {
				arena.getPlayerHandler().leavePlayer(player, Messages.playerlefttoplayer, Messages.playerlefttoothers);
				return true;
			} else {
				sender.sendMessage("You are not in arena");
				return true;
			}
		}
		// vote
		else if (args.length == 1 && args[0].equalsIgnoreCase("vote")) {
			Arena arena = plugin.arenas.getPlayerArena(player.getName());
			if (arena != null) {
				if (arena.getPlayerHandler().vote(player)) {
					Messages.sendMessage(player, Messages.playervotedforstart);
				} else {
					Messages.sendMessage(player, Messages.playeralreadyvotedforstart);
				}
				return true;
			} else {
				sender.sendMessage("You are not in arena");
				return true;
			}
		}
		return false;
	}

}
