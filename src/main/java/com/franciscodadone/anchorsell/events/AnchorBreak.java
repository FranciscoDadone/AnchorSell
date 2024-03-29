package com.franciscodadone.anchorsell.events;

import com.franciscodadone.anchorsell.api.Global;
import com.franciscodadone.anchorsell.api.AnchorAPI;
import com.franciscodadone.anchorsell.utils.Logger;
import com.franciscodadone.anchorsell.AnchorSell;
import com.franciscodadone.anchorsell.models.Anchor;
import com.franciscodadone.anchorsell.utils.Utils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

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

            // If the anchor isn't registered, exits.
            if (!AnchorAPI.isValidAnchor(location)) return;

            Anchor anchor = AnchorAPI.getAnchorFromLoc(location);

            if(!Global.plugin.getConfig().getBoolean("break-others") && !AnchorAPI.belongsToPlayer(anchor, p)) {
                e.setCancelled(true);

                assert anchor != null;
                Utils.sendConfigMessageF(
                        "break-others-message",
                        "%player%",
                        Objects.requireNonNull(Bukkit.getPlayer(UUID.fromString(Objects.requireNonNull(anchor.getOwner().getName())))).getDisplayName(),
                        p
                );

                return;
            }

            // Don't drop the item when broken to spawn a custom one.
            e.setDropItems(false);

            //
            // Delays the task to check if the block hasn't been removed by other plugin.
            // AKA: Protection plugin.
            //
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {

                // Checks if there isn't an anchor in that place.
                if(!location.getBlock().getType().equals(Material.RESPAWN_ANCHOR)) {

                    // Log to console the anchor break
                    Logger.info(p.getName() + " broke a level " + AnchorAPI.getAnchorLevel(location) + " Anchor. " +
                            "(" +
                                    location.getX() + ", " +
                                    location.getY() + ", " +
                                    location.getZ() +
                            ")");

                    // Playing sound on anchor break.
                    p.playSound(p.getLocation(), Sound.BLOCK_BEACON_DEACTIVATE, 1f, 1f);

                    // Generate particles around on break.
                    for(int i = 0; i < 360; i += 3) {
                        Location flameloc = new Location(e.getBlock().getLocation().getWorld(), e.getBlock().getLocation().getX(), e.getBlock().getLocation().getY(), e.getBlock().getLocation().getZ());
                        p.getWorld().spawnParticle(Particle.FLAME, new Location(p.getWorld(), flameloc.getX() + Math.sin(i) * 2, e.getBlock().getLocation().getY(), flameloc.getZ() + Math.cos(i) * 2),10);
                    }

                    // Announcing to the user that the anchor has been removed
                    Utils.Color(plugin.getConfig().getStringList("anchor-break")).forEach((str) -> p.sendMessage(str.replaceAll("%coordsX%", String.valueOf(location.getX())).
                            replaceAll("%coordsY%", String.valueOf(location.getY())).
                            replaceAll("%coordsZ%", String.valueOf(location.getZ())).
                            replaceAll("%level%", String.valueOf(AnchorAPI.getAnchorLevel(location)))));

                    HashMap<Integer, ItemStack> inv = p.getInventory().addItem(Utils.getAnchor(AnchorAPI.getAnchorLevel(location), 1));

                    // If the inventory is full it drops the anchor.
                    if(!inv.isEmpty()) {
                        p.getWorld().dropItem(location, Utils.getAnchor(AnchorAPI.getAnchorLevel(location), 1)).setInvulnerable(true);
                    }

                    // Saves to the database the broken anchor.
                    AnchorAPI.removeAnchor(Objects.requireNonNull(AnchorAPI.getAnchorFromLoc(location)));

                    // Removes anchor from cache
                    Global.removeAnchor(anchor);
                }
            }, 20L);
        }
    }

    private final AnchorSell plugin;
}
