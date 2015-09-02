package fr.mrtheo95.emptyorganic.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.mrtheo95.emptyorganic.EmptyOrganic;
import fr.mrtheo95.emptyorganic.Undo;

public class PlayerQuit implements Listener {
	
	@EventHandler
	public void quit(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		EmptyOrganic.clearSelection(player);
		if (Undo.undos.containsKey(player))
			Undo.undos.remove(player);
	}

}
