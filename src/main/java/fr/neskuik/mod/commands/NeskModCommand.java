package fr.neskuik.mod.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;

import fr.neskuik.mod.api.CheckUpdates;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

@CommandAlias("neskmod")
@Description("Télécharge la dernière version de NeskMod depuis GitHub")
@CommandPermission("neskmod.admin")
public class NeskModCommand extends BaseCommand {

    private final JavaPlugin plugin;
    private final Logger logger;

    private static final String API_URL = "https://api.github.com/repos/NeskDev/NeskMod/releases/latest";

    public NeskModCommand(JavaPlugin plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
    }

    @Subcommand("update")
    public void onUpdateCommand(CommandSender sender) {
        sender.sendMessage("§7Téléchargement de la dernière version de §eNeskMod§7...");
        CheckUpdates.downloadLatestJar((Player) sender);
    }
}
