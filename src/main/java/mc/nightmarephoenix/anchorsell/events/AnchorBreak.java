package mc.nightmarephoenix.anchorsell.events;

import mc.nightmarephoenix.anchorsell.AnchorSell;
import mc.nightmarephoenix.anchorsell.storage.PerUSerStorage;
import mc.nightmarephoenix.anchorsell.storage.StorageManager;
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

        if(block.getType() == Material.RESPAWN_ANCHOR) {
            p.sendMessage(Utils.Color(plugin.getConfig().getString("anchor-break").replaceAll("%coordsX%", String.valueOf(block.getX())).replaceAll("%coordsY%", String.valueOf(block.getY())).replaceAll("%coordsZ%", String.valueOf(block.getZ()))));

            e.setDropItems(false);
            p.getWorld().dropItem(block.getLocation(), Utils.getAnchor(StorageManager.getAnchorLevel(block.getLocation()), 1)).setInvulnerable(true);
            StorageManager.anchorBreak(plugin, block.getLocation());
        }
    }

    private AnchorSell plugin;
}
