package mc.nightmarephoenix.anchorsell.tasks;

import mc.nightmarephoenix.anchorsell.AnchorSell;
import mc.nightmarephoenix.anchorsell.economy.EconomyManager;
import mc.nightmarephoenix.anchorsell.storage.StorageManager;
import mc.nightmarephoenix.anchorsell.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;

public class PayTask extends BukkitRunnable {

    public PayTask(AnchorSell plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        Collection<Player> onlinePlayers = (Collection<org.bukkit.entity.Player>) Bukkit.getOnlinePlayers();
        for(Player p: onlinePlayers) {
            double totalAmount = 0;
            for(int i = 1; i <= StorageManager.getPlayerTotalAnchors(plugin, p); i++) {
                int level = StorageManager.getUserData(plugin, p).getConfig().getInt("anchors." + i + ".level");
                double amount = Utils.getMoneyPerMinute(level) * plugin.getConfig().getInt("pay-timer-in-minutes");
                totalAmount += amount;
                EconomyManager.getEconomy().depositPlayer(p, amount);
            }
            p.sendMessage(Utils.Color(plugin.getConfig().getString("paying-message").replaceAll("%amount%", String.valueOf(totalAmount))));
        }
    }

    private AnchorSell plugin;
}
