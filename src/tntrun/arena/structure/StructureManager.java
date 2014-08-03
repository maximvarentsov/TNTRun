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

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import tntrun.arena.Arena;

import java.io.IOException;

public class StructureManager {

	private Arena arena;
    private String world;
    private Vector p1 = null;
    private Vector p2 = null;
    private GameZone gamezone = new GameZone();
    private int gameleveldestroydelay = 8;
    private LoseLevel loselevel = new LoseLevel();
    private Vector spawnpoint = null;
    private int maxPlayers = 6;
    private int minPlayers = 2;
    private double votesPercent = 0.75;
    private int timelimit = 180;
    private int countdown = 10;
    private Kits kits = new Kits();
    private Rewards rewards = new Rewards();
    private DamageEnabled damageEnabled = DamageEnabled.NO;
    private Vector spectatorspawn = null;

    public static enum DamageEnabled {
        YES, ZERO, NO
    }

    public StructureManager(final Arena arena) {
		this.arena = arena;
	}

	public String getWorldName() {
		return world;
	}

	public World getWorld() {
		return Bukkit.getWorld(world);
	}

	public Vector getP1() {
		return p1;
	}

	public Vector getP2() {
		return p2;
	}

	public GameZone getGameZone() {
		return gamezone;
	}

	public int getGameLevelDestroyDelay() {
		return gameleveldestroydelay;
	}

	public LoseLevel getLoseLevel() {
		return loselevel;
	}

    public Vector getSpectatorSpawnVector() {
        return spectatorspawn;
    }

    public Location getSpectatorSpawn() {
        if (spectatorspawn != null) {
            return new Location(getWorld(), spectatorspawn.getX(), spectatorspawn.getY(), spectatorspawn.getZ());
        }
        return null;
    }

	public Vector getSpawnPointVector() {
		return spawnpoint;
	}

	public Location getSpawnPoint() {
		return new Location(getWorld(), spawnpoint.getX(), spawnpoint.getY(), spawnpoint.getZ());
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}

	public int getMinPlayers() {
		return minPlayers;
	}

    public double getVotePercent() {
		return votesPercent;
	}

	public int getTimeLimit() {
		return timelimit;
	}

	public int getCountdown() {
		return countdown;
	}

	public Kits getKits() {
		return kits;
	}

	public Rewards getRewards() {
		return rewards;
	}

    public DamageEnabled getDamageEnabled() {
        return damageEnabled;
    }

    public void setDamageEnabled(final DamageEnabled damageEnabled) {
        this.damageEnabled = damageEnabled;
    }

	public boolean isInArenaBounds(final Location loc) {
        return loc.toVector().isInAABB(getP1(), getP2());
    }

	public boolean isArenaConfigured() {
		return isArenaConfiguredString().equals("yes");
	}

	public String isArenaConfiguredString() {
		if (getP1() == null || getP2() == null || world == null) {
			return "Arena bounds not set";
		}
		if (!loselevel.isConfigured()) {
			return "Arena looselevel not set";
		}
		if (spawnpoint == null) {
			return "Arena spawnpoint not set";
		}
		return "yes";
	}

	public void setArenaPoints(Location loc1, Location loc2) {
		this.world = loc1.getWorld().getName();
		this.p1 = loc1.toVector();
		this.p2 = loc2.toVector();
	}

	public void setGameLevelDestroyDelay(int delay) {
		gameleveldestroydelay = delay;
	}

	public boolean setLooseLevel(Location loc1, Location loc2) {
		if (isInArenaBounds(loc1) && isInArenaBounds(loc2)) {
			loselevel.setLooseLocation(loc1, loc2);
			return true;
		}
		return false;
	}

	public boolean setSpawnPoint(Location loc) {
		if (isInArenaBounds(loc)) {
			spawnpoint = loc.toVector();
			return true;
		}
		return false;
	}

    public boolean setSpectatorsSpawn(Location loc) {
        if (isInArenaBounds(loc)) {
            spectatorspawn = loc.toVector();
            return true;
        }
        return false;
    }

    public void removeSpectatorsSpawn() {
        spectatorspawn = null;
    }

	public void setMaxPlayers(int maxplayers) {
		this.maxPlayers = maxplayers;
	}

	public void setMinPlayers(int minplayers) {
		this.minPlayers = minplayers;
	}

	public void setVotePercent(double votepercent) {
		this.votesPercent = votepercent;
	}

	public void setTimeLimit(int timelimit) {
		this.timelimit = timelimit;
	}

	public void setCountdown(int countdown) {
		this.countdown = countdown;
	}

	public void addKit(String name, Player player) {
		kits.registerKit(name, player);
	}

	public void removeKit(String name) {
		kits.unregisterKit(name);
	}

	public void setRewards(int money) {
		this.rewards.setRewards(money);
	}

	public void saveToConfig() {
		FileConfiguration config = new YamlConfiguration();
		// save arena bounds
		try {
			config.set("world", world);
			config.set("p1", p1);
			config.set("p2", p2);
		} catch (Exception ignore) {
		}

        // save damage enabled
        config.set("damageenabled", damageEnabled.toString());

		// save gamelevel destroy delay
		config.set("gameleveldestroydelay", gameleveldestroydelay);
		// save looselevel
		try {
			loselevel.saveToConfig(config);
		} catch (Exception ignore) {
		}
		// save spawnpoint
		try {
			config.set("spawnpoint", spawnpoint);
		} catch (Exception ignore) {
		}
        // save spectators spawn
        try {
            config.set("spectatorspawn", spectatorspawn);
        } catch (Exception ignore) {
        }
		// save maxplayers
		config.set("maxPlayers", maxPlayers);
		// save minplayers
		config.set("minPlayers", minPlayers);
		// save vote percent
		config.set("votePercent", votesPercent);
		// save timelimit
		config.set("timelimit", timelimit);
		// save countdown
		config.set("countdown", countdown);
		// save kits
		kits.saveToConfig(config);
		// save rewards
		rewards.saveToConfig(config);
		try {
			config.save(arena.getArenaFile());
		} catch (IOException ignore) {
		}
	}

	public void loadFromConfig() {
		FileConfiguration config = YamlConfiguration.loadConfiguration(arena.getArenaFile());
		// load arena world location
		world = config.getString("world", null);
		// load arena bounds
		p1 = config.getVector("p1", null);
		p2 = config.getVector("p2", null);

        damageEnabled = DamageEnabled.valueOf(config.getString("damageenabled", DamageEnabled.NO.toString()));
		// load gamelevel destroy delay
		gameleveldestroydelay = config.getInt("gameleveldestroydelay", gameleveldestroydelay);
		// load looselevel
		loselevel.loadFromConfig(config);
		// load spawnpoint
		spawnpoint = config.getVector("spawnpoint", null);
        // load spectators spawn
        spectatorspawn = config.getVector("spectatorspawn", null);
		// load maxplayers
		maxPlayers = config.getInt("maxPlayers", maxPlayers);
		// load minplayers
		minPlayers = config.getInt("minPlayers", minPlayers);
		// load vote percent
		votesPercent = config.getDouble("votePercent", votesPercent);
		// load timelimit
		timelimit = config.getInt("timelimit", timelimit);
		// load countdown
		countdown = config.getInt("countdown", countdown);
		// load kits
		kits.loadFromConfig(config);
		// load rewards
		rewards.loadFromConfig(config);
	}
}
