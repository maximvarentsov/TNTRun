package tntrun.arena;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class PlayersManager {

	private HashSet<String> players = new HashSet<String>();

	public boolean isPlayerInArena(String name) {
		return players.contains(name);
	}

	public int getPlayersCount() {
		return players.size();
	}

	public Set<String> getPlayersInArena() {
		return Collections.unmodifiableSet(players);
	}

	public void addPlayerToArena(String name) {
		players.add(name);
	}

	public void removePlayerFromArena(String name) {
		players.remove(name);
	}
	
}
