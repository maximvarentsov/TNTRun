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

package tntrun.commands.game;

import org.bukkit.entity.Player;
import tntrun.TNTRun;
import tntrun.arena.Arena;
import tntrun.arena.ArenasManager;
import tntrun.commands.CommandHandler;
import tntrun.messages.Message;
import tntrun.messages.Messages;

public class List implements CommandHandler {

    private final ArenasManager arenas;

    public List(final TNTRun plugin) {
        arenas = plugin.arenas;
    }

    @Override
    public String handleCommand(final Player player, final String[] args) {

        StringBuilder message = new StringBuilder(200);
        message.append(Messages.getMessage(Message.availablearenas));

        for (Arena arena : arenas) {
            if (arena.getStatusManager().isArenaEnabled()) {
                message.append("&a").append(arena.getArenaName()).append(" ");
            } else {
                message.append("&c").append(arena.getArenaName()).append(" ");
            }
        }

        return message.toString();
    }

    @Override
    public int getMinArgsLength() {
        return 0;
    }
}
