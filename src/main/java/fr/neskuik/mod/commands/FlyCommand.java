package fr.neskuik.mod.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("fly")
public class FlyCommand extends BaseCommand implements CommandExecutor {

    @Default("fly")
    @CommandPermission("fly.use")
    public void onFlyToggle(Player player) {
        boolean isFlying = player.isFlying();
        player.setAllowFlight(!isFlying); // Autoriser ou désactiver le vol
        player.setFlying(!isFlying); // Activer ou désactiver le vol

        if (!isFlying) {
            player.sendMessage("§aVous êtes maintenant en mode vol.");
        } else {
            player.sendMessage("§cVous avez quitté le mode vol.");
        }
    }

    @Subcommand("status")
    @CommandPermission("join.moderation")
    public void onFlyStatus(Player player) {
        if (player.getAllowFlight()) {
            player.sendMessage("§aVous êtes actuellement en mode vol.");
        } else {
            player.sendMessage("§cVous n'êtes pas en mode vol.");
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cSeuls les joueurs peuvent utiliser cette commande.");
            return true;
        }

        if (!player.hasPermission("fly.use")) {
            player.sendMessage("§c[Erreur] Vous n'avez pas la permission d'utiliser cette commande.");
            return true;
        }

        if (args.length == 0 || args[0].equalsIgnoreCase("toggle")) {
            onFlyToggle(player);
        } else if (args[0].equalsIgnoreCase("status")) {
            onFlyStatus(player);
        } else {
            player.sendMessage("§cUtilisation: /fly <toggle/status>");
        }

        return true;
    }
}
