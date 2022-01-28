package com.franciscodadone.anchorsell.tasks;

import com.franciscodadone.anchorsell.AnchorSell;
import com.franciscodadone.anchorsell.api.Global;
import com.franciscodadone.anchorsell.api.StorageManager;
import com.franciscodadone.anchorsell.models.Anchor;
import com.franciscodadone.anchorsell.thirdparty.vault.EconomyManager;
import com.franciscodadone.anchorsell.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;


import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Objects;

public class PayTask extends BukkitRunnable {

    /**
     *
     * Handles the pay to all users online from a given minutes in the config file.
     *
     */

    public PayTask(AnchorSell plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        lastUserPayment = System.currentTimeMillis();

        @SuppressWarnings("all")
        Collection<Player> onlinePlayers = (Collection<org.bukkit.entity.Player>) Bukkit.getOnlinePlayers();

        // Revalidates all the anchors.
        StorageManager.revalidateAll();

        // Pays to all online players.
        new Thread(() -> {
            // Sleeps for 1sec to wait for the chunk to unload in the revalidation process.
            // (if not loaded from a player)
            try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }

            for(Player p: onlinePlayers) {
                boolean playerAfk = false; //EssentialsManager.getEssentials().getUser(p).isAfk();

                if(Global.plugin.getConfig().getBoolean("pay-afk-players") || !playerAfk) {
                    double totalAmount = 0;

                    for(Anchor playerAnchor : StorageManager.getAllPlayerAnchors(p)) {
                        boolean loaded = true;
                        // Checks if the chunk that the anchor is on is loaded.
                        if(Global.plugin.getConfig().getBoolean("pay-if-chunk-is-loaded")) {
                            Location anchorLocation = playerAnchor.getLocation();
                            loaded = Objects.requireNonNull(anchorLocation.getWorld()).isChunkLoaded((anchorLocation.getBlockX() >> 4), (anchorLocation.getBlockZ() >> 4));
                        }
                        if(loaded) {
                            int level = playerAnchor.getLevel();

                            double amount = Utils.getMoneyPerMinute(level) * plugin.getConfig().getInt("pay-timer-in-minutes");
                            totalAmount += amount;
                        }
                    }
                    if(totalAmount != 0) {
                        EconomyManager.getEconomy().depositPlayer(p, totalAmount);
                        p.sendMessage(Utils.Color(Objects.requireNonNull(plugin.getConfig().getString("paying-message")).replaceAll("%amount%", String.valueOf(new DecimalFormat("0.00").format(totalAmount)))));
                    }
                }
            }
        }).start();
    }

    public static long getLastUserPayment() {
        return lastUserPayment;
    }

    private static long lastUserPayment;
    private final AnchorSell plugin;
}
