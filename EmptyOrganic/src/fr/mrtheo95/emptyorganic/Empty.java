package fr.mrtheo95.emptyorganic;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Empty {
	private Player player;
	private Cuboid cuboid;
	private ListBlock listBlock;
	
	public Empty(Player player, Cuboid cuboid, ListBlock listBlocks) {
		this.player = player;
		this.cuboid = cuboid;
		this.listBlock = listBlocks;
	}
	
	private boolean isSlab(Material material) {
		switch (material) {
		case STEP:
			return true;
		case WOOD_STEP:
			return true;
			

		default:
			return false;
		}
	}
	
	@SuppressWarnings("deprecation")
	public void start() {
		for (Block block : cuboid.getBlocks()) {
			if (block.getRelative(BlockFace.UP).getType() == Material.AIR) continue;
			if (block.getRelative(BlockFace.DOWN).getType() == Material.AIR) continue;
			if (block.getRelative(BlockFace.NORTH).getType() == Material.AIR) continue;
			if (block.getRelative(BlockFace.SOUTH).getType() == Material.AIR) continue;
			if (block.getRelative(BlockFace.EAST).getType() == Material.AIR) continue;
			if (block.getRelative(BlockFace.WEST).getType() == Material.AIR) continue;
			if (isSlab(block.getRelative(BlockFace.UP).getType())) continue;
			if (isSlab(block.getRelative(BlockFace.DOWN).getType())) continue;
			if (isSlab(block.getRelative(BlockFace.NORTH).getType())) continue;
			if (isSlab(block.getRelative(BlockFace.SOUTH).getType())) continue;
			if (isSlab(block.getRelative(BlockFace.EAST).getType())) continue;
			if (isSlab(block.getRelative(BlockFace.WEST).getType())) continue;
			listBlock.blocks.add(block);
		}
		
		HashMap<Location, ItemStack> blocksSave = new HashMap<Location, ItemStack>();
		
		for (Block block : listBlock.blocks) {
			blocksSave.put(block.getLocation(), new ItemStack(block.getType(), 1, block.getData()));
			block.setType(Material.AIR);
		}
		new Undo(player, blocksSave);
		
		player.sendMessage(EmptyOrganic.prefix + "§aOrganic vidé. §7(" + listBlock.blocks.size() + " blocs remplacés)");
		listBlock.blocks.clear();
	}

}
