package fr.neskuik.mod.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import fr.neskuik.mod.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

@CommandAlias("vanish")
@CommandPermission("join.moderation|§fCommande Inconnu(e).")
public class VanishCommand extends BaseCommand {

    private final Plugin plugin;

    public VanishCommand(Main plugin) {
        this.plugin = JavaPlugin.getPlugin(Main.class);
    }

    @Default
    public void vanish(Player player) {
        if (isVanished(player)) {
            showPlayerToAll(player);
            player.sendMessage("§9§lModération §f• §aTu es maintenant §avisible.");
        } else {
            hidePlayerFromAll(player);
            player.sendMessage("§9§lModération §f• §aTu es maintenant §cinvisible.");
        }
    }

    @Subcommand("supervanish")
    @CommandPermission("vanish.supervanish|§fCommande Inconnu(e).")
    public void superVanish(Player player) {
        if (isSuperVanish(player)) {
            showPlayerToAll(player);
            player.sendMessage("§9§lModération §f• §aTu es maintenant §avisible en super vanish.");
        } else {
            hidePlayerFromSuperVanish(player);
            player.sendMessage("§9§lModération §f• §aTu es maintenant en §csuper vanish.");
        }
    }

    public boolean isVanished(Player player) {
        return player.hasMetadata("vanished");
    }

    public boolean isSuperVanish(Player player) {
        return player.hasMetadata("supervanished");
    }

    public void hidePlayerFromAll(Player player) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!p.hasPermission("join.moderation")) {
                p.hidePlayer(player);
            }
        }
        player.setMetadata("vanished", new FixedMetadataValue(plugin, true));
        player.removeMetadata("supervanished", plugin);
    }

    public void showPlayerToAll(Player player) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.showPlayer(player);
        }
        player.removeMetadata("vanished", plugin);
        player.removeMetadata("supervanished", plugin);
    }

    public void hidePlayerFromSuperVanish(Player player) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!p.hasPermission("core.supervanish")) {
                p.hidePlayer(player);
            }
        }
        player.setMetadata("supervanished", new FixedMetadataValue(plugin, true));
        player.setMetadata("vanished", new FixedMetadataValue(plugin, true));
    }

    public void reloadVanish() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (isVanished(player)) {
                hidePlayerFromAll(player);
            }
            if (isSuperVanish(player)) {
                hidePlayerFromSuperVanish(player);
            }
        }
    }
}