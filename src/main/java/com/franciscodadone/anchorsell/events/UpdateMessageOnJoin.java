package com.franciscodadone.anchorsell.events;

import com.franciscodadone.anchorsell.utils.UpdateChecker;
import com.franciscodadone.anchorsell.utils.Utils;
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
                    sendMessage(Utils.Color("&7Download it from: &ahttps://www.spigotmc.org/resources/90038/"));
            event.getPlayer().sendMessage("");
        }
    }
}
