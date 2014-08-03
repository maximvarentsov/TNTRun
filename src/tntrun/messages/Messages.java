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

package tntrun.messages;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;

public class Messages {

    private static Configuration configuration;

    public Messages(final Configuration configuration) {
        this.configuration = configuration;
    }

	public static void broadcast(final Message message, final Object...args) {
		Bukkit.broadcastMessage(getMessage(message));
	}

    public static String getMessage(final Message message) {
        String value = configuration.getString("messages." + message.name());
        return ChatColor.translateAlternateColorCodes('&', value);
    }

    public static void send(final CommandSender sender, final Message message, final Object...args) {
        sender.sendMessage(String.format(getMessage(message), args));
    }
}
