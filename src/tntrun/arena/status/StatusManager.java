package tntrun.arena.status;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import tntrun.arena.Arena;
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

	public boolean isArenaEnabled() {
		return enabled;
	}

	public boolean enableArena() {
		if (arena.getStructureManager().isArenaConfigured().equalsIgnoreCase("yes")) {
			enabled = true;
			arena.getGameHandler().startArenaAntiLeaveHandler();
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
				arena.getPlayerHandler().leavePlayer(player, Messages.arenadisabling, "");
			}
		}
		// stop arena
		arena.getGameHandler().stopArena();
		// stop countdown
		arena.getGameHandler().stopArenaCountdown();
		// stop antileave handler
		arena.getGameHandler().stopArenaAntiLeaveHandler();
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
