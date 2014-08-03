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

package tntrun.selectionget;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class OwnLocations {

	private final Map<String, Location> loc1 = new HashMap<>();
    private final Map<String, Location> loc2 = new HashMap<>();

	protected void putPlayerLoc1(String playername, Location loc) {
		loc = loc.getBlock().getLocation();
		loc1.put(playername, loc);
	}

	protected void putPlayerLoc2(String playername, Location loc) {
		loc = loc.getBlock().getLocation();
		loc2.put(playername, loc);
	}

	protected void clearPoints(String playername) {
		loc1.remove(playername);
		loc2.remove(playername);
	}

	protected Location[] getLocations(final Player player) {
        return sortLoc(player);
	}

	// 0 is min, 1 is max
	private Location[] sortLoc(Player player) {
		Double xmin = loc1.get(player.getName()).getX();
		Double xmax = loc2.get(player.getName()).getX();
		if (xmin > xmax) {
			Double temp = xmax;
			xmax = xmin;
			xmin = temp;
		}
		Double ymin = loc1.get(player.getName()).getY();
		Double ymax = loc2.get(player.getName()).getY();
		if (ymin > ymax) {
			Double temp = ymax;
			ymax = ymin;
			ymin = temp;
		}
		Double zmin = loc1.get(player.getName()).getZ();
		Double zmax = loc2.get(player.getName()).getZ();
		if (zmin > zmax) {
			Double temp = zmax;
			zmax = zmin;
			zmin = temp;
		}

		Location[] locs = new Location[2];
		locs[0] = new Location(loc1.get(player.getName()).getWorld(), xmin, ymin, zmin);
		locs[1] = new Location(loc1.get(player.getName()).getWorld(), xmax, ymax, zmax);
		locs[0].distanceSquared(locs[1]);
		return locs;
	}
}
