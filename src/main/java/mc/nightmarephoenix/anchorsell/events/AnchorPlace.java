package mc.nightmarephoenix.anchorsell.events;

import mc.nightmarephoenix.anchorsell.AnchorSell;
import mc.nightmarephoenix.anchorsell.storage.GeneralStorage;
import mc.nightmarephoenix.anchorsell.storage.PerUSerStorage;
import mc.nightmarephoenix.anchorsell.utils.Utils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import sun.security.jgss.wrapper.GSSNameElement;


public class AnchorPlace implements Listener {

    public AnchorPlace(AnchorSell plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void place(BlockPlaceEvent e) {
        Block block = e.getBlock();
        Player p = e.getPlayer();

        PerUSerStorage storage;
        GeneralStorage data;
        if(block.getType() == Material.RESPAWN_ANCHOR) {
            p.sendMessage(Utils.Color(plugin.getConfig().getString("anchor-place").replaceAll("%coordsX%", String.valueOf(block.getX())).replaceAll("%coordsY%", String.valueOf(block.getY())).replaceAll("%coordsZ%", String.valueOf(block.getZ()))));

            // saving to player config file
            storage = new PerUSerStorage(plugin, p);
            data = new GeneralStorage(plugin);

            if(storage.getConfig().contains("total")) {
                amount = storage.getConfig().getInt("total");
            }
            amount++;
            storage.getConfig().set("total", amount);

            storage.getConfig().set("anchors." + (block.getX()+block.getY()+block.getZ()) + ".location.x", block.getX());
            storage.getConfig().set("anchors." + (block.getX()+block.getY()+block.getZ()) + ".location.y", block.getY());
            storage.getConfig().set("anchors." + (block.getX()+block.getY()+block.getZ()) + ".location.z", block.getZ());
            storage.getConfig().set("anchors." + (block.getX()+block.getY()+block.getZ()) + ".location.z", block.getZ());
            storage.getConfig().set("anchors." + (block.getX()+block.getY()+block.getZ()) + ".ID", (block.getX()+block.getY()+block.getZ()));

            int level = Integer.parseInt(String.valueOf(e.getItemInHand().getItemMeta().getLore().get(2).substring(18)));

            //p.sendMessage(String.valueOf(level));

            data.getConfig().set("all_anchors." + (block.getX()+block.getY()+block.getZ()) + ".location.x", block.getX());
            data.getConfig().set("all_anchors." + (block.getX()+block.getY()+block.getZ()) + ".location.y", block.getY());
            data.getConfig().set("all_anchors." + (block.getX()+block.getY()+block.getZ()) + ".location.z", block.getZ());
            data.getConfig().set("all_anchors." + (block.getX()+block.getY()+block.getZ()) + ".owner", p.getUniqueId());
            data.getConfig().set("all_anchors." + (block.getX()+block.getY()+block.getZ()) + ".level", level);






            storage.saveConfig();
            data.saveConfig();
        }
    }

    private AnchorSell plugin;
    private int amount = 0;
}
