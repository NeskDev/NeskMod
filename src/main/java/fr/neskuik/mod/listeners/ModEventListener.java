package fr.neskuik.mod.listeners;

import fr.neskuik.mod.api.PlayerCPSWrapper;
import fr.neskuik.mod.commands.FreezeCommand;
import fr.neskuik.mod.commands.ModCommand;
import fr.neskuik.mod.commands.VanishCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MainHand;

public class ModEventListener implements Listener {

    private final ModCommand modCommand;
    private final VanishCommand vanishCommand;

    public ModEventListener(ModCommand modCommand, VanishCommand vanishCommand) {
        this.modCommand = modCommand;
        this.vanishCommand = vanishCommand;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        if (modCommand.isInModMode(player)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        modCommand.disableMod(player);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        if (modCommand.isInModMode(player)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        if (modCommand.isInModMode(player)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player player) {
            if (modCommand.isInModMode(player)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent e) {
        if (e.getWhoClicked() instanceof Player player) {
            if (modCommand.isInModMode(player)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();

        if (modCommand.isInModMode(player)) {
            if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                ItemStack item = player.getItemInHand();

                if (item != null) {
                    switch (item.getType()) {

                        case ICE: {
                            Player targetPlayer = PlayerCPSWrapper.getTargetPlayer(player);

                            if (targetPlayer == null) {
                                player.sendMessage("§9§lMOD §f• §cVous devez viser un joueur correctement.");
                                return;
                            }

                            if (targetPlayer == player) {
                                player.sendMessage("§9§lMOD §f• §cVous ne pouvez pas vous geler vous-même.");
                                return;
                            }

                            if (FreezeCommand.frozenPlayers.contains(targetPlayer)) {
                                FreezeCommand.Unfreeze(targetPlayer);
                            } else {
                                FreezeCommand.Freeze(targetPlayer);
                            }
                            break;
                        }

                        case NETHER_STAR: {
                            player.performCommand("panel");
                            break;
                        }

                        case BONE: {
                            vanishCommand.superVanish(player);
                            break;
                        }

                        case ENDER_PEARL: {
                            e.setCancelled(true);
                            vanishCommand.vanish(player);
                            break;
                        }

                        case PAPER: {
                            Player targetPlayer = PlayerCPSWrapper.getTargetPlayer(player);

                            if (targetPlayer == null) {
                                player.sendMessage("§9§lMOD §f• §cVous devez viser un joueur correctement.");
                                return;
                            }

                            player.chat("/cps " + targetPlayer.getName());
                            break;
                        }

                        default:
                            break;
                    }
                }
            }
        }
    }


}