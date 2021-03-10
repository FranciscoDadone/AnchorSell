package mc.nightmarephoenix.anchorsell.events;

import mc.nightmarephoenix.anchorsell.AnchorSell;
import mc.nightmarephoenix.anchorsell.storage.StorageManager;
import mc.nightmarephoenix.anchorsell.utils.Utils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class AnchorBlow implements Listener {

    public AnchorBlow(AnchorSell plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onExplosion(ExplosionPrimeEvent e) {
        Entity entity = e.getEntity();
        int r = Math.round(e.getRadius());
        String radius = plugin.getConfig().getString("anchor.explotion-raius-break");
        if(!radius.equals("mc-default"))
            r = Integer.parseInt(radius);
        ArrayList<Block> possibleAnchors = getNearbyAnchors(entity.getLocation(), r);

        Location anchorLoc = new Location(e.getEntity().getLocation().getWorld(), 0, 0, 0);

        for(Block b: possibleAnchors) {
            if(b.getType().equals(Material.RESPAWN_ANCHOR)) {
                b.breakNaturally(new ItemStack(Material.AIR));
                world = entity.getLocation().getWorld();
                anchorLoc = new Location(b.getLocation().getWorld(), b.getLocation().getX(), b.getLocation().getY(), b.getLocation().getZ());

                // Playing sound on anchor break
                world.playSound(b.getLocation(), Sound.BLOCK_BEACON_DEACTIVATE, 1f, 1f);

                // generate particles around on place
                for(int i = 0; i < 360; i += 3) {
                    Location flameloc = new Location(b.getLocation().getWorld(), b.getLocation().getX(), b.getLocation().getY(), b.getLocation().getZ());
                    flameloc.getWorld().spawnParticle(Particle.FLAME,new Location(b.getWorld(), flameloc.getX() + Math.sin(i) * 3, b.getLocation().getY(), flameloc.getZ() + Math.cos(i) * 3),10);
                }
                StorageManager.anchorBreak(plugin, b.getLocation());
            }
        }

        Location finalAnchorLoc = anchorLoc;
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            world.dropItem(new Location(finalAnchorLoc.getWorld(), finalAnchorLoc.getX(), finalAnchorLoc.getY(), finalAnchorLoc.getZ()), Utils.getAnchor(StorageManager.getAnchorLevel(plugin, finalAnchorLoc), 1)).setInvulnerable(true);
        }, 20L);
    }

    public ArrayList<Block> getNearbyAnchors(Location location, int radius) {
        ArrayList<Block> blocks = new ArrayList<Block>();
        for(int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for(int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for(int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    if(new Location(location.getWorld(), x, y, z).getBlock().getType() == Material.RESPAWN_ANCHOR) {
                        blocks.add(location.getWorld().getBlockAt(x, y, z));
                    }
                }
            }
        }
        return blocks;
    }


    private World world;
    private AnchorSell plugin;

}
