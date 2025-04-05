package fr.neskuik.mod.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@CommandAlias("tph")
@Description("Téléporte un joueur vers soi")
public class TphCommand extends BaseCommand {

    @Default
    @CommandCompletion("@players")
    public void onTph(Player sender, String targetName) {
        Player target = Bukkit.getPlayer(targetName);
        if (target != null && target.isOnline()) {
            target.teleport(sender.getLocation());
            sender.sendMessage("§7Vous avez téléporté §c" + target.getName() + "§7 vers vous.");
        } else {
            sender.sendMessage("§c[Erreur] Joueur non connecté(e).");
        }
    }
}