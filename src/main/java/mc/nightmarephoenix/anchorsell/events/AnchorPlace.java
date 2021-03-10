package mc.nightmarephoenix.anchorsell.events;

import mc.nightmarephoenix.anchorsell.AnchorSell;
import mc.nightmarephoenix.anchorsell.storage.StorageManager;
import mc.nightmarephoenix.anchorsell.utils.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.RespawnAnchor;
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

        Location loc = new Location(block.getWorld(), block.getX(), block.getY(), block.getZ());

        if(block.getType() == Material.RESPAWN_ANCHOR) {
            if(analyzeLocation(new Location(block.getWorld(), block.getX() - 3, block.getY() - 3, block.getZ() - 3), loc, plugin.getConfig().getInt("safe-anchor-area"))) {
                StorageManager.anchorPlace(plugin, e, p, loc);

                // Determines witch level of glowstone the anchor needs
                Material i = Utils.getAnchorOreLevel(StorageManager.getAnchorLevel(plugin, loc));
                int charges = 0;
                if(i == Material.IRON_INGOT)
                    charges = 1;
                else if(i == Material.GOLD_INGOT)
                    charges = 2;
                else if(i == Material.DIAMOND)
                    charges = 3;
                else if(i == Material.NETHERITE_INGOT)
                    charges = 4;
                Block b = loc.getBlock();
                RespawnAnchor anchor = (RespawnAnchor) b.getBlockData();
                anchor.setCharges(charges);
                b.setBlockData(anchor);
            } else {
                p.sendMessage(Utils.Color(plugin.getConfig().getString("radius-error")));
                e.setCancelled(true);
            }
        }
    }

    public boolean analyzeLocation(Location startOfTheBox, Location anchor, int radius) {
        for(int i = 0; i < (radius * 2) + 1; i++) {
            for(int j = 0; j < (radius * 2) + 1; j++) {
                for(int k = 0; k < (radius * 2) + 1; k++) {
                    Block b = new Location(startOfTheBox.getWorld(), startOfTheBox.getBlock().getX() + i, startOfTheBox.getBlock().getY() + j, startOfTheBox.getBlock().getZ() + k).getBlock();
                    if(!anchor.getBlock().equals(b) && b.getType() == Material.RESPAWN_ANCHOR) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private AnchorSell plugin;
}
