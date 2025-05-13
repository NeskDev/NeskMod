package fr.neskuik.mod.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Optional;
import fr.neskuik.mod.api.PlayerCPSWrapper;
import org.bukkit.entity.Player;

import static org.bukkit.Bukkit.getPlayer;

@CommandAlias("cps")
public class CPSCommand extends BaseCommand {

    @Default
    public void onCPSCommand(Player player, @Optional String targetName) {
        PlayerCPSWrapper cps;
        Player targetPlayer = getPlayer(targetName);

        player.sendMessage("§7§m-----------------------------");
        if (targetPlayer != null) {
            player.sendMessage("§7CPS Monitor for §6" + targetPlayer.getDisplayName());
            cps = PlayerCPSWrapper.get(targetPlayer);
        } else {
            player.sendMessage("§7Your CPS Monitor");
            cps = PlayerCPSWrapper.get(player);
        }

        long sum = 0;
        long count = 0;
        for (int i : cps.clicksLeft) {
            sum += i;
            count++;
        }
        long result = 0;
        long count1 = 0;
        for (int i : cps.clicksRight) {
            result += i;
            count1++;
        }

        player.sendMessage("§7Last Click Left/Right §f: §6" + cps.clicksLeft[1] + " §7| §e" + cps.clicksRight[1]);
        player.sendMessage("§7Ratio Click Left/Right §f: §6" + (int) (count > 0 ? (double) sum / count : 0) + " §7| §e" + (int) (count1 > 0 ? (double) result / count1 : 0));
        player.sendMessage("§7Maximal Click Left/Right §f: §6" + cps.maxClicks + " §7| §e" + cps.maxClicksRight);
        player.sendMessage("§7§m-----------------------------");
    }


}
