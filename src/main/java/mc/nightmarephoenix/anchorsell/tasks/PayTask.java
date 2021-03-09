package mc.nightmarephoenix.anchorsell.tasks;

import mc.nightmarephoenix.anchorsell.AnchorSell;
import mc.nightmarephoenix.anchorsell.economy.EconomyManager;
import mc.nightmarephoenix.anchorsell.storage.GeneralStorage;
import mc.nightmarephoenix.anchorsell.storage.StorageManager;
import mc.nightmarephoenix.anchorsell.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import sun.java2d.loops.FillRect;

import java.util.ArrayList;
import java.util.Collection;

public class PayTask extends BukkitRunnable {

    public PayTask(AnchorSell plugin, double time) {
        this.plugin = plugin;
        this.time = time;
    }

    @Override
    public void run() {
        Bukkit.broadcastMessage("Paying...");

        Collection<Player> onlinePlayers = (Collection<org.bukkit.entity.Player>) Bukkit.getOnlinePlayers();


        for(Player p: onlinePlayers) {
            Collection<String> all_user_keys = StorageManager.getUserData(plugin, p).getConfig().getKeys(true);

//            String str = "";
//            ArrayList<String> anchors = new ArrayList<String>();
//            while(all_user_keys.iterator().hasNext()) { // Removing all the user keys that not have 'anchors'
//                str = (String) all_user_keys.iterator().next();
//                p.sendMessage(str);
//                if(str.contains("anchors")) {
//                    anchors.add(all_user_keys.iterator().toString());
//                }
//            }

//            while(all_user_keys.iterator().hasNext()) { // Removing all the user keys that not have 'anchors'
//                str = (String) all_user_keys.iterator().next();
//
//            }



//            p.sendMessage(String.valueOf(anchors));
//            for(String anchor: playerAnchors) {
//                int level = StorageManager.getUserData(plugin, p).getConfig().getInt("anchors." + anchor + ".level");
//                boolean a = EconomyManager.depositToUser(p, Utils.getMoneyPerMinute(level) * time);
//            }
        }
    }


    private double time;
    private AnchorSell plugin;
}
