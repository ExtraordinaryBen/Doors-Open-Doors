package net.mcbat.doorsopendoors.listeners;

import net.mcbat.doorsopendoors.DoorsOpenDoors;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;



public class DoorsOpenDoorsPlayerListener implements Listener {

	public DoorsOpenDoorsPlayerListener(DoorsOpenDoors plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);

	}
	
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.isCancelled())
			return;
		
		if (event.getClickedBlock().getType() == Material.WOODEN_DOOR) {
			
			Block doorBlockClicked = event.getClickedBlock();
			Block door1Top = null;
			Block door1Bottom = null;
			
			Block door2Bottom = null;

			if ( doorIsTopHalf(doorBlockClicked.getData()) ) {
				door1Top = doorBlockClicked;
				door1Bottom = doorBlockClicked.getRelative(BlockFace.DOWN);
			}
			else {
				door1Top = doorBlockClicked.getRelative(BlockFace.UP);
				door1Bottom = doorBlockClicked;
			}
			
			
			if (door1Bottom.getRelative(BlockFace.NORTH).getType() == Material.WOODEN_DOOR && doorIsConnected(door1Top.getData(), door1Top.getRelative(BlockFace.NORTH).getData())) {
				door2Bottom = door1Bottom.getRelative(BlockFace.NORTH);
			}
			else if (door1Bottom.getRelative(BlockFace.SOUTH).getType() == Material.WOODEN_DOOR && doorIsConnected(door1Top.getData(), door1Top.getRelative(BlockFace.SOUTH).getData())) {
				door2Bottom = door1Bottom.getRelative(BlockFace.SOUTH);
			}
			else if (door1Bottom.getRelative(BlockFace.EAST).getType() == Material.WOODEN_DOOR && doorIsConnected(door1Top.getData(), door1Top.getRelative(BlockFace.EAST).getData())) {
				door2Bottom = door1Bottom.getRelative(BlockFace.EAST);
			}
			else if (door1Bottom.getRelative(BlockFace.WEST).getType() == Material.WOODEN_DOOR && doorIsConnected(door1Top.getData(), door1Top.getRelative(BlockFace.WEST).getData())) {
				door2Bottom = door1Bottom.getRelative(BlockFace.WEST);
			}
			

			if (door2Bottom != null) {
				
				if ( doorIsOpen(door1Bottom.getData()) == doorIsOpen(door2Bottom.getData())) {
					door2Bottom.setData(flipDoor(door2Bottom.getData()));
				}
			}
		}
	}
	
	private boolean doorIsConnected(byte door1Top, byte door2Top) {
		if ((door1Top & 0x1) == (door2Top & 0x1)) //If hinges are both the same side.
			return false;
		else 
			return true;
		
	}
	
	private boolean doorIsTopHalf(byte data) {
		if((data & 0x8) == 0x08)
			return true;
		else 
			return false;

	}
	
	private boolean doorIsOpen(byte data) {
		if((data & 0x4) == 0x4) 
			return true;
		else 
			return false;

	}
	
	private byte flipDoor(byte data) {
		return (byte) (doorIsOpen(data) ? (data & ~0x4) : (data | 0x4));
	}
}
