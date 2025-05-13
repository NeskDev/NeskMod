package fr.neskuik.mod.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.Bukkit.getPlayer;

@CommandAlias("fly")
public class FlyCommand extends BaseCommand {

    @Default("fly")
    @CommandPermission("fly.use")
    public void onFlyToggle(Player player, @Optional String targetName) {
        Player target = getPlayer(targetName);

        if (target == null) {
            boolean isFlying = player.isFlying();
            player.setAllowFlight(!isFlying); // Autoriser ou désactiver le vol
            player.setFlying(!isFlying); // Activer ou désactiver le vol

            if (!isFlying) {
                player.sendMessage("§aVous êtes maintenant en mode vol.");
            } else {
                player.sendMessage("§cVous avez quitté le mode vol.");
            }
        } else {
            boolean isFlying = target.isFlying();
            target.setAllowFlight(!isFlying); // Autoriser ou désactiver le vol
            target.setFlying(!isFlying); // Activer ou désactiver le vol
            if (!isFlying) {
                player.sendMessage("§aVous avez activé le vol pour " + target.getName() + ".");
                target.sendMessage("§aVous êtes maintenant en mode vol.");
            } else {
                player.sendMessage("§cVous avez désactivé le vol pour " + target.getName() + ".");
                target.sendMessage("§cVous avez quitté le mode vol.");
            }
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

}
