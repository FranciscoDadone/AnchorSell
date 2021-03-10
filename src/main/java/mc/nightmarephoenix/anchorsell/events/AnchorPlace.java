package mc.nightmarephoenix.anchorsell.events;

import mc.nightmarephoenix.anchorsell.AnchorSell;
import mc.nightmarephoenix.anchorsell.storage.StorageManager;
import mc.nightmarephoenix.anchorsell.utils.Utils;
import org.bukkit.*;
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
        World anchorInWorld = block.getWorld();
        boolean isInWorld = false;

        // Busca si el bloque esta en enable-in-worlds
        for (String world : plugin.getConfig().getStringList("enable-in-worlds")) {
            if (anchorInWorld.getName().equalsIgnoreCase(world)) {
                isInWorld = true;
                break;
            }
        }

        // Si no esta en enable-in-worlds se sale del método
        if (!isInWorld) {
            e.setCancelled(true);
            return;
        }

        Player p = e.getPlayer();
        Location loc = new Location(anchorInWorld, block.getX(), block.getY(), block.getZ());

        if(block.getType() == Material.RESPAWN_ANCHOR) {
            if(analyzeLocation(new Location(block.getWorld(), block.getX() - 3, block.getY() - 3, block.getZ() - 3), loc, plugin.getConfig().getInt("safe-anchor-area"))) {
                if(!StorageManager.anchorPlace(plugin, e, p, loc)) {
                    return;
                }

                // generate sound on place
                p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1f, 1f);

                // generate particles around on place
                for(int i = 0; i < 360; i += 5) {
                    Location flameloc = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ());
                    flameloc.setZ(flameloc.getZ() + Math.cos(i) * 5);
                    flameloc.setX(flameloc.getX() + Math.sin(i) * 5);
                    loc.getWorld().playEffect(flameloc, Effect.POTION_BREAK, 51);
                }

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
