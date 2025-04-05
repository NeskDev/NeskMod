package fr.neskuik.mod.commands.sanctions;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;
import org.bukkit.ChatColor;

import java.util.concurrent.TimeUnit;

public class SanctionsCommand extends BaseCommand {

    @CommandAlias("ban")
    @CommandPermission("join.moderation")
    @Description("Sanctionner en bannissant une personne")
    @CommandCompletion("@players")
    public void banPlayer(CommandSender sender, Player target, @Optional String duration, @Optional String... reasonArgs) {
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Joueur introuvable.");
            return;
        }

        String reason = reasonArgs != null && reasonArgs.length > 0 ? String.join(" ", reasonArgs) : "Aucune raison";
        long durationMillis = parseDuration(duration);

        // TODO: Lire les paramètres depuis sanctions.yml

        if (durationMillis > 0) {
            sender.sendMessage(ChatColor.GREEN + "Vous avez banni " + target.getName() + " pendant " + duration + " pour " + reason + ".");
        } else {
            sender.sendMessage(ChatColor.GREEN + "Vous avez banni définitivement " + target.getName() + " pour " + reason + ".");
        }

        target.kickPlayer(ChatColor.RED + "Vous avez été banni.\nRaison: " + reason + "\nDurée: " + (durationMillis > 0 ? duration : "Définitif"));

        // TODO: Enregistrer la sanction dans sanctions.yml
    }

    @CommandAlias("mute")
    @CommandPermission("join.moderation")
    @Description("Sanctionner une personne en le mutant.")
    @CommandCompletion("@players")
    public void mutePlayer(CommandSender sender, Player target, @Optional String duration, @Optional String... reasonArgs) {
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Joueur introuvable.");
            return;
        }

        String reason = reasonArgs != null && reasonArgs.length > 0 ? String.join(" ", reasonArgs) : "Aucune raison";
        long durationMillis = parseDuration(duration);

        // TODO: Lire les paramètres depuis sanctions.yml

        sender.sendMessage(ChatColor.YELLOW + "Vous avez mute " + target.getName() + " pendant " + (duration != null ? duration : "indéfiniment") + " pour " + reason + ".");

        // TODO: Ajouter la logique pour bloquer les messages du joueur
        // TODO: Enregistrer la sanction dans sanctions.yml
    }

    @CommandAlias("history")
    @CommandPermission("join.moderation")
    @Description("Voir l'historique de sanction d'un joueur")
    @CommandCompletion("@players")
    public void showHistory(CommandSender sender, Player target) {

        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Joueur introuvable.");
            return;
        }

        // TODO: Lire dans sanctions.yml et afficher les sanctions
        sender.sendMessage(ChatColor.GOLD + "Historique des sanctions de " + target.getName() + " :");

        sender.sendMessage(ChatColor.GRAY + "- Mute: 7d pour Insulte (par Admin)");
        sender.sendMessage(ChatColor.GRAY + "- Ban: Définitif pour Triche (par Modérateur)");
    }

    private long parseDuration(String duration) {
        if (duration == null || duration.isEmpty()) return -1;

        try {
            long multiplier;
            String unit = duration.substring(duration.length() - 1);
            int value = Integer.parseInt(duration.substring(0, duration.length() - 1));

            switch (unit) {
                case "d":
                    multiplier = TimeUnit.DAYS.toMillis(1);
                    break;
                case "h":
                    multiplier = TimeUnit.HOURS.toMillis(1);
                    break;
                case "m":
                    multiplier = TimeUnit.MINUTES.toMillis(1);
                    break;
                default:
                    return -1;
            }

            return value * multiplier;
        } catch (Exception e) {
            return -1;
        }
    }
}
