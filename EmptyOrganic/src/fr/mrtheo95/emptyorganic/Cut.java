package fr.mrtheo95.emptyorganic;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Cut extends Cuboid {
	private Player player;
	private int numberBlocks;
	private HashMap<Location, ItemStack> undo = new HashMap<Location, ItemStack>();

	public Cut(Cuboid cuboid, Player player) {
		super(cuboid);
		this.player = player;
	}
	
	@SuppressWarnings("deprecation")
	public void cut() {
		for (Block block : getBlocks()) {
			ItemStack is = new ItemStack(block.getType(), 1, block.getData());
			if (block.getType() == Material.AIR) continue;
			block.setType(Material.AIR);
			undo.put(block.getLocation(), is);
			numberBlocks++;
		}
		new Undo(player, undo);
		player.sendMessage(EmptyOrganic.prefix + "§aCut effectué §7(" + numberBlocks + " blocs cut)");
	}

}
