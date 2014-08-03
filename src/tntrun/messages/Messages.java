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

import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;

public class Messages {

    private static Configuration configuration;

    public Messages(final Configuration configuration) {
        Messages.configuration = configuration;
    }

    public static String getMessage(final Message message, Object...args) {
        String value = configuration.getString("messages." + message.name());
        return String.format(ChatColor.translateAlternateColorCodes('&', value), args);
    }
}
