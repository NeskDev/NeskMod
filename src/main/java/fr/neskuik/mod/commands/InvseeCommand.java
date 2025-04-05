package fr.neskuik.mod.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

@CommandAlias("invsee")
public class InvseeCommand extends BaseCommand implements CommandExecutor {

    @Subcommand("open")
    @CommandCompletion("@players")
    @CommandPermission("join.moderation")
    public void onInvsee(Player player, Player target) {
        if (target == null || !target.isOnline()) {
            player.sendMessage("§cLe joueur n'est pas en ligne.");
            return;
        }

        Inventory targetInventory = target.getInventory();
        player.openInventory(targetInventory);
        player.sendMessage("§9§lModération §f• §aVous avez ouvert l'inventaire de §e" + target.getName() + "§a.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cSeuls les joueurs peuvent utiliser cette commande.");
            return true;
        }

        if (!player.hasPermission("invsee.use")) {
            player.sendMessage("§cVous n'avez pas la permission d'utiliser cette commande.");
            return true;
        }

        if (args.length < 1) {
            player.sendMessage("§cUtilisation: /invsee <joueur>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            player.sendMessage("§9§lModération §f• §cLe joueur n'est pas en ligne.");
            return true;
        }

        onInvsee(player, target);
        return true;
    }
}
