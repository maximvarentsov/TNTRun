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

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;

import tntrun.arena.Arena;

public class GameLevel {

	private String name;

	public GameLevel(String name) {
		this.name = name;
	}

	public String getGameLevelName() {
		return name;
	}

	private Vector gp1 = null;
	private Vector gp2 = null;

	private Vector p1 = null;

	public Vector getP1() {
		return p1;
	}

	private Vector p2 = null;

	public Vector getP2() {
		return p2;
	}

	public boolean isSandLocation(Location loc) {
		return loc.toVector().isInAABB(gp1, gp2.clone().add(new Vector(0, 1, 0)));
	}

	private HashSet<Block> blockstodestroy = new HashSet<Block>();

	public void destroyBlock(Location loc, final Arena arena) {
		final Block block = getBlockUnderPlayer(loc);
		if (block != null) {
			if (!blockstodestroy.contains(block)) {
				blockstodestroy.add(block);
				Bukkit.getScheduler().scheduleSyncDelayedTask(
					arena.plugin,
					new Runnable() {
						@Override
						public void run() {
							if (arena.getStatusManager().isArenaRunning()) {
								blockstodestroy.remove(block);
								removeGLBlocks(block);
							}
						}
					}, arena.getStructureManager().getGameLevelDestroyDelay()
				);
			}
		}
	}

	private static double PLAYER_BOUNDINGBOX_ADD = 0.3;
	private Block getBlockUnderPlayer(Location location) {
		ImmutableVector loc = new ImmutableVector(location.getX(), gp1.getY(), location.getZ());
		Block b11 = loc.add(+PLAYER_BOUNDINGBOX_ADD, 0, -PLAYER_BOUNDINGBOX_ADD).getBlock(location.getWorld());
		if (b11.getType() != Material.AIR) {
			return b11;
		}
		Block b12 = loc.add(-PLAYER_BOUNDINGBOX_ADD, 0, -PLAYER_BOUNDINGBOX_ADD).getBlock(location.getWorld());
		if (b12.getType() != Material.AIR) {
			return b12;
		}
		Block b21 = loc.add(+PLAYER_BOUNDINGBOX_ADD, 0, +PLAYER_BOUNDINGBOX_ADD).getBlock(location.getWorld());
		if (b21.getType() != Material.AIR) {
			return b21;
		}
		Block b22 = loc.add(-PLAYER_BOUNDINGBOX_ADD, 0, +PLAYER_BOUNDINGBOX_ADD).getBlock(location.getWorld());
		if (b22.getType() != Material.AIR) {
			return b22;
		}
		return null;
	}

	private LinkedList<BlockState> blocks = new LinkedList<BlockState>();

	private void removeGLBlocks(Block block) {
		blocks.add(block.getState());
		block.setType(Material.AIR);
		block = block.getRelative(BlockFace.DOWN);
		blocks.add(block.getState());
		block.setType(Material.AIR);
	}

	public void regen() {
		Iterator<BlockState> bsit = blocks.iterator();
		while (bsit.hasNext()) {
			BlockState bs = bsit.next();
			bs.update(true);
			bsit.remove();
		}
	}

	public void setGameLocation(Location p1, Location p2) {
		this.p1 = p1.toVector();
		this.p2 = p2.toVector();
		this.gp1 = p1.add(0, 1, 0).toVector();
		this.gp2 = p2.add(0, 1, 0).toVector();
		fillArea(p1.getWorld());
	}

	private void fillArea(World w) {
		int y = p1.getBlockY();
		for (int x = p1.getBlockX() + 1; x < p2.getBlockX(); x++) {
			for (int z = p1.getBlockZ() + 1; z < p2.getBlockZ(); z++) {
				Block b = w.getBlockAt(x, y, z);
				if (b.getType() == Material.AIR) {
					b.setType(Material.TNT);
				}
				b = b.getRelative(BlockFace.UP);
				if (b.getType() == Material.AIR) {
					b.setType(Material.SAND);
				}
			}
		}
	}

	private static class ImmutableVector {

		private double x;
		private double y;
		private double z;

		public ImmutableVector(double x, double y, double z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}

		public ImmutableVector add(double addx, double addy, double addz) {
			return new ImmutableVector(x + addx, y + addy, z + addz);
		}

		public Block getBlock(World world) {
			return world.getBlockAt(NumberConversions.floor(x), NumberConversions.floor(y), NumberConversions.floor(z));
		}

	}

	public void saveToConfig(FileConfiguration config) {
		config.set("gamelevels." + name + ".p1", p1);
		config.set("gamelevels." + name + ".p2", p2);
	}

	public void loadFromConfig(FileConfiguration config) {
		Vector p1 = config.getVector("gamelevels." + name + ".p1", null);
		Vector p2 = config.getVector("gamelevels." + name + ".p2", null);
		this.p1 = p1;
		this.p2 = p2;
		this.gp1 = p1.clone().add(new Vector(0, 1, 0));
		this.gp2 = p2.clone().add(new Vector(0, 1, 0));
	}

}
