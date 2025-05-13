package fr.neskuik.mod.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import fr.neskuik.mod.Main;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashSet;

@CommandAlias("mod")
@CommandPermission("join.moderation|Vous n'avez pas la permission d'utiliser cette commande.")
public class ModCommand extends BaseCommand {

    private final VanishCommand vanish;
    private final Main plugin;
    private final HashSet<Player> modPlayers;

    public ModCommand(Main instance, VanishCommand vanish) {
        this.plugin = instance;
        this.vanish = vanish;
        this.modPlayers = new HashSet<>();
    }

    @Default
    public void onMod(Player player) {
        if (player == null) return;

        if (modPlayers.contains(player)) {
            modPlayers.remove(player);

            player.getInventory().clear();

            player.sendMessage("§9§lModération §f• §7Vous avez §cdésactivé §7votre §emode modération§7.");

            if (vanish != null) {
                vanish.showPlayerToAll(player);
            } else {
                player.sendMessage("§cErreur : Impossible d'afficher les joueurs (Vanish non initialisé).");
            }
        } else {
            modPlayers.add(player);

            vanish.hidePlayerFromAll(player);

            player.getInventory().clear();

            giveItem(player, Material.DIAMOND_SWORD, "§cEpée KB", Enchantment.KNOCKBACK, 2, 0);
            giveItem(player, Material.ICE, "§bFreeze", null, 0, 1);
            giveItem(player, Material.NETHER_STAR, "§6Panel de Modération", null, 0, 4);

            if (player.hasPermission("core.supervanish")) {
                giveItem(player, Material.BONE, "§dSuper-Vanish", null, 0, 6);
            }

            giveItem(player, Material.ENDER_PEARL, "§aVanish", null, 0, 7);
            giveItem(player, Material.PAPER, "§bCPS", null, 0, 8);

            player.sendMessage("§9§lModération §f• §7Vous êtes §adésormais §7en §emode modération.");
        }
    }

    public void disableMod(Player player) {
        if (modPlayers.contains(player)) {
            modPlayers.remove(player);

            player.getInventory().clear();

            player.sendMessage("§9§lModération §f• §7Vous avez §cdésactivé §7votre §emode modération§7.");

            if (vanish != null) {
                vanish.showPlayerToAll(player);
            } else {
                player.sendMessage("§cErreur : Impossible d'afficher les joueurs (Vanish non initialisé).");
            }
        }
    }

    private void giveItem(Player player, Material material, String name, Enchantment enchantment, int level, int slot) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(name);
            if (enchantment != null) {
                meta.addEnchant(enchantment, level, true);
            }
            item.setItemMeta(meta);
        }

        player.getInventory().setItem(slot, item);
    }

    public boolean isInModMode(Player player) {
        return modPlayers != null && modPlayers.contains(player);
    }

}