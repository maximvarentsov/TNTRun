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

package tntrun.arena.structure;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import tntrun.messages.Messages;

import java.util.ArrayList;
import java.util.List;

public class Rewards {

	private Object economy = null;
    private int moneyreward = 0;

    public Rewards() {
		if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
			RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
			if (economyProvider != null) {
				economy = economyProvider.getProvider();
			}
		}
	}

	public int getMoneyReward() {
		return moneyreward;
	}

	public void setRewards(int money) {
		this.moneyreward = money;
	}

	public void rewardPlayer(Player player) {
		String rewardmessage = "";

		if (moneyreward != 0) {
			rewardMoney(player.getName(), moneyreward);
			rewardmessage += ChatColor.GOLD.toString() + moneyreward;
		}
		if (rewardmessage.endsWith(", ")) {
			rewardmessage = rewardmessage.substring(0, rewardmessage.length() - 2);
		}
		rewardmessage = Messages.playerrewardmessage.replace("{REWARD}", rewardmessage);
		if (!rewardmessage.isEmpty()) {
			Messages.sendMessage(player, rewardmessage);
		}
	}

	private void rewardMoney(String playername, int money) {
		if (economy != null) {
			Economy econ = (Economy) economy;
			econ.depositPlayer(playername, money);
		}
	}

	public void saveToConfig(FileConfiguration config) {
		config.set("reward.money", moneyreward);
	}

	@SuppressWarnings("unchecked")
	public void loadFromConfig(FileConfiguration config) {
		moneyreward = config.getInt("reward.money", moneyreward);
	}

}
