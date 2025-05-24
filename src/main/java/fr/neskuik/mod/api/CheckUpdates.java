package fr.neskuik.mod.api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONObject;

public class CheckUpdates {

    private static JavaPlugin plugin;
    private static final String API_URL = "https://api.github.com/repos/NeskDev/NeskMod/releases/latest";
    private static boolean updateNotified = false;

    public CheckUpdates(JavaPlugin plugin) {
        CheckUpdates.plugin = plugin;
    }

    public static void checkForUpdate() {
        if (updateNotified) return; // Empêche les doublons

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(API_URL).openConnection();
                connection.setRequestProperty("Accept", "application/vnd.github.v3+json");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                StringBuilder response = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                }

                JSONObject json = new JSONObject(response.toString());
                String latestVersion = json.getString("tag_name").replace("v", "").trim();
                String currentVersion = plugin.getDescription().getVersion().trim();

                if (!currentVersion.equalsIgnoreCase(latestVersion)) {
                    updateNotified = true;

                    Bukkit.getScheduler().runTask(plugin, () -> {
                        Bukkit.getLogger().warning("[NeskMod] Une mise à jour est disponible : v" + latestVersion +
                                " (actuelle : v" + currentVersion + ")");
                        Bukkit.getLogger().warning("[NeskMod] Téléchargez-la ici : https://github.com/NeskDev/NeskMod/releases/latest");

                        for (Player playerOnline : Bukkit.getOnlinePlayers()) {
                            if (playerOnline.isOp()) {
                                playerOnline.sendMessage("§cUne nouvelle mise à jour de §eNeskMod §cest disponible : §e" + latestVersion);
                                playerOnline.sendMessage("§cTéléchargez-la ici : §ehttps://github.com/NeskDev/NeskMod/releases/latest");
                            }
                        }
                    });
                }

            } catch (IOException e) {
                Bukkit.getLogger().warning("[NeskMod] Impossible de vérifier la mise à jour : " + e.getMessage());
            }
        });
    }
}
