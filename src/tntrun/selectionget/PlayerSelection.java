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

public class PlayerSelection {

	private final OwnLocations ownlocations;

	public PlayerSelection() {
        ownlocations = new OwnLocations();
	}

	public PlayerCuboidSelection getPlayerSelection(final Player player) {

		Location[] locs = ownlocations.getLocations(player);

        if (locs != null) {
			return new PlayerCuboidSelection(locs[0], locs[1]);
		}

        return null;
	}

	public void setSelectionPoint1(final Player player) {
		ownlocations.putPlayerLoc1(player.getName(), player.getTargetBlock(null, 30).getLocation());
	}

    public void setSelectionPoint2(final Player player) {
        ownlocations.putPlayerLoc2(player.getName(), player.getTargetBlock(null, 30).getLocation());
    }
}
