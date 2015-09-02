package fr.mrtheo95.emptyorganic.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import fr.mrtheo95.emptyorganic.EmptyOrganic;

public class PlayerInterract implements Listener {
	
	@EventHandler
	public void interract(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		if (EmptyOrganic.players.contains(player)) {
			if (player.getItemInHand().getType() == Material.STICK) {
				if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
					e.setCancelled(true);
					if (EmptyOrganic.l1.containsKey(player))
						EmptyOrganic.l1.remove(player);
					EmptyOrganic.l1.put(player, e.getClickedBlock().getLocation());
					player.sendMessage(EmptyOrganic.prefix + "§aPoint 1 crée");
				} else if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
					e.setCancelled(true);
					if (EmptyOrganic.l2.containsKey(player))
						EmptyOrganic.l2.remove(player);
					EmptyOrganic.l2.put(player, e.getClickedBlock().getLocation());
					player.sendMessage(EmptyOrganic.prefix + "§aPoint 2 crée");
				}
			}
		}
	}

}
