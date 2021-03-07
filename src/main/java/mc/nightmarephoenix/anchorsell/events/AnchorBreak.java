package mc.nightmarephoenix.anchorsell.events;

import mc.nightmarephoenix.anchorsell.AnchorSell;
import mc.nightmarephoenix.anchorsell.storage.PerUSerStorage;
import mc.nightmarephoenix.anchorsell.utils.Utils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class AnchorBreak implements Listener {

    public AnchorBreak(AnchorSell plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void place(BlockBreakEvent e) {
        Block block = e.getBlock();
        Player p = e.getPlayer();
        PerUSerStorage storage;

        if(block.getType() == Material.RESPAWN_ANCHOR) {
            p.sendMessage(Utils.Color(plugin.getConfig().getString("anchor-break").replaceAll("%coordsX%", String.valueOf(block.getX())).replaceAll("%coordsY%", String.valueOf(block.getY())).replaceAll("%coordsZ%", String.valueOf(block.getZ()))));

            // saving to player config file
            storage = new PerUSerStorage(plugin, p);

            if(storage.getConfig().contains("total")) {
                amount = storage.getConfig().getInt("total");
            }
            storage.getConfig().set("total", (amount - 1));
            storage.saveConfig();

        }
    }

    private int amount;
    private AnchorSell plugin;
}
