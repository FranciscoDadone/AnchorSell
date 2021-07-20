package mc.nightmarephoenix.anchorsell.events;

import mc.nightmarephoenix.anchorsell.utils.UpdateChecker;
import mc.nightmarephoenix.anchorsell.utils.Utils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class UpdateMessageOnJoin implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if(!UpdateChecker.updateString.equalsIgnoreCase("") && event.getPlayer().isOp()) {
            event.getPlayer().sendMessage("");
            event.getPlayer().
                    sendMessage(Utils.Color("&5&lAnchorSell &f- There is a new update available! (v" + UpdateChecker.updateString + ")."));
            event.getPlayer().
                    sendMessage(Utils.Color("&7Download it from: &ahttps://www.spigotmc.org/resources/anchorsell-earn-money-automatically-1-16-x-1-17-x.90038/"));
            event.getPlayer().sendMessage("");
        }
    }
}
