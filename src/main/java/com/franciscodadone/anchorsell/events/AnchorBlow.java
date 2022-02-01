package com.franciscodadone.anchorsell.events;

import com.franciscodadone.anchorsell.api.Global;
import com.franciscodadone.anchorsell.api.AnchorAPI;
import com.franciscodadone.anchorsell.utils.Logger;
import com.franciscodadone.anchorsell.AnchorSell;
import com.franciscodadone.anchorsell.utils.Utils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Objects;

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

        assert radius != null;
        if(!radius.equals("mc-default")) r = Integer.parseInt(radius);

        // Gets the nearby anchors within the explosion radius (vanilla radius).
        ArrayList<Block> possibleAnchors = getNearbyAnchors(entity.getLocation(), r);

        // Loops throw the possible anchors in that explosion.
        for(Block b: possibleAnchors) {
            //
            // If it is an anchor in the explosion:
            // - it adds it to the array list of anchors
            // - breaks it not dropping anything
            // - plays a sound
            // - generates particles
            //
            if(b.getType().equals(Material.RESPAWN_ANCHOR)) {

                anchors.add(b);

                b.breakNaturally(new ItemStack(Material.AIR));
                world = entity.getLocation().getWorld();

                // Playing sound on anchor break
                assert world != null;
                world.playSound(b.getLocation(), Sound.BLOCK_BEACON_DEACTIVATE, 1f, 1f);

                // Removes anchor from cache.
                Global.removeAnchor(AnchorAPI.getAnchorFromLoc(b.getLocation()));


                // generate particles around on blow
                for(int i = 0; i < 360; i += 3) {
                    Location flameloc = new Location(b.getLocation().getWorld(), b.getLocation().getX(), b.getLocation().getY(), b.getLocation().getZ());
                    Objects.requireNonNull(flameloc.getWorld()).spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, new Location(b.getWorld(), flameloc.getX() + Math.sin(i) * 3, b.getLocation().getY(), flameloc.getZ() + Math.cos(i) * 3),10);
                }
            }
        }

        // Waiting 20 ticks to spawn the item because it can break during the explosion
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            if(!anchors.isEmpty()) {
                for(Block anchor: anchors) {
                    int level = AnchorAPI.getAnchorLevel(anchor.getLocation());
                    world.dropItem(new Location(anchor.getWorld(), anchor.getX(), anchor.getY(), anchor.getZ()), Utils.getAnchor(level, 1)).setInvulnerable(true);

                    // Log to console the anchor blow
                    Logger.info("Anchor blew " +
                            "(" +
                                    anchor.getX() + ", " +
                                    anchor.getY() + ", " +
                                    anchor.getZ() +
                            ")");

                    // Saves the broken anchor to the database.
                    AnchorAPI.removeAnchor(Objects.requireNonNull(AnchorAPI.getAnchorFromLoc(anchor.getLocation())));
                }
            }

        }, 20L);
    }

    /**
     * Gets the anchors around the explosion.
     *
     * @param location center
     * @param radius r
     * @return ArrayList
     */
    public ArrayList<Block> getNearbyAnchors(Location location, int radius) {
        ArrayList<Block> blocks = new ArrayList<>();
        for(int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for(int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for(int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    if(new Location(location.getWorld(), x, y, z).getBlock().getType() == Material.RESPAWN_ANCHOR) {
                        blocks.add(Objects.requireNonNull(location.getWorld()).getBlockAt(x, y, z));
                    }
                }
            }
        }
        return blocks;
    }


    private World world;
    private final AnchorSell plugin;
}
