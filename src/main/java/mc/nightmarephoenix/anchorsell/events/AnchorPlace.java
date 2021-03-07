package mc.nightmarephoenix.anchorsell.events;

import mc.nightmarephoenix.anchorsell.AnchorSell;
import mc.nightmarephoenix.anchorsell.storage.Storage;
import mc.nightmarephoenix.anchorsell.utils.Utils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;


public class AnchorPlace implements Listener {

    public AnchorPlace(AnchorSell plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void place(BlockPlaceEvent e) {
        Block block = e.getBlock();
        Player p = e.getPlayer();

        Storage storage;
        if(block.getType() == Material.RESPAWN_ANCHOR) {
            p.sendMessage(Utils.Color(plugin.getConfig().getString("anchor-place").replaceAll("%coordsX%", String.valueOf(block.getX())).replaceAll("%coordsY%", String.valueOf(block.getY())).replaceAll("%coordsZ%", String.valueOf(block.getZ()))));

            // saving to player config file
            storage = new Storage(plugin, p);

            if(storage.getConfig().contains("total")) {
                amount = storage.getConfig().getInt("total");
            }
            amount++;
            storage.getConfig().set("total", amount);

            storage.getConfig().set("anchors." + amount + ".location.x", block.getX());
            storage.getConfig().set("anchors." + amount + ".location.y", block.getY());
            storage.getConfig().set("anchors." + amount + ".location.z", block.getZ());
            storage.getConfig().set("anchors." + amount + ".location.z", block.getZ());




            storage.saveConfig();
        }
    }

    private AnchorSell plugin;
    private int amount = 0;
}
