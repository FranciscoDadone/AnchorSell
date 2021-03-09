package mc.nightmarephoenix.anchorsell.events;

import mc.nightmarephoenix.anchorsell.AnchorSell;
import mc.nightmarephoenix.anchorsell.storage.PerUSerStorage;
import mc.nightmarephoenix.anchorsell.storage.StorageManager;
import mc.nightmarephoenix.anchorsell.utils.Utils;
import org.bukkit.Location;
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
    public void breakEv(BlockBreakEvent e) {
        Block block = e.getBlock();
        Player p = e.getPlayer();

        if(block.getType() == Material.RESPAWN_ANCHOR) {
            Location location = block.getLocation();
            // Announcing to the user that the anchor has been removed
            Utils.Color(plugin.getConfig().getStringList("anchor-break")).forEach((str) -> {
                p.sendMessage(str.replaceAll("%coordsX%", String.valueOf(location.getX())).
                        replaceAll("%coordsY%", String.valueOf(location.getY())).
                        replaceAll("%coordsZ%", String.valueOf(location.getZ())).
                        replaceAll("%level%", String.valueOf(StorageManager.getAnchorLevel(plugin, location))));
            });

            e.setDropItems(false);
            p.getWorld().dropItem(location, Utils.getAnchor(StorageManager.getAnchorLevel(plugin, location), 1)).setInvulnerable(true);
            StorageManager.anchorBreak(plugin, location);
        }
    }

    private AnchorSell plugin;
}
