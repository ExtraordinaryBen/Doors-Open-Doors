package net.mcbat.doorsopendoors.listeners;

import net.mcbat.doorsopendoors.DoorsOpenDoors;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;

public class DoorsOpenDoorsPlayerListener extends PlayerListener {
	private final DoorsOpenDoors _plugin;
	
	public DoorsOpenDoorsPlayerListener(DoorsOpenDoors plugin) {
		_plugin = plugin;
	}
	
	public void registerEvents() {
		_plugin.getServer().getPluginManager().registerEvent(Event.Type.PLAYER_INTERACT, this, Priority.Highest, _plugin);
	}
	
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.isCancelled())
			return;
		
		if (event.getClickedBlock().getType() == Material.WOODEN_DOOR) {
			Block doorBlock = event.getClickedBlock();
			Block otherDoorTopBlock = null;
			Block otherDoorBottomBlock = null;

			if (this.doorIsTopHalf(doorBlock.getData()))
				doorBlock = doorBlock.getRelative(BlockFace.DOWN);
			
			if (doorBlock.getRelative(BlockFace.NORTH).getType() == Material.WOODEN_DOOR && doorIsConnected(doorBlock.getData(), doorBlock.getRelative(BlockFace.NORTH).getData())) {
				otherDoorBottomBlock = doorBlock.getRelative(BlockFace.NORTH);
			}
			else if (doorBlock.getRelative(BlockFace.SOUTH).getType() == Material.WOODEN_DOOR && doorIsConnected(doorBlock.getData(), doorBlock.getRelative(BlockFace.SOUTH).getData())) {
				otherDoorBottomBlock = doorBlock.getRelative(BlockFace.SOUTH);
			}
			else if (doorBlock.getRelative(BlockFace.EAST).getType() == Material.WOODEN_DOOR && doorIsConnected(doorBlock.getData(), doorBlock.getRelative(BlockFace.EAST).getData())) {
				otherDoorBottomBlock = doorBlock.getRelative(BlockFace.EAST);
			}
			else if (doorBlock.getRelative(BlockFace.WEST).getType() == Material.WOODEN_DOOR && doorIsConnected(doorBlock.getData(), doorBlock.getRelative(BlockFace.WEST).getData())) {
				otherDoorBottomBlock = doorBlock.getRelative(BlockFace.WEST);
			}

			if (otherDoorBottomBlock != null) {
				otherDoorTopBlock = otherDoorBottomBlock.getRelative(BlockFace.UP);
				
				if (this.doorIsOpen(doorBlock.getData()) != this.doorIsOpen(otherDoorTopBlock.getData())) {
					otherDoorTopBlock.setData(flipDoor(otherDoorTopBlock.getData()));
					otherDoorBottomBlock.setData(flipDoor(otherDoorBottomBlock.getData()));
				}
			}
		}
	}
	
	private boolean doorIsConnected(byte data, byte data2) {
		int f1 = -1, f2 = -1, h1 = -1, h2 = -1;

		f1 = (data & 0x3);
		f2 = (data2 & 0x3);
		
		if ((data & 0x3) == 0x3)		h1 = 3;
		else if ((data & 0x1) == 0x1)	h1 = 1;
		else if ((data & 0x2) == 0x2)	h1 = 2;
		else							h1 = 0;

		if ((data2 & 0x3) == 0x3)		h2 = 3;
		else if ((data2 & 0x1) == 0x1)	h2 = 1;
		else if ((data2 & 0x2) == 0x2)	h2 = 2;
		else							h2 = 0;
		
		
		if (f1 == f2 && f2 == h1 && h1 == h2)
			return false;
		
		if (f1 == h1 && f2 == h2)
			return true;
		
		return false;
	}
	
	private boolean doorIsTopHalf(byte data) {
		return ((data & 0x8) == 0x8);
	}
	
	private boolean doorIsOpen(byte data) {
		return ((data & 0x4) == 0x4);
	}
	
	private byte flipDoor(byte data) {
		return (byte) (doorIsOpen(data) ? (data & ~0x4) : (data | 0x4));
	}
}
