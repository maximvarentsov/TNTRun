package tntrun.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import tntrun.TNTRun;
import tntrun.arena.Arena;

public class GameCommands implements CommandExecutor{

	private TNTRun plugin;
	public GameCommands(TNTRun plugin)
	{
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label,
			String[] args) {
		if (!(sender instanceof Player)) {return true;}
		Player player = (Player) sender;
		//list arenas
		if (args.length == 1 && args[0].equalsIgnoreCase("list"))
		{
			StringBuilder message = new StringBuilder(200);
			message.append("Available arenas: ");
			for (Arena arena : plugin.pdata.getArenas())
			{
				message.append(arena.getArenaName()+" ");
			}
			player.sendMessage(message.toString());
			return true;
		}
		//join arena
		else if (args.length == 2 && args[0].equalsIgnoreCase("join"))
		{
			Arena arena = getArenaByName(args[1]);
			if (arena != null)
			{
				if (!arena.isArenaEnabled()) {sender.sendMessage("Arena is disabled"); return true;}
				if (arena.running) {sender.sendMessage("Arena already running"); return true;}
				arena.spawnPlayer(player);
				player.sendMessage("You have joined the arena");
				return true;
			} else
			{
				sender.sendMessage("Arena not exists");
				return true;
			}
		}
		//leave arena
		else if (args.length == 1 && args[0].equalsIgnoreCase("leave"))
		{
				Arena arena = plugin.pdata.getPlayerArena(player.getName());
				if (arena != null)
				{
					arena.leavePlayer(player);
					player.sendMessage("You left the arena");
				} else
				{
					sender.sendMessage("You are not in arena");
					return true;
				}
		}
		//vote
		else if (args.length == 1 && args[0].equalsIgnoreCase("vote"))
		{
			Arena arena = plugin.pdata.getPlayerArena(player.getName());
			if (arena != null)
			{
				arena.vote(player);
				player.sendMessage("You voted for game start");
			} else
			{
				sender.sendMessage("You are not in arena");
				return true;
			}
		}
		return false;
	}
	
	
	private Arena getArenaByName(String name)
	{
		for (Arena arena : plugin.pdata.getArenas())
		{
			if (arena.getArenaName().equals(name))
			{
				return arena;
			}
		}
		return null;
	}

}
