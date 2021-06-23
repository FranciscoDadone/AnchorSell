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

        if(block.getType() == Material.RESPAWN_ANCHOR) {
            Location location = block.getLocation();

            // Si no esta registrado el anchor no hace nada
            if (!StorageManager.isARegisterAnchor(plugin, location)) {
                System.out.println("error 1");
                return;
            }

            // Check if the player is in his faction and let it break the anchor
            if(!Utils.isPlayerInHisFaction(block, p) && !Utils.isBlockInWilderness(block) && !p.isOp() && Global.getFactionsX() == FactionsX.ACTIVE) {
                System.out.println("error 2");
                e.setCancelled(true);
                return;
            }

            // Checks if is a faction member
            if(Utils.isAFactionMember(plugin, p, block) && !plugin.getConfig().getBoolean("can-faction-members-destroy-members-anchors")) {
                e.setCancelled(true);
                p.sendMessage(Utils.Color(plugin.getConfig().getString("cannot-break-members-anchors")));
                return;
            }

            // Playing sound on anchor break
            p.playSound(p.getLocation(), Sound.BLOCK_BEACON_DEACTIVATE, 1f, 1f);

            // generate particles around on place
            for(int i = 0; i < 360; i += 3) {
                Location flameloc = new Location(e.getBlock().getLocation().getWorld(), e.getBlock().getLocation().getX(), e.getBlock().getLocation().getY(), e.getBlock().getLocation().getZ());
                p.getWorld().spawnParticle(Particle.FLAME, new Location(p.getWorld(), flameloc.getX() + Math.sin(i) * 2, e.getBlock().getLocation().getY(), flameloc.getZ() + Math.cos(i) * 2),10);
            }

            // Announcing to the user that the anchor has been removed
            Utils.Color(plugin.getConfig().getStringList("anchor-break")).forEach((str) -> {
                p.sendMessage(str.replaceAll("%coordsX%", String.valueOf(location.getX())).
                        replaceAll("%coordsY%", String.valueOf(location.getY())).
                        replaceAll("%coordsZ%", String.valueOf(location.getZ())).
                        replaceAll("%level%", String.valueOf(StorageManager.getAnchorLevel(plugin, location))));
            });

            e.setDropItems(false);
            HashMap<Integer, ItemStack> inv = p.getInventory().addItem(Utils.getAnchor(StorageManager.getAnchorLevel(plugin, location), 1));
            if(!inv.isEmpty()) {
                p.getWorld().dropItem(location, Utils.getAnchor(StorageManager.getAnchorLevel(plugin, location), 1)).setInvulnerable(true);
            }
            StorageManager.anchorBreak(plugin, location);
        }
    }

    private AnchorSell plugin;
}
