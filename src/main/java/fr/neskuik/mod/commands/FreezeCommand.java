package fr.neskuik.mod.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashSet;
import java.util.Set;

@CommandAlias("freeze")
public class FreezeCommand extends BaseCommand implements Listener {

    private static final Set<Player> frozenPlayers = new HashSet<>();

    @Default("freeze")
    @CommandPermission("join.moderation|§fCommande Inconnu(e).")
    @CommandCompletion("@players")
    public void onFreeze(Player sender, Player target) {
        if (target == null || !target.isOnline()) {
            sender.sendMessage("§c[Erreur] §fLe joueur n'est pas en ligne.");
            return;
        }

        if (frozenPlayers.contains(target)) {
            frozenPlayers.remove(target);
            target.setWalkSpeed(0.2f);
            target.setAllowFlight(false);
            target.sendMessage("§aVous avez été dégelé.");
            sender.sendMessage("§9§lModération §f• §aVous avez dégelé §e" + target.getName() + "§a.");
        } else {
            frozenPlayers.add(target);
            target.setWalkSpeed(0.0f);
            target.setAllowFlight(true);
            target.sendMessage("§cVous avez été gelé, vous ne pouvez plus bouger.");
            sender.sendMessage("§9§lModération §f• §aVous avez gelé §e" + target.getName() + "§a.");
        }
    }

    @Subcommand("status")
    @CommandPermission("join.moderation")
    @CommandCompletion("@players")
    public void onFreezeStatus(Player player, Player target) {
        if (target == null || !target.isOnline()) {
            player.sendMessage("§c[Erreur] §fLe joueur n'est pas en ligne.");
            return;
        }

        if (frozenPlayers.contains(target)) {
            player.sendMessage("§9§lModération §f• §e" + target.getName() + " §cest actuellement gelé.");
        } else {
            player.sendMessage("§9§lModération §f• §e" + target.getName() + " §aest libre de ses mouvements.");
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (frozenPlayers.contains(player)) {
            if (event.getFrom().getY() != event.getTo().getY()) {
                event.setCancelled(true);
            }
        }
    }

    public static void Freeze(Player target) {
        frozenPlayers.add(target);
        target.setWalkSpeed(0.0f);
        target.setAllowFlight(true);
    }

    public static void Unfreeze(Player target) {
        frozenPlayers.remove(target);
        target.setWalkSpeed(0.2f);
        target.setAllowFlight(false);
    }
}