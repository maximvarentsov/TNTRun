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

package tntrun.signs;

import com.google.common.collect.ImmutableMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import tntrun.TNTRun;
import tntrun.datahandler.ArenasManager;
import tntrun.messages.Messages;
import tntrun.signs.type.JoinSign;
import tntrun.signs.type.LeaveSign;
import tntrun.signs.type.SignType;
import tntrun.signs.type.VoteSign;

import java.util.Map;

@Deprecated
public class SignHandler implements Listener {

	private final Map<String, SignType> signs;

	public SignHandler(final TNTRun plugin) {
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
		signs = ImmutableMap.of(
            "[join]", new JoinSign(plugin),
            "[leave]", new LeaveSign(plugin),
            "[vote]", new VoteSign(plugin)
        );
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    @SuppressWarnings("unused")
	void onTNTRunSignCreate(final SignChangeEvent event) {
		Player player = event.getPlayer();
		if (event.getLine(0).equalsIgnoreCase("[TNTRun]")) {
			if (!player.hasPermission("tntrun.setup")) {
				Messages.sendMessage(player, Messages.nopermission);
				event.setCancelled(true);
				event.getBlock().breakNaturally();
				return;
			}
			String line = event.getLine(1).toLowerCase();
			if (signs.containsKey(line)) {
				signs.get(line).handleCreation(event);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
    @SuppressWarnings("unused")
    void onSignClick(final PlayerInteractEvent event) {
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
			return;
		}
		if (!(event.getClickedBlock().getState() instanceof Sign)) {
			return;
		}
		Sign sign = (Sign) event.getClickedBlock().getState();
		if (sign.getLine(0).equalsIgnoreCase(ChatColor.BLUE + "[TNTRun]")) {
			String line = sign.getLine(1).toLowerCase();
			if (signs.containsKey(line)) {
				signs.get(line).handleClick(event);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    @SuppressWarnings("unused")
    void onSignDestroy(final BlockBreakEvent event) {
		if (!(event.getBlock().getState() instanceof Sign)) {
			return;
		}
		Player player = event.getPlayer();
		Sign sign = (Sign) event.getBlock().getState();
		if (sign.getLine(0).equalsIgnoreCase(ChatColor.BLUE + "[TNTRun]")) {
			if (!player.hasPermission("tntrun.setup")) {
				Messages.sendMessage(player, Messages.nopermission);
				event.setCancelled(true);
				return;
			}
			String line = sign.getLine(1).toLowerCase();
			if (signs.containsKey(line)) {
				signs.get(line).handleDestroy(event);
			}
		}
	}

}
