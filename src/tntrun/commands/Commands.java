package tntrun.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import tntrun.TNTRun;
import tntrun.messages.Message;
import tntrun.messages.Messages;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

abstract class Commands implements CommandExecutor, TabCompleter {

    protected final Map<String, CommandHandler> commands = new HashMap<>();

    public Commands(final TNTRun plugin, final String command) {
        PluginCommand pluginCommand = plugin.getCommand(command);
        pluginCommand.setExecutor(this);
        pluginCommand.setPermissionMessage(Messages.getMessage(Message.nopermission));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Player is expected");
            return true;
        }

        Player player = (Player) sender;

        if (commands.containsKey(args[0])) {
            CommandHandler cmd = commands.get(args[0]);

            if (args.length - 1 < cmd.getMinArgsLength()) {
                player.sendMessage(ChatColor.RED + "Not enough args");
                return false;
            }

            String message = cmd.handleCommand(player, Arrays.copyOfRange(args, 1, args.length));
            player.sendMessage(message);
            return true;
        }
        return false;
    }
}
