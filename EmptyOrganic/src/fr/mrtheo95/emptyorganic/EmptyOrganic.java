package fr.mrtheo95.emptyorganic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import fr.mrtheo95.emptyorganic.listeners.PlayerInterract;
import fr.mrtheo95.emptyorganic.listeners.PlayerQuit;

public class EmptyOrganic extends JavaPlugin {
	public static String prefix = "�7[�aEmptyOrganic�7] �r";
	private String help = prefix + "�c/emptyorganic <tool | empty | clear | pos1 | pos2 | undo | replace | cut | set>";
	public static List<Player> players = new ArrayList<Player>();
	public static HashMap<Player, Location> l1 = new HashMap<Player, Location>();
	public static HashMap<Player, Location> l2 = new HashMap<Player, Location>();
	
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new PlayerInterract(), this);
		getServer().getPluginManager().registerEvents(new PlayerQuit(), this);
		getLogger().info("=============== EmptyOrganic ===============");
		getLogger().info("Author: MrTheo95");
		getLogger().info("Version: " + getDescription().getVersion());
		getLogger().info("Loaded");
		getLogger().info("============================================");
	}
	
	public static void clearSelection(Player player) {
		if (l1.containsKey(player))
			l1.remove(player);
		if (l2.containsKey(player))
			l2.remove(player);
	}
	
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("The console cannot do it !");
			return true;
		}
		Player player = (Player) sender;
		if (!player.isOp()) {
			player.sendMessage(prefix + "�cVous n'avez pas la permission !");
			return true;
		}
		
		if (args.length == 0) {
			player.sendMessage(help);
			return true;
		}
		
		if (args[0].equalsIgnoreCase("tool")) {
			if (players.contains(player)) {
				players.remove(player);
				player.sendMessage(prefix + "�6Vous venez de �cd�sactiver �6l'outil de s�lection.");
				return true;
			} else {
				players.add(player);
				player.sendMessage(prefix + "�6Vous venez d'�aactiver �6l'outil de s�lection.");
				ItemStack is = new ItemStack(Material.STICK);
				ItemMeta im = is.getItemMeta();
				im.setDisplayName("�aOutil de s�lection EmptyOrganic");
				im.setLore(Arrays.asList("�6Outil de s�lection,", "�6pour �aEmptyOrganic�6."));
				is.setItemMeta(im);
				player.getInventory().addItem(is);
				player.updateInventory();
				return true;
			}
		} else if (args[0].equalsIgnoreCase("empty")) {
			if (l1.containsKey(player) && l2.containsKey(player)) {
				Cuboid cuboid = new Cuboid(l1.get(player), l2.get(player));
				ListBlock lb = new ListBlock();
				Empty empty = new Empty(player, cuboid, lb);
				empty.start();
			} else {
				player.sendMessage(prefix + "�cMerci de faire une s�lection avant de faire cela !");
			}
		} else if (args[0].equalsIgnoreCase("clear")) {
			clearSelection(player);
			player.sendMessage(prefix + "�aS�lection clear.");
			return true;
		} else if (args[0].equalsIgnoreCase("pos1")) {
			if (l1.containsKey(player))
				l1.remove(player);
			l1.put(player, player.getLocation());
			player.sendMessage(prefix + "�aPoint 1 cr�e");
			return true;
		} else if (args[0].equalsIgnoreCase("pos2")) {
			if (l2.containsKey(player))
				l2.remove(player);
			l2.put(player, player.getLocation());
			player.sendMessage(prefix + "�aPoint 2 cr�e");
		} else if (args[0].equalsIgnoreCase("undo")) {
			if (Undo.undos.containsKey(player)) {
				Undo.undos.get(player).undo();
				Undo.undos.remove(player);
				player.sendMessage(prefix + "�aUndo fait avec succ�s");
			} else
				player.sendMessage(prefix + "�cAucun undo en cache");
		} else if (args[0].equalsIgnoreCase("replace")) {
			if (args.length != 3) {
				player.sendMessage(prefix + "�c/emptyorganic replace <blocs � remplac�s (ex: 1,2:1)> <en ce bloc>");
				return true;
			}
			if (!l1.containsKey(player) || !l2.containsKey(player)) {
				player.sendMessage(prefix + "�cMerci de faire une s�lection avant de faire cela !");
				return true;
			}
			Cuboid cuboid = new Cuboid(l1.get(player), l2.get(player));
			ItemStack replaceBlock;
			if (args[2].contains(":")) {
				String[] idM = args[2].split(":");
				int id = Integer.parseInt(idM[0].replace(":", ""));
				byte meta = Byte.parseByte(idM[1].replace(":", ""));
				replaceBlock = new ItemStack(id, 1, meta);
			} else {
				int id = Integer.parseInt(args[2]);
				replaceBlock = new ItemStack(id, 1);
			}
			List<ItemStack> items = new ArrayList<ItemStack>();
			if (args[1].contains(",")) {
				String[] blocks = args[1].split(",");
				for (String id : blocks) {
					if (id.contains(":")) {
						String[] idM = id.split(":");
						int idSM = Integer.parseInt(idM[0].replace(":", ""));
						byte meta = Byte.parseByte(idM[1].replace(":", ""));
						items.add(new ItemStack(idSM, 1, meta));
					} else {
						int idM = Integer.parseInt(id);
						items.add(new ItemStack(idM, 1));
					}
				}
				Replace replace = new Replace(cuboid, player, items, replaceBlock);
				replace.replace();
				return true;
			} else {
				if (args[1].contains(":")) {
					String[] idM = args[1].split(":");
					int id = Integer.parseInt(idM[0].replace(":", ""));
					byte meta = Byte.parseByte(idM[1].replace(":", ""));
					items.add(new ItemStack(id, 1, meta));
				} else {
					int id = Integer.parseInt(args[1]);
					items.add(new ItemStack(id, 1));
				}
				Replace replace = new Replace(cuboid, player, items, replaceBlock);
				replace.replace();
				return true;
			}
		} else if (args[0].equalsIgnoreCase("cut")) {
			if (l1.containsKey(player) && l2.containsKey(player)) {
				Cuboid cuboid = new Cuboid(l1.get(player), l2.get(player));
				Cut cut = new Cut(cuboid, player);
				cut.cut();
			} else {
				player.sendMessage(prefix + "�cMerci de faire une s�lection avant de faire cela !");
			}
		} else if (args[0].equalsIgnoreCase("set")) {
			if (args.length != 2) {
				player.sendMessage(prefix + "�c/emptyorganic set <blocs � set>");
				return true;
			}
			if (!l1.containsKey(player) || !l2.containsKey(player)) {
				player.sendMessage(prefix + "�cMerci de faire une s�lection avant de faire cela !");
				return true;
			}
			Cuboid cuboid = new Cuboid(l1.get(player), l2.get(player));
			ItemStack is;
			if (args[1].contains(":")) {
				String[] idM = args[1].split(":");
				int id = Integer.parseInt(idM[0].replace(":", ""));
				byte meta = Byte.parseByte(idM[1].replace(":", ""));
				is = new ItemStack(id, 1, meta);
			} else {
				int id = Integer.parseInt(args[1]);
				is = new ItemStack(id, 1);
			}
			Set set = new Set(cuboid, player, is);
			set.set();
			return true;
		} else {
			player.sendMessage(help);
			return true;
		}
		return true;
	}

}
