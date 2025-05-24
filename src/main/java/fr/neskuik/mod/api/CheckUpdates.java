package fr.neskuik.mod.api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONObject;

public class CheckUpdates {

    private final JavaPlugin plugin;
    private final String apiUrl = "https://api.github.com/repos/NeskDev/NeskMod/releases/latest";

    public CheckUpdates(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void checkForUpdate() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
                connection.setRequestProperty("Accept", "application/vnd.github.v3+json");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JSONObject json = new JSONObject(response.toString());
                String latestVersion = json.getString("tag_name").replace("v", ""); // Ex: "v1.0.1" -> "1.0.1"
                String currentVersion = plugin.getDescription().getVersion();

                if (!currentVersion.equalsIgnoreCase(latestVersion)) {
                    Bukkit.getLogger().warning("[NeskMod] Une mise à jour est disponible : v" + latestVersion +
                            " (actuelle : v" + currentVersion + ")");
                    Bukkit.getLogger().warning("[NeskMod] Téléchargez-la ici : https://github.com/NeskDev/NeskMod/releases/latest");
                } else {

                }

            } catch (IOException e) {
                Bukkit.getLogger().warning("[NeskMod] Impossible de vérifier la mise à jour : " + e.getMessage());
            }
        });
    }
}
