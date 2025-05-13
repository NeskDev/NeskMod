package fr.neskuik.mod.api;

import lombok.Getter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Getter
public class PlayerCPSWrapper {
    @Getter
    public final static Map<UUID, PlayerCPSWrapper> players = new HashMap<>();
    public final Player player;
    public int[] clicksLeft = {0, 0, 0};
    public int[] clicksRight = {0, 0, 0};
    public int alerts = 0;
    public int maxClicks = 0;
    public int maxClicksRight = 0;
    public long lastBlockInteraction = 0;
    public long lastBlockInteractionR = 0;
    public long lastAlert = 0;




    private static final int MAX_CPS = 20;



    public PlayerCPSWrapper(Player p) {
        players.put(p.getUniqueId(), this);
        this.player = p;
    }

    public static PlayerCPSWrapper get(Player p) {
        return players.get(p.getUniqueId());
    }

    public static PlayerCPSWrapper get(UUID uuid) {
        return players.get(uuid);
    }

    public static void removePlayer(Player p) {
        players.remove(p.getUniqueId());
    }

    public String getName() {
        return player.getName();
    }

    public void handleClick(boolean isLeftClick) {
        long currentTime = System.currentTimeMillis();
        int index = (int) ((currentTime / 1000) % 3);

        if (isLeftClick) {
            clicksLeft[index]++;
            int cps = calculateCPS(clicksLeft);
            if (cps > MAX_CPS) {
                sendAlert(cps);
            }
        } else {
            clicksRight[index]++;
            int cps = calculateCPS(clicksRight);
            if (cps > MAX_CPS) {
                sendAlert(cps);
            }
        }
    }

    private int calculateCPS(int[] clicks) {
        return clicks[0] + clicks[1] + clicks[2];
    }

    private void sendAlert(int cps) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastAlert > 2000) {
            lastAlert = currentTime;
            player.sendMessage("§c§l[Alerte] §f§l• §fVous avez dépassé la limite de CPS ! (" + cps + ")");
        }
    }

    public void resetClicks() {
        clicksLeft = new int[]{0, 0, 0};
        clicksRight = new int[]{0, 0, 0};
    }

    public static Player getTargetPlayer(final Player p) {
        return getTarget(p, p.getWorld().getPlayers());
    }

    public static Player getTarget(final Entity entity, final List<Player> entities) {
        if (entity == null)
            return null;
        if (entities == null)
            return null;
        Player target = null;
        double threshold = 1;
        for (Player other : entities) {
            Vector n = other.getLocation().toVector().subtract(entity.getLocation().toVector());
            if (entity.getLocation().getDirection().normalize().crossProduct(n).lengthSquared() < threshold && n.normalize().dot(entity.getLocation().getDirection().normalize()) >= 0) {
                if (target == null || target.getLocation().distanceSquared(entity.getLocation()) > other.getLocation().distanceSquared(entity.getLocation()))
                    target = other;
            }
        }
        return target;
    }
}
