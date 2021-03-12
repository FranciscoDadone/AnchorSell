package mc.nightmarephoenix.anchorsell.events;

import mc.nightmarephoenix.anchorsell.AnchorSell;
import mc.nightmarephoenix.anchorsell.storage.StorageManager;
import mc.nightmarephoenix.anchorsell.utils.Utils;
import org.bukkit.*;
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

            if(!Utils.isPlayerInHisFaction(block, p) && !Utils.isBlockInWilderness(block)) {
                return;
            }

            // Si no esta registrado el anchor no hace nada
            if (!StorageManager.isARegisterAnchor(plugin, location)) {
                return;
            }

            // Playing sound on anchor break
            p.playSound(p.getLocation(), Sound.BLOCK_BEACON_DEACTIVATE, 1f, 1f);

            // generate particles around on place
            for(int i = 0; i < 360; i += 3) {
                Location flameloc = new Location(e.getBlock().getLocation().getWorld(), e.getBlock().getLocation().getX(), e.getBlock().getLocation().getY(), e.getBlock().getLocation().getZ());
                p.getWorld().spawnParticle(Particle.FLAME, new Location(p.getWorld(), flameloc.getX() + Math.sin(i) * 2, e.getBlock().getLocation().getY(), flameloc.getZ() + Math.cos(i) * 2),10);
            }

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
