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

package tntrun.signs.editor;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;

@Deprecated
public class SignInfo {

	private String worldname;
	private int x;
	private int y;
	private int z;

	public SignInfo(String worldname, int x, int y, int z) {
		this.worldname = worldname;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public SignInfo(Block block) {
		this.worldname = block.getWorld().getName();
		this.x = block.getX();
		this.y = block.getY();
		this.z = block.getZ();
	}

	public Block getBlock() {
		if (worldname == null) {
			return null;
		}
		World world = Bukkit.getWorld(worldname);
		if (world != null) {
			return world.getBlockAt(x, y, z);
		}
		return null;
	}

	protected String getWorldName() {
		return worldname;
	}

	protected int getX() {
		return x;
	}

	protected int getY() {
		return y;
	}

	protected int getZ() {
		return z;
	}

}
