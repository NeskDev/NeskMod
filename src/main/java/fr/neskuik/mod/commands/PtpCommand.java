package fr.neskuik.mod.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@CommandAlias("ptp|tpa")
@CommandPermission("join.moderation")
@Description("Téléporte vous vers un joueur")
public class PtpCommand extends BaseCommand {

    @Default
    @CommandCompletion("@players")
    public void onPtp(Player sender, String targetName) {
        Player target = Bukkit.getPlayer(targetName);
        if (target != null && target.isOnline()) {
            sender.teleport(target.getLocation());
            sender.sendMessage("§7Vous avez été téléporté vers §c" + target.getName() + "§7.");
        } else {
            sender.sendMessage("§c[Erreur] Joueur non connecté(e).");
        }
    }
}