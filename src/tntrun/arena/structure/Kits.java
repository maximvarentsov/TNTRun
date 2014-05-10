package tntrun.arena.structure;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class Kits {

	private HashMap<String, Kit> kits = new HashMap<String, Kit>();

	public boolean isKitExist(String name) {
		return kits.containsKey(name);
	}

	public void registerKit(Player player, String name) {
		kits.put(name, new Kit(player));
	}

	public void unregisterKit(String name) {
		kits.remove(name);
	}

	public void giveKit(Player player, String name) {
		try {
			kits.get(name).giveKit(player);
		} catch (Exception e) {
		}
	}

	private static class Kit {

		private ItemStack[] armor;
		private ItemStack[] items;
		private Collection<PotionEffect> effects;

		public Kit(Player player) {
			armor = player.getInventory().getArmorContents();
			items = player.getInventory().getContents();
			effects = player.getActivePotionEffects();
		}

		public void giveKit(Player player) {
			player.getInventory().setArmorContents(armor);
			player.getInventory().setContents(items);
			player.addPotionEffects(effects);
		}

		public void loadFromConfig(FileConfiguration config, String path) {
			armor = (ItemStack[]) config.get(path+".armor");
			items = (ItemStack[]) config.get(path+".items");
			effects = Arrays.asList((PotionEffect[]) config.get(path+".effects"));
		}

		public void saveToConfig(FileConfiguration config, String path) {
			config.set(path+".armor", armor);
			config.set(path+".items", items);
			config.set(path+".effects", effects.toArray(new PotionEffect[effects.size()]));
		}

	}

	public void loadFromConfig(FileConfiguration config) {
		
	}

}
