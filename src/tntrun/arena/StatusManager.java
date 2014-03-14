package tntrun.arena;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import tntrun.arena.structure.GameLevel;
import tntrun.messages.Messages;

public class StatusManager {

	private Arena arena;
	public StatusManager(Arena arena) {
		this.arena = arena;
	}
	
	private boolean enabled = false;
	private boolean starting = false;
	private boolean running = false;
	private boolean regenerating = false;

	// arena status handler
	public boolean isArenaEnabled() {
		return enabled;
	}

	public boolean enableArena() {
		if (arena.getStructureManager().isArenaConfigured().equalsIgnoreCase("yes")) {
			enabled = true;
			arena.arenagh.startArenaAntiLeaveHandler();
			arena.plugin.signEditor.modifySigns(arena.getArenaName());
			return true;
		}
		return false;
	}

	public void disableArena() {
		enabled = false;
		// drop players
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (arena.getPlayersManager().isPlayerInArena(player.getName())) {
				arena.arenaph.leavePlayer(player, Messages.arenadisabling, "");
			}
		}
		// stop arena
		arena.arenagh.stopArena();
		// stop countdown
		arena.arenagh.stopArenaCountdown();
		// stop antileave handler
		arena.arenagh.stopArenaAntiLeaveHandler();
		// regen gamelevels
		for (GameLevel gl : arena.getStructureManager().getGameLevels()) {
			gl.regen();
		}
		// modify signs
		arena.plugin.signEditor.modifySigns(arena.getArenaName());
	}

	public boolean isArenaStarting() {
		return starting;
	}

	public void setStarting(boolean starting) {
		this.starting = starting;
	}

	public boolean isArenaRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public boolean isArenaRegenerating() {
		return regenerating;
	}

	public void setRegenerating(boolean regenerating) {
		this.regenerating = regenerating;
	}
	
}
