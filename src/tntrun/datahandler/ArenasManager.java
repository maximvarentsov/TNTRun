package tntrun.datahandler;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import tntrun.arena.Arena;

public class ArenasManager {

	private HashMap<String, Arena> arenanames = new HashMap<String, Arena>();
	
	public void registerArena(Arena arena) {
		arenanames.put(arena.getArenaName(), arena);
	}
	
	public void unregisterArena(Arena arena) {
		arenanames.remove(arena.getArenaName());
	}

	public Collection<Arena> getArenas() {
		return arenanames.values();
	}

	public Set<String> getArenasNames() {
		return arenanames.keySet();
	}

	public Arena getArenaByName(String name) {
		return arenanames.get(name);
	}
	
	public Arena getPlayerArena(String name) {
		for (Arena arena : arenanames.values()) {
			if (arena.getPlayersManager().isPlayerInArena(name)) {
				return arena;
			}
		}
		return null;
	}
	
}
