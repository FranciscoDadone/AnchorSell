package mc.nightmarephoenix.anchorsell.events;

import mc.nightmarephoenix.anchorsell.AnchorSell;
import mc.nightmarephoenix.anchorsell.hooks.FactionsX;
import mc.nightmarephoenix.anchorsell.hooks.Global;
import mc.nightmarephoenix.anchorsell.storage.StorageManager;
import mc.nightmarephoenix.anchorsell.utils.Utils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class AnchorBreak implements Listener {

    public AnchorBreak(AnchorSell plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void breakEv(BlockBreakEvent e) {
        Block block = e.getBlock();
        Player p = e.getPlayer();

        if(block.getType().equals(Material.RESPAWN_ANCHOR)) {
            Location location = block.getLocation();

            /**
             * If the anchor isn't registered, exits.
             */
            if (!StorageManager.anchorIsRegistered(plugin, location)) return;

            /**
             * Check if the player is in his faction.
             * (FactionsX soft-depend)
             */
            if(!Utils.isPlayerInHisFaction(block, p) && !Utils.isBlockInWilderness(block) && !p.isOp() && Global.getFactionsX() == FactionsX.ACTIVE) {
                e.setCancelled(true);
                return;
            }

            /**
             * Checks if is the owner
             * (FactionsX soft-depend)
             */
            if(Utils.isAFactionMember(plugin, p, block) && !plugin.getConfig().getBoolean("can-faction-members-destroy-members-anchors")) {
                e.setCancelled(true);
                p.sendMessage(Utils.Color(plugin.getConfig().getString("cannot-break-members-anchors")));
                return;
            }

            /**
             * Don't drop the item when broken to spawn a custom one.
             */
            e.setDropItems(false);

            /**
             * Delays the task to check if the block hasn't been removed by other plugin.
             * AKA: Protection plugin.
             */
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {

                /**
                 * Checks if there isn't an anchor in that place.
                 */
                if(!location.getBlock().getType().equals(Material.RESPAWN_ANCHOR)) {

                    /**
                     * Playing sound on anchor break.
                     */
                    p.playSound(p.getLocation(), Sound.BLOCK_BEACON_DEACTIVATE, 1f, 1f);

                    /**
                     * Generate particles around on break.
                     */
                    for(int i = 0; i < 360; i += 3) {
                        Location flameloc = new Location(e.getBlock().getLocation().getWorld(), e.getBlock().getLocation().getX(), e.getBlock().getLocation().getY(), e.getBlock().getLocation().getZ());
                        p.getWorld().spawnParticle(Particle.FLAME, new Location(p.getWorld(), flameloc.getX() + Math.sin(i) * 2, e.getBlock().getLocation().getY(), flameloc.getZ() + Math.cos(i) * 2),10);
                    }

                    /**
                     * Announcing to the user that the anchor has been removed
                     */
                    Utils.Color(plugin.getConfig().getStringList("anchor-break")).forEach((str) -> {
                        p.sendMessage(str.replaceAll("%coordsX%", String.valueOf(location.getX())).
                                replaceAll("%coordsY%", String.valueOf(location.getY())).
                                replaceAll("%coordsZ%", String.valueOf(location.getZ())).
                                replaceAll("%level%", String.valueOf(StorageManager.getAnchorLevel(plugin, location))));
                    });

                    HashMap<Integer, ItemStack> inv = p.getInventory().addItem(Utils.getAnchor(StorageManager.getAnchorLevel(plugin, location), 1));
                    /**
                     * If the inventory is full it drops the anchor.
                     */
                    if(!inv.isEmpty()) {
                        p.getWorld().dropItem(location, Utils.getAnchor(StorageManager.getAnchorLevel(plugin, location), 1)).setInvulnerable(true);
                    }

                    /**
                     * Saves to the database the broken anchor.
                     */
                    StorageManager.anchorBreak(plugin, location);
                }

            }, 20L);
        }
    }

    private AnchorSell plugin;
}
