package mc.nightmarephoenix.anchorsell.events;

import mc.nightmarephoenix.anchorsell.AnchorSell;
import mc.nightmarephoenix.anchorsell.storage.Cache;
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
        ArrayList<Block> anchors = new ArrayList<>();
        Entity entity = e.getEntity(); // (primed tnt)
        int r = Math.round(e.getRadius());
        String radius = plugin.getConfig().getString("anchor.explosion-radius-break");

        if(!radius.equals("mc-default")) r = Integer.parseInt(radius);

        /**
         * Gets the nearby anchors within the explosion radius (vanilla radius).
         */
        ArrayList<Block> possibleAnchors = getNearbyAnchors(entity.getLocation(), r);

        /**
         * Loops throw the possible anchors in that explosion.
         */
        for(Block b: possibleAnchors) {
            /**
             * If it is an anchor in the explosion:
             *  - it adds it to the array list of anchors
             *  - breaks it not dropping anything
             *  - plays a sound
             *  - generates particles
             */
            if(b.getType().equals(Material.RESPAWN_ANCHOR)) {

                anchors.add(b);

                b.breakNaturally(new ItemStack(Material.AIR));
                world = entity.getLocation().getWorld();

                // Playing sound on anchor break
                world.playSound(b.getLocation(), Sound.BLOCK_BEACON_DEACTIVATE, 1f, 1f);

                /**
                 * Removes anchor from cache.
                 */
                Cache.removeAnchor(b.getLocation());


                // generate particles around on blow
                for(int i = 0; i < 360; i += 3) {
                    Location flameloc = new Location(b.getLocation().getWorld(), b.getLocation().getX(), b.getLocation().getY(), b.getLocation().getZ());
                    flameloc.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, new Location(b.getWorld(), flameloc.getX() + Math.sin(i) * 3, b.getLocation().getY(), flameloc.getZ() + Math.cos(i) * 3),10);
                }
            }
        }

        /**
         * Waiting 20 ticks to spawn the item because it can break during the explosion
         */
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            if(!anchors.isEmpty()) {
                for(Block anchor: anchors) {
                    int level = StorageManager.getAnchorLevel(plugin, anchor.getLocation());
                    world.dropItem(new Location(anchor.getWorld(), anchor.getX(), anchor.getY(), anchor.getZ()), Utils.getAnchor(level, 1)).setInvulnerable(true);

                    /**
                     Log to console the anchor blow
                     */
                    Bukkit.getLogger().info("[AnchorSell] " +
                            "Anchor blew " +
                            "(" +
                                    anchor.getX() + ", " +
                                    anchor.getY() + ", " +
                                    anchor.getZ() +
                            ")");

                    /**
                     * Saves the broken anchor to the database.
                     */
                    StorageManager.anchorBreak(plugin, anchor.getLocation());
                }
            }

        }, 20L);
    }

    /**
     * Gets the anchors around the explosion.
     *
     * @param location
     * @param radius
     * @return ArrayList
     */
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
