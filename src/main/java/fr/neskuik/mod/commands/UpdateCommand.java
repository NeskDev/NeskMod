package fr.neskuik.mod.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

@CommandAlias("neskmod")
@Description("Télécharge la dernière version de NeskMod depuis GitHub")
@CommandPermission("neskmod.admin")
public class UpdateCommand extends BaseCommand {

    private final JavaPlugin plugin;
    private final Logger logger;

    private static final String API_URL = "https://api.github.com/repos/NeskDev/NeskMod/releases/latest";

    public UpdateCommand(JavaPlugin plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
    }

    @Subcommand("update")
    public void onUpdateCommand(CommandSender sender) {
        sender.sendMessage("§7[§bNeskMod§7] §fTéléchargement de la dernière version...");

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                // Get latest release JSON
                HttpURLConnection connection = (HttpURLConnection) new URL(API_URL).openConnection();
                connection.setRequestProperty("Accept", "application/vnd.github.v3+json");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                StringBuilder response = new StringBuilder();
                try (BufferedInputStream reader = new BufferedInputStream(connection.getInputStream())) {
                    int ch;
                    while ((ch = reader.read()) != -1) {
                        response.append((char) ch);
                    }
                }

                // Parse download URL
                String body = response.toString();
                String downloadUrl = body.split("\"browser_download_url\":\"")[1].split("\"")[0];

                // Download file
                URL url = new URL(downloadUrl);
                File updateDir = new File(plugin.getDataFolder().getParentFile(), "update");
                if (!updateDir.exists()) updateDir.mkdirs();

                File outputFile = new File(updateDir, "NeskMod.jar");

                try (BufferedInputStream in = new BufferedInputStream(url.openStream());
                     FileOutputStream fileOutputStream = new FileOutputStream(outputFile)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = in.read(buffer, 0, 1024)) != -1) {
                        fileOutputStream.write(buffer, 0, bytesRead);
                    }
                }

                Bukkit.getScheduler().runTask(plugin, () -> {
                    sender.sendMessage("§aLa dernière version de §eNeskMod§a a été téléchargée !");
                    sender.sendMessage("§7Elle sera chargée au prochain redémarrage.");
                });

            } catch (IOException | ArrayIndexOutOfBoundsException e) {
                Bukkit.getScheduler().runTask(plugin, () -> {
                    sender.sendMessage("§cErreur lors du téléchargement : " + e.getMessage());
                });
                logger.warning("[NeskMod] Échec du téléchargement de la dernière version : " + e.getMessage());
            }
        });
    }
}
