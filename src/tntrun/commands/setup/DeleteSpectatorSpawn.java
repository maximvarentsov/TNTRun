package tntrun.commands.setup;

import org.bukkit.entity.Player;
import tntrun.TNTRun;
import tntrun.arena.Arena;
import tntrun.arena.ArenasManager;
import tntrun.commands.CommandHandlerInterface;
import tntrun.messages.Message;
import tntrun.messages.Messages;

public class DeleteSpectatorSpawn implements CommandHandlerInterface {

    private final ArenasManager arenas;

    public DeleteSpectatorSpawn(final TNTRun plugin) {
        arenas = plugin.arenas;
    }

    @Override
    public String handleCommand(final Player player, final String[] args) {

        Arena arena = arenas.get(args[0]);

        if (arena == null) {
            return Messages.getMessage(Message.arena_not_found, args[0]);
        }

        if (arena.getStatusManager().isArenaEnabled()) {
            return Messages.getMessage(Message.disable_arena_first, args[0]);
        }

        arena.getStructureManager().removeSpectatorsSpawn();

        return "Spectator spawn deleted";
    }

    @Override
    public int getMinArgsLength() {
        return 2;
    }
}
