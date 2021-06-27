package mc.nightmarephoenix.anchorsell.events;

import mc.nightmarephoenix.anchorsell.AnchorSell;
import mc.nightmarephoenix.anchorsell.hooks.FactionsX;
import mc.nightmarephoenix.anchorsell.hooks.Global;
import mc.nightmarephoenix.anchorsell.storage.StorageManager;
import mc.nightmarephoenix.anchorsell.utils.Utils;
import mc.nightmarephoenix.anchorsell.worldguard.RegionManager;
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

        if(e.getBlock().getType().equals(Material.RESPAWN_ANCHOR)) {
            Block block = e.getBlock();
            World anchorInWorld = block.getWorld();
            Player p = e.getPlayer();
            Location loc = new Location(anchorInWorld, block.getX(), block.getY(), block.getZ());
            int currentAnchorLevel = -1;


            boolean creativeAnchor = false;
            boolean isInWorld = false;

            /**
             * Anchors can only be placed in faction territory
             */
            if(!Utils.isPlayerInHisFaction(block, e.getPlayer()) && plugin.getConfig().getBoolean("anchor.onlyPlaceInFactionTerritory") && block.getType() == Material.RESPAWN_ANCHOR && Global.getFactionsX() == FactionsX.ACTIVE && !e.getPlayer().isOp()) {
                e.getPlayer().sendMessage(Utils.Color(plugin.getConfig().getString("anchor.notInFaction")));
                e.setCancelled(true);
                return;
            }

            /**
             * World Guard zone check
             */
            if((!new RegionManager().canBuild(e.getBlock().getLocation())) && (e.getBlock().getType() == Material.RESPAWN_ANCHOR)) {// if it is in a protected land, the user can't place the anchor
                if(!e.getPlayer().isOp()) { // si no es op
                    return;
                }
            }

            /**
             * Searches if the block is in the enable-in-worlds.
             */
            for (String world : plugin.getConfig().getStringList("enable-in-worlds")) {
                if (anchorInWorld.getName().equalsIgnoreCase(world)) {
                    isInWorld = true;
                    break;
                }
            }


            /**
             * Getting the current anchor level before placing the block
             * If it makes an exception it is a creative anchor.
             */
            try {
                currentAnchorLevel = Integer.parseInt(
                        e.getItemInHand().
                                getItemMeta().
                                getLore().
                                get(2).
                                substring(18)
                );
            } catch (NullPointerException ex) {
                /**
                 * Creative respawn anchor won't be usable.
                 */
                creativeAnchor = true;
            }
            if(creativeAnchor) return;

            /**
             * Normalizes the level
             */
            if (currentAnchorLevel == 0) {
                currentAnchorLevel = 1;
            } else if(currentAnchorLevel > 64) {
                currentAnchorLevel = 64;
            }

            /**
             * If the anchor is usable and is not in enable-in-worlds
             * it cancels the placement and exits.
             */
            if (!isInWorld && e.getBlock().getType().equals(Material.RESPAWN_ANCHOR)) {
                e.setCancelled(true);
                return;
            }


            /**
             * Searches the area (safe-anchor-area) and sees if the anchor can be placed.
             */
            if(analyzeLocation(
                    new Location(
                            block.getWorld(),
                            block.getX() - plugin.getConfig().getInt("safe-anchor-area"),
                            block.getY() - plugin.getConfig().getInt("safe-anchor-area"),
                            block.getZ() - plugin.getConfig().getInt("safe-anchor-area")),
                    loc,
                    plugin.getConfig().getInt("safe-anchor-area")
            )) {

                /**
                 * Checks if the user can place more anchors
                 * (if it has a limit)
                 */
                if(!StorageManager.userCanPlaceMoreAnchors(plugin, p)) {
                    e.setCancelled(true);
                    p.sendMessage(Utils.Color(plugin.getConfig().getString("cannot-place-more-anchors").replaceAll("%quantity%", String.valueOf(plugin.getConfig().getInt("total-anchors-user-can-have")))));
                    return;
                }

                /**
                 * Waits 1sec to check if the block is actually placed and not removed by a protection plugin.
                 */
                int finalCurrentAnchorLevel = currentAnchorLevel;
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    if(block.getType().equals(Material.RESPAWN_ANCHOR)) {
                        if(!StorageManager.anchorPlace(plugin, e, p, loc, finalCurrentAnchorLevel)) return;

                        /**
                         * Plays the sound on placement.
                         */
                        p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1f, 1f);

                        /**
                         * Generate particles around on placement.
                         */
                        for(int i = 0; i < 360; i += 5) {
                            Location flameloc = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ());
                            flameloc.setZ(flameloc.getZ() + Math.cos(i) * 5);
                            flameloc.setX(flameloc.getX() + Math.sin(i) * 5);
                            loc.getWorld().playEffect(flameloc, Effect.POTION_BREAK, 51);
                        }

                        /**
                         * Determines witch level of glowstone the anchor needs
                         */
                        Material i = Utils.getAnchorOreLevel(StorageManager.getAnchorLevel(plugin, loc));
                        int charges = 0;
                        if(i == Material.IRON_INGOT)           charges = 1;
                        else if(i == Material.GOLD_INGOT)      charges = 2;
                        else if(i == Material.DIAMOND)         charges = 3;
                        else if(i == Material.NETHERITE_INGOT) charges = 4;

                        Block b = loc.getBlock();
                        RespawnAnchor anchor = (RespawnAnchor) b.getBlockData();
                        anchor.setCharges(charges);
                        b.setBlockData(anchor);
                    }
                }, 20L);

            } else {
                /**
                 * Throws a radius error.
                 */
                p.sendMessage(Utils.Color(plugin.getConfig().getString("radius-error")));
                e.setCancelled(true);
            }
        }
    }

    /**
     * Method to check if there are more anchors in the area.
     *
     * @param startOfTheBox
     * @param anchor
     * @param radius
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

    private AnchorSell plugin;
}
