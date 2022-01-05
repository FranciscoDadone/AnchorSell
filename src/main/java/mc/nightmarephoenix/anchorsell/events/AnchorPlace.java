package mc.nightmarephoenix.anchorsell.events;

import mc.nightmarephoenix.anchorsell.AnchorSell;
import mc.nightmarephoenix.anchorsell.models.Anchor;
import mc.nightmarephoenix.anchorsell.api.Global;
import mc.nightmarephoenix.anchorsell.api.StorageManager;
import mc.nightmarephoenix.anchorsell.utils.Utils;
import mc.nightmarephoenix.anchorsell.thirdparty.worldguard.RegionManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.RespawnAnchor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.Objects;

public class AnchorPlace implements Listener {

    public AnchorPlace(AnchorSell plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void place(BlockPlaceEvent e) {

        if(e.getBlock().getType().equals(Material.RESPAWN_ANCHOR)) {
            Block block = e.getBlock();
            World anchorInWorld = block.getWorld();
            Player p = e.getPlayer();
            Location loc = new Location(anchorInWorld, block.getX(), block.getY(), block.getZ());
            int currentAnchorLevel = -1;


            boolean creativeAnchor = false;
            boolean isInWorld;

            // World Guard zone check
            if((!new RegionManager().canBuild(e.getBlock().getLocation())) && (e.getBlock().getType() == Material.RESPAWN_ANCHOR)) {// if it is in a protected land, the user can't place the anchor
                if(!e.getPlayer().isOp()) { // si no es op
                    return;
                }
            }

            // Searches if the block is in the enable-in-worlds.
            isInWorld = plugin.getConfig().getStringList("enable-in-worlds").contains(anchorInWorld.getName());
            if(!isInWorld) Utils.sendConfigMessage("world-not-enabled-error", e.getPlayer());


            //
            // Getting the current anchor level before placing the block
            // If it makes an exception it is a creative anchor.
            //
            try {
                currentAnchorLevel = Integer.parseInt(
                        Objects.requireNonNull(Objects.requireNonNull(e.getItemInHand().
                                                getItemMeta()).
                                        getLore()).
                                get(2).
                                substring(18)
                );
            } catch (Exception ex) {
                // Creative respawn anchor won't be usable.
                creativeAnchor = true;
            }
            if(creativeAnchor) return;

            // Normalizes the level
            if (currentAnchorLevel == 0) {
                currentAnchorLevel = 1;
            } else if(currentAnchorLevel > 64) {
                currentAnchorLevel = 64;
            }

            //
            // If the anchor is usable and is not in enable-in-worlds
            // it cancels the placement and exits.
            //
            if (!isInWorld && e.getBlock().getType().equals(Material.RESPAWN_ANCHOR)) {
                e.setCancelled(true);
                return;
            }

            // Searches the area (safe-anchor-area) and sees if the anchor can be placed.
            if(analyzeLocation(
                    new Location(
                            block.getWorld(),
                            block.getX() - plugin.getConfig().getInt("safe-anchor-area"),
                            block.getY() - plugin.getConfig().getInt("safe-anchor-area"),
                            block.getZ() - plugin.getConfig().getInt("safe-anchor-area")),
                    loc,
                    plugin.getConfig().getInt("safe-anchor-area")
            )) {

                //
                // Checks if the user can place more anchors
                // (if it has a limit)
                //
                try {
                    if(!StorageManager.canPlaceMoreAnchors(p)) {
                        e.setCancelled(true);
                        p.sendMessage(Utils.Color(Objects.requireNonNull(plugin.getConfig().getString("cannot-place-more-anchors")).replaceAll("%quantity%", String.valueOf(plugin.getConfig().getInt("total-anchors-user-can-have")))));
                        return;
                    }
                } catch(Exception ignored) {}

                // Waits 1sec to check if the block is actually placed and not removed by a protection plugin.
                int finalCurrentAnchorLevel = currentAnchorLevel;
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    if(block.getType().equals(Material.RESPAWN_ANCHOR)) {

                        Anchor anchor = new Anchor(
                                finalCurrentAnchorLevel,
                                loc,
                                p,
                                p.getPlayerListName()
                        );

                        boolean couldPlace = StorageManager.saveAnchor(anchor);

                        if(!couldPlace) {
                            p.sendMessage(Utils.Color(Objects.requireNonNull(plugin.getConfig().getString("cannot-place-more-anchors")).replaceAll("%quantity%", String.valueOf(plugin.getConfig().getInt("total-anchors-user-can-have")))));
                            e.setCancelled(true);
                            return;
                        }

                        // Log to console the anchor place
                        Bukkit.getLogger().info("[AnchorSell] "
                                + p.getName() + " placed a level " + finalCurrentAnchorLevel + " Anchor. " +
                                "(" +
                                block.getX() + ", " +
                                block.getY() + ", " +
                                block.getZ() +
                                ")");
                        // Plays the sound on placement.
                        p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1f, 1f);

                        // Generate particles around on placement.
                        for(int i = 0; i < 360; i += 5) {
                            Location flameloc = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ());
                            flameloc.setZ(flameloc.getZ() + Math.cos(i) * 5);
                            flameloc.setX(flameloc.getX() + Math.sin(i) * 5);
                            Objects.requireNonNull(loc.getWorld()).playEffect(flameloc, Effect.POTION_BREAK, 51);
                        }

                        // Caching anchor
                        Global.addAnchor(new Anchor(
                                StorageManager.getAnchorLevel(loc),
                                loc,
                                p,
                                p.getPlayerListName()
                        ));

                        // Determines witch level of glowstone the anchor needs
                        Material i = Utils.getAnchorOreLevel(StorageManager.getAnchorLevel(loc));
                        int charges = 0;
                        if(i == Material.IRON_INGOT)           charges = 1;
                        else if(i == Material.GOLD_INGOT)      charges = 2;
                        else if(i == Material.DIAMOND)         charges = 3;
                        else if(i == Material.NETHERITE_INGOT) charges = 4;

                        Block b = loc.getBlock();
                        RespawnAnchor anchorBlock = (RespawnAnchor) b.getBlockData();
                        anchorBlock.setCharges(charges);
                        b.setBlockData(anchorBlock);

                        // Announcing to the user that the anchor has been placed
                        Utils.Color(plugin.getConfig().getStringList("anchor-place")).forEach((str) -> p.sendMessage(str.replaceAll("%coordsX%", String.valueOf(loc.getX())).
                                replaceAll("%coordsY%", String.valueOf(loc.getY())).
                                replaceAll("%coordsZ%", String.valueOf(loc.getZ())).
                                replaceAll("%level%", String.valueOf(StorageManager.getAnchorLevel(loc)))));
                    }
                }, 20L);

            } else {
                // Throws a radius error.
                p.sendMessage(Utils.Color(Objects.requireNonNull(plugin.getConfig().getString("radius-error"))));
                e.setCancelled(true);
            }
        }
    }

    /**
     * Method to check if there are more anchors in the area.
     *
     * @param startOfTheBox start of the box to search for anchors
     * @param anchor anchor location
     * @param radius radius
     * @return boolean
     */
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

    private final AnchorSell plugin;
}
