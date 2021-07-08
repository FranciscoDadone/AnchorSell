package mc.nightmarephoenix.anchorsell.tasks;

import mc.nightmarephoenix.anchorsell.AnchorSell;
import mc.nightmarephoenix.anchorsell.storage.Cache;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;


public class ParticleTask extends BukkitRunnable {

    /**
     *
     * Handles the particles around the anchors.
     *
     */

    public ParticleTask(AnchorSell plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        /**
         * Gets all the cached anchors.
         * Sees if a player is near that anchor.
         * If yes, it ticks a particle effect.
         * If no, breaks and continues with another player.
         */
        for(Location loc : Cache.getAllAnchors()) {
            for(Player p : Bukkit.getOnlinePlayers()) {
                if(p.getWorld().equals(loc.getWorld())) {
                    if(p.getLocation().distanceSquared(loc) < 30 * 30) {

                        Block block = loc.getBlock();

                        if (block.getType().equals(Material.RESPAWN_ANCHOR)) {
                            block.getLocation().getWorld().spawnParticle(
                                    Particle.PORTAL,
                                    new Location(
                                            block.getWorld(),
                                            block.getX() + 0.5,
                                            block.getY() + 1,
                                            block.getZ() + 0.5
                                    ),
                                    5);

                            for (int a = 0; a < 360; a += 10) {
                                Location flameloc = new Location(block.getLocation().getWorld(), block.getLocation().getX(), block.getLocation().getY(), block.getLocation().getZ());
                                flameloc.setX(flameloc.getX() + 0.5 + Math.sin(a) * 0.3);
                                flameloc.setZ(flameloc.getZ() + 0.5 + Math.cos(a) * 0.3);
                                flameloc.setY(flameloc.getY() + 2.2);

                                block.getLocation().getWorld().spawnParticle(
                                        Particle.END_ROD,
                                        flameloc,
                                        0
                                );
                                block.getLocation().getWorld().spawnParticle(
                                        Particle.DRIPPING_OBSIDIAN_TEAR,
                                        new Location(flameloc.getWorld(), flameloc.getX(), flameloc.getY() - 1.2, flameloc.getZ()),
                                        0
                                );
                            }

                            block.getLocation().getWorld().spawnParticle(
                                    Particle.SOUL,
                                    new Location(
                                            block.getWorld(),
                                            block.getX() + 0.5,
                                            block.getY() + 1.6,
                                            block.getZ() + 0.5
                                    ),
                                    0, 0, 0, 0
                            );

                            block.getLocation().getWorld().spawnParticle(
                                    Particle.FLAME,
                                    new Location(
                                            block.getWorld(),
                                            block.getX() + 0.5,
                                            block.getY() + 1.1,
                                            block.getZ() + 0.5
                                    ),
                                    0, 0, 0, 0
                            );
                        }
                        break;
                    }
                }
            }
        }
    }

    private AnchorSell plugin;

}
