package fr.mrtheo95.emptyorganic;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Replace extends Cuboid {
	private Player player;
	private List<ItemStack> replace;
	private ItemStack replaceBlock;
	private int numberBlocks;
	private HashMap<Location, ItemStack> undo = new HashMap<Location, ItemStack>();

	public Replace(Cuboid cuboid, Player player, List<ItemStack> replace, ItemStack replaceBlock) {
		super(cuboid);
		this.player = player;
		this.replace = replace;
		this.replaceBlock = replaceBlock;
	}
	
	@SuppressWarnings("deprecation")
	public void replace() {
		for (Block block : getBlocks()) {
			ItemStack is = new ItemStack(block.getType(), 1, block.getData());
			if (!replace.contains(is)) continue;
			block.setType(replaceBlock.getType());
			block.setData(replaceBlock.getData().getData());
			undo.put(block.getLocation(), is);
			numberBlocks++;
		}
		new Undo(player, undo);
		player.sendMessage(EmptyOrganic.prefix + "§aRemplacement effectué §7(" + numberBlocks + " blocs remplacé)");
	}

}
