package fr.mrtheo95.emptyorganic;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Set extends Cuboid {
	private Player player;
	private ItemStack is;
	private int numberBlocks;
	private HashMap<Location, ItemStack> undo = new HashMap<Location, ItemStack>();

	public Set(Cuboid other, Player player, ItemStack is) {
		super(other);
		this.player = player;
		this.is = is;
	}
	
	@SuppressWarnings("deprecation")
	public void set() {
		for (Block block : getBlocks()) {
			ItemStack old = new ItemStack(block.getType(), 1, block.getData());
			block.setType(is.getType());
			block.setData(is.getData().getData());
			undo.put(block.getLocation(), old);
			numberBlocks++;
		}
		new Undo(player, undo);
		player.sendMessage(EmptyOrganic.prefix + "§aSet effectué §7(" + numberBlocks + " blocs placés)");
	}

}
