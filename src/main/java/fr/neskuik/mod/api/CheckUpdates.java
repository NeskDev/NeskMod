package fr.neskuik.mod.api;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONArray;
import org.json.JSONObject;

public class CheckUpdates {

    private static JavaPlugin plugin;
    private static final String API_URL = "https://api.github.com/repos/NeskDev/NeskMod/releases/latest";
    private static boolean updateNotified = false;

    public CheckUpdates(JavaPlugin plugin) {
        CheckUpdates.plugin = plugin;
    }

    public static void init(JavaPlugin pluginInstance) {
        plugin = pluginInstance;
    }

    public static void checkForUpdate() {
        if (updateNotified) return;

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
                        Bukkit.getLogger().warning("[NeskMod] Téléchargez-la ici : https://github.com/NeskDev/NeskMod/releases/latest ou en fesant /neskmod update !");

                        for (Player playerOnline : Bukkit.getOnlinePlayers()) {
                            if (playerOnline.isOp()) {
                                playerOnline.sendMessage("§cUne nouvelle mise à jour de §eNeskMod §cest disponible : §e" + latestVersion);
                                playerOnline.sendMessage("§cTéléchargez-la en fesant /neskmod update !");
                            }
                        }
                    });
                }

            } catch (IOException e) {
                Bukkit.getLogger().warning("[NeskMod] Impossible de vérifier la mise à jour : " + e.getMessage());
            }
        });
    }

    public static void downloadLatestJar(Player sender) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(API_URL).openConnection();
                connection.setRequestProperty("Accept", "application/vnd.github.v3+json");

                StringBuilder response = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                }

                JSONObject json = new JSONObject(response.toString());
                JSONArray assets = json.getJSONArray("assets");

                String downloadUrl = null;
                for (int i = 0; i < assets.length(); i++) {
                    JSONObject asset = assets.getJSONObject(i);
                    String name = asset.getString("name");
                    if (name.endsWith(".jar")) {
                        downloadUrl = asset.getString("browser_download_url");
                        break;
                    }
                }

                if (downloadUrl == null) {
                    sender.sendMessage("§cAucun fichier .jar trouvé dans la dernière release !");
                    return;
                }

                // Télécharger dans plugins/update/
                File updateDir = new File("plugins/update");
                if (!updateDir.exists()) updateDir.mkdirs();

                File outputFile = new File(updateDir, "NeskMod.jar");

                try (BufferedInputStream in = new BufferedInputStream(new URL(downloadUrl).openStream());
                     FileOutputStream fileOutputStream = new FileOutputStream(outputFile)) {
                    byte[] dataBuffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                        fileOutputStream.write(dataBuffer, 0, bytesRead);
                    }
                }

                sender.sendMessage("§aNeskMod " + json.getString("tag_name") + " téléchargé dans §e/plugins/update/");
                sender.sendMessage("§6Redémarrez le serveur pour appliquer la mise à jour.");

            } catch (Exception e) {
                sender.sendMessage("§cErreur lors du téléchargement de la mise à jour : " + e.getMessage());
            }
        });
    }
}
