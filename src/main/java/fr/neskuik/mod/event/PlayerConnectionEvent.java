package fr.neskuik.mod.event;

import fr.neskuik.mod.api.CheckUpdates;
import fr.neskuik.mod.commands.ModCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConnectionEvent implements Listener {

    private final ModCommand mod;

    public PlayerConnectionEvent(ModCommand mod) {
        this.mod = mod;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        CheckUpdates.checkForUpdate();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        if (mod.isInModMode(player)) {
            mod.disableMod(player);
        }
    }
}
