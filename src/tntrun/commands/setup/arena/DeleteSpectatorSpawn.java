package tntrun.commands.setup.arena;

import org.bukkit.entity.Player;
import tntrun.TNTRun;
import tntrun.arena.Arena;
import tntrun.arena.ArenasManager;
import tntrun.commands.setup.CommandHandlerInterface;
import tntrun.messages.Message;
import tntrun.messages.Messages;

public class DeleteSpectatorSpawn implements CommandHandlerInterface {

    private final ArenasManager arenas;

    public DeleteSpectatorSpawn(final TNTRun plugin) {
        arenas = plugin.arenas;
    }

    @Override
    public boolean handleCommand(final Player player, final String[] args) {

        Arena arena = arenas.get(args[0]);

        if (arena == null) {
            Messages.send(player, Message.arena_not_found, args[0]);
            return true;
        }

        if (arena.getStatusManager().isArenaEnabled()) {
            player.sendMessage("Disable arena first");
            return true;
        }

        arena.getStructureManager().removeSpectatorsSpawn();
        player.sendMessage("Spectator spawn deleted");

        return true;
    }

    @Override
    public int getMinArgsLength() {
        return 2;
    }
}
