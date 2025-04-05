package fr.neskuik.mod.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InvseeListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        String inventoryTitle = event.getView().getTitle();

        if (inventoryTitle.contains("Inventaire de")) {
            event.setCancelled(true);
            player.sendMessage("§7Vous ne pouvez pas modifier l'inventaire d'un autre joueur.");
        }
    }
}
