package net.mcbat.doorsopendoors;

import java.util.logging.Logger;

import net.mcbat.doorsopendoors.listeners.DoorsOpenDoorsPlayerListener;

import org.bukkit.plugin.java.JavaPlugin;

public class DoorsOpenDoors extends JavaPlugin {
	private final Logger _logger = Logger.getLogger("minecraft");
	
	@Override
	public void onEnable() {
		_logger.info("[Doors Open Doors] v"+this.getDescription().getVersion()+" (Helium) enabled.");
		_logger.info("[Doors Open Doors] Developed by: [Mattera, Steven (IchigoKyger)].");
		_logger.info("[Doors Open Doors] Based on: Kassoon's single player mod.");

		(new DoorsOpenDoorsPlayerListener(this)).registerEvents();
	}
	
	@Override
	public void onDisable() {
		_logger.info("[Doors Open Doors] v"+this.getDescription().getVersion()+" (Helium) disabled.");
	}
	
	public Logger getMinecraftLogger() {
		return _logger;
	}
}
