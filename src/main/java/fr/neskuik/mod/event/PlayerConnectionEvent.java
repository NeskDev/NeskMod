package fr.neskuik.mod.event;

import fr.neskuik.mod.api.CheckUpdates;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerConnectionEvent implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        CheckUpdates.checkForUpdate();

    }
}
