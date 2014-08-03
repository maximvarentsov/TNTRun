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
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.util.NumberConversions;
import tntrun.TNTRun;
import tntrun.arena.Arena;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

public class GameZone {

    private Set<Block> blockstodestroy = new HashSet<>();
    private final int SCAN_DEPTH = 1;
    private final int MAX_BLOCKS_PER_TICK = 50;
    private static double PLAYER_BOUNDINGBOX_ADD = 0.3;
    private LinkedList<BlockState> blocks = new LinkedList<>();

    public void destroyBlock(Location loc, final Arena arena) {
        int y = loc.getBlockY();
        Block block = null;
        for (int i = 0; i <= SCAN_DEPTH; i++) {
            block = getBlockUnderPlayer(y, loc);
            y--;
            if (block != null) {
                break;
            }
        }
        if (block != null) {
            final Block fblock = block;
            if (!blockstodestroy.contains(fblock)) {
                blockstodestroy.add(fblock);
                Bukkit.getScheduler().scheduleSyncDelayedTask(
                        arena.plugin,
                        () -> {
                            if (arena.getStatusManager().isArenaRunning()) {
                                blockstodestroy.remove(fblock);
                                removeGLBlocks(fblock);
                            }
                        }, arena.getStructureManager().getGameLevelDestroyDelay()
                );
            }
        }
    }

    private Block getBlockUnderPlayer(int y, Location location) {
        PlayerPosition loc = new PlayerPosition(location.getX(), y, location.getZ());
        Block b11 = loc.getBlock(location.getWorld(), +PLAYER_BOUNDINGBOX_ADD, -PLAYER_BOUNDINGBOX_ADD);
        if (b11.getType() != Material.AIR) {
            return b11;
        }
        Block b12 = loc.getBlock(location.getWorld(), -PLAYER_BOUNDINGBOX_ADD, +PLAYER_BOUNDINGBOX_ADD);
        if (b12.getType() != Material.AIR) {
            return b12;
        }
        Block b21 = loc.getBlock(location.getWorld(), +PLAYER_BOUNDINGBOX_ADD, +PLAYER_BOUNDINGBOX_ADD);
        if (b21.getType() != Material.AIR) {
            return b21;
        }
        Block b22 = loc.getBlock(location.getWorld(), -PLAYER_BOUNDINGBOX_ADD, -PLAYER_BOUNDINGBOX_ADD);
        if (b22.getType() != Material.AIR) {
            return b22;
        }
        return null;
    }

    private void removeGLBlocks(Block block) {
        blocks.add(block.getState());
        block.setType(Material.AIR);
        block = block.getRelative(BlockFace.DOWN);
        blocks.add(block.getState());
        block.setType(Material.AIR);
    }

    public void regenNow() {
        Iterator<BlockState> bsit = blocks.iterator();
        while (bsit.hasNext()) {
            BlockState bs = bsit.next();
            bs.update(true);
            bsit.remove();
        }
    }

    public int regen(TNTRun plugin) {
        final Iterator<BlockState> bsit = blocks.iterator();
        int ticks = 1;
        for (;ticks <= (blocks.size() / MAX_BLOCKS_PER_TICK) + 1; ticks++) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin,
                    () -> {
                        while (bsit.hasNext()) {
                            BlockState bs = bsit.next();
                            bs.update(true);
                            bsit.remove();
                        }
                    },
                    ticks
            );
        }
        return ticks;
    }

    private static class PlayerPosition {

        private double x;
        private int y;
        private double z;

        public PlayerPosition(double x, int y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Block getBlock(World world, double addx, double addz) {
            return world.getBlockAt(NumberConversions.floor(x + addx), y, NumberConversions.floor(z + addz));
        }

    }

}