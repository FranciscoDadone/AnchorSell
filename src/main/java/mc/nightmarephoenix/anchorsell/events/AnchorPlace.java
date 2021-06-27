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
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

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



            boolean creativeAnchor = false;
            boolean isInWorld = false;

            // Anchors can only be placed in faction territory
            if(!Utils.isPlayerInHisFaction(block, e.getPlayer()) && plugin.getConfig().getBoolean("anchor.onlyPlaceInFactionTerritory") && block.getType() == Material.RESPAWN_ANCHOR && Global.getFactionsX() == FactionsX.ACTIVE && !e.getPlayer().isOp()) {
                e.getPlayer().sendMessage(Utils.Color(plugin.getConfig().getString("anchor.notInFaction")));
                e.setCancelled(true);
                return;
            }

            // World Guard check
            if((!new RegionManager().canBuild(e.getBlock().getLocation())) && (e.getBlock().getType() == Material.RESPAWN_ANCHOR)) {// if it is in a protected land, the user can't place the anchor
                if(!e.getPlayer().isOp()) { // si no es op
                    return;
                }
            }

            // Busca si el bloque esta en enable-in-worlds
            for (String world : plugin.getConfig().getStringList("enable-in-worlds")) {
                if (anchorInWorld.getName().equalsIgnoreCase(world)) {
                    isInWorld = true;
                    break;
                }
            }

            // Si no esta en enable-in-worlds se sale del método
            if (!isInWorld && e.getBlock().getType() == Material.RESPAWN_ANCHOR && !creativeAnchor) {
                e.setCancelled(true);
                return;
            }

            if(creativeAnchor)  // if is a creative anchor it will not continue
                return;

            int currentAnchorLevel = -1;


            // Getting the current anchor level before placing the block
            try {
                currentAnchorLevel = Integer.parseInt(e.getItemInHand().getItemMeta().getLore().get(2).substring(18));
            } catch (NullPointerException ex) {
                // Creative respawn anchor will not work as a interactuable anchor
                creativeAnchor = true;
            }

            if (currentAnchorLevel == 0) {
                currentAnchorLevel = 1;
            } else if(currentAnchorLevel > 64) {
                currentAnchorLevel = 64;
            }

            if(analyzeLocation(new Location(block.getWorld(), block.getX() - plugin.getConfig().getInt("safe-anchor-area"), block.getY() - plugin.getConfig().getInt("safe-anchor-area"), block.getZ() - plugin.getConfig().getInt("safe-anchor-area")), loc, plugin.getConfig().getInt("safe-anchor-area"))) {

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
                        if(!StorageManager.anchorPlace(plugin, e, p, loc, finalCurrentAnchorLevel)) {
                            return;
                        }

                        if(block.getType() == Material.RESPAWN_ANCHOR) {
                            // generate sound on place
                            p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1f, 1f);

                            // generate particles around on place
                            for(int i = 0; i < 360; i += 5) {
                                Location flameloc = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ());
                                flameloc.setZ(flameloc.getZ() + Math.cos(i) * 5);
                                flameloc.setX(flameloc.getX() + Math.sin(i) * 5);
                                loc.getWorld().playEffect(flameloc, Effect.POTION_BREAK, 51);
                            }

                            // Determines witch level of glowstone the anchor needs
                            Material i = Utils.getAnchorOreLevel(StorageManager.getAnchorLevel(plugin, loc));
                            int charges = 0;
                            if(i == Material.IRON_INGOT)
                                charges = 1;
                            else if(i == Material.GOLD_INGOT)
                                charges = 2;
                            else if(i == Material.DIAMOND)
                                charges = 3;
                            else if(i == Material.NETHERITE_INGOT)
                                charges = 4;
                            Block b = loc.getBlock();
                            RespawnAnchor anchor = (RespawnAnchor) b.getBlockData();
                            anchor.setCharges(charges);
                            b.setBlockData(anchor);
                        }
                    }
                }, 20L);

            } else {
                p.sendMessage(Utils.Color(plugin.getConfig().getString("radius-error")));
                e.setCancelled(true);
            }
        }
    }

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
