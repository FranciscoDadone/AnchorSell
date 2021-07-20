package mc.nightmarephoenix.anchorsell.events;

import mc.nightmarephoenix.anchorsell.utils.UpdateChecker;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class updateMessageOnJoin implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if(!UpdateChecker.updateString.equalsIgnoreCase("") && event.getPlayer().isOp()) {
            event.getPlayer().
                    sendMessage("&5&lAnchorSell &f- There is a new update available! (v" + UpdateChecker.updateString + ").");
        }
    }

}
