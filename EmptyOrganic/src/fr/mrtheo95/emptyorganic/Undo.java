package fr.mrtheo95.emptyorganic;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Undo {
	public static HashMap<Player, Undo> undos = new HashMap<Player, Undo>();
	private HashMap<Location, ItemStack> blocks = new HashMap<Location, ItemStack>();
	
	public Undo(Player player, HashMap<Location, ItemStack> blocks) {
		clearUndo(player);
		this.blocks = blocks;
		undos.put(player, this);
	}
	
	@SuppressWarnings("deprecation")
	public void undo() {
		for (Location loc : blocks.keySet()) {
			loc.getWorld().getBlockAt(loc).setType(blocks.get(loc).getType());
			loc.getWorld().getBlockAt(loc).setData(blocks.get(loc).getData().getData());
		}
	}
	
	public static void clearUndo(Player player) {
		if (undos.containsKey(player))
			undos.remove(player);
	}
}
