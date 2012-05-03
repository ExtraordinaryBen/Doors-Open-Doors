package net.mcbat.doorsopendoors.listeners;

import net.mcbat.doorsopendoors.DoorsOpenDoors;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.player.PlayerInteractEvent;



public class DoorsOpenDoorsPlayerListener implements Listener {

	public DoorsOpenDoorsPlayerListener(DoorsOpenDoors plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void OnIronDoorPowered(BlockRedstoneEvent event) {
		
		Material blockType = event.getBlock().getType();
		
		if( blockType == Material.IRON_DOOR_BLOCK || blockType == Material.WOODEN_DOOR) {
				Block doorBlock = event.getBlock();
				Block door1Top = null;
				Block door1Bottom = null;
				
				Block door2Bottom = null;

				if ( doorIsTopHalf(doorBlock.getData()) ) {
					door1Top = doorBlock;
					door1Bottom = doorBlock.getRelative(BlockFace.DOWN);
				}
				else {
					door1Top = doorBlock.getRelative(BlockFace.UP);
					door1Bottom = doorBlock;
				}
				
				
				door2Bottom = findOtherDoor(door1Bottom, door1Top, blockType);
				

				if (door2Bottom != null) {
					if ( doorIsOpen(door1Bottom.getData()) == doorIsOpen(door2Bottom.getData())) {
						door2Bottom.setData(flipDoor(door2Bottom.getData()));
					}
				}
			}
		
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
			
			
			door2Bottom = findOtherDoor(door1Bottom, door1Top, Material.WOODEN_DOOR);
			

			if (door2Bottom != null) {
				
				if ( doorIsOpen(door1Bottom.getData()) == doorIsOpen(door2Bottom.getData())) {
					door2Bottom.setData(flipDoor(door2Bottom.getData()));
				}
			}
		}
	}
	
	private Block findOtherDoor(Block doorBottom, Block doorTop, Material doorType) {
		
		if (doorBottom.getRelative(BlockFace.NORTH).getType() == doorType && doorIsConnected(doorTop.getData(), doorTop.getRelative(BlockFace.NORTH).getData())) {
			return doorBottom.getRelative(BlockFace.NORTH);
		}
		else if (doorBottom.getRelative(BlockFace.SOUTH).getType() == doorType && doorIsConnected(doorTop.getData(), doorTop.getRelative(BlockFace.SOUTH).getData())) {
			return doorBottom.getRelative(BlockFace.SOUTH);
		}
		else if (doorBottom.getRelative(BlockFace.EAST).getType() == doorType && doorIsConnected(doorTop.getData(), doorTop.getRelative(BlockFace.EAST).getData())) {
			return doorBottom.getRelative(BlockFace.EAST);
		}
		else if (doorBottom.getRelative(BlockFace.WEST).getType() == doorType && doorIsConnected(doorTop.getData(), doorTop.getRelative(BlockFace.WEST).getData())) {
			return doorBottom.getRelative(BlockFace.WEST);
		}
		else
			return null;

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
