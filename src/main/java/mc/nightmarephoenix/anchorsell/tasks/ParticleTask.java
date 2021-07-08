package mc.nightmarephoenix.anchorsell.tasks;

import mc.nightmarephoenix.anchorsell.AnchorSell;
import mc.nightmarephoenix.anchorsell.storage.StorageManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;

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
        Collection<Player> onlinePlayers = (Collection<org.bukkit.entity.Player>) Bukkit.getOnlinePlayers();

        for(Player p : onlinePlayers) {
            final int radius = 30;
            Location playerLocation = p.getLocation();
            Location startOfTheBox = new Location(
                    p.getWorld(),
                    playerLocation.getX() - radius,
                    playerLocation.getY() - radius,
                    playerLocation.getZ() - radius
            );

            for(int i = 0; i < (radius * 2) + 1; i++) {
                for(int j = 0; j < (radius * 2) + 1; j++) {
                    for(int k = 0; k < (radius * 2) + 1; k++) {
                        Block block = new Location(startOfTheBox.getWorld(), startOfTheBox.getBlock().getX() + i, startOfTheBox.getBlock().getY() + j, startOfTheBox.getBlock().getZ() + k).getBlock();

                        if(block.getType().equals(Material.RESPAWN_ANCHOR)) {
                            if(StorageManager.anchorIsRegistered(plugin, block.getLocation())) {

                                block.getLocation().getWorld().spawnParticle(
                                        Particle.PORTAL,
                                        new Location(
                                                block.getWorld(),
                                                block.getX() + 0.5,
                                                block.getY() + 1,
                                                block.getZ() + 0.5
                                                ),
                                        5);


                                for(int a = 0; a < 360; a += 10) {
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
                        }

                    }
                }
            }
        }


    }

    private AnchorSell plugin;

}
