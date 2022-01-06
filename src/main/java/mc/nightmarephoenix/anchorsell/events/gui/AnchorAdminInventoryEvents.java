package mc.nightmarephoenix.anchorsell.events.gui;

import mc.nightmarephoenix.anchorsell.AnchorSell;
import mc.nightmarephoenix.anchorsell.api.StorageManager;
import mc.nightmarephoenix.anchorsell.inventories.AnchorAdmin;
import mc.nightmarephoenix.anchorsell.inventories.ChangeLevelScreen;
import mc.nightmarephoenix.anchorsell.utils.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class AnchorAdminInventoryEvents implements Listener {

    public AnchorAdminInventoryEvents(AnchorSell plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if(e == null) return;

        Set<Material> invisibleBlocksSet = new HashSet<>();
        invisibleBlocksSet.add(Material.AIR);
        invisibleBlocksSet.add(Material.WATER);
        invisibleBlocksSet.add(Material.LAVA);

        Player p = (Player) e.getWhoClicked();
        Block block = p.getTargetBlock(invisibleBlocksSet, 5);
        Location location = block.getLocation();

        //
        // Checks if the player is in direct contact with the anchor
        //
        if (block.getType() != Material.RESPAWN_ANCHOR) {
            try {
                if(Objects.requireNonNull(Objects.requireNonNull(e.getCurrentItem()).getItemMeta()).getDisplayName().equals(Utils.Color(Objects.requireNonNull(plugin.getConfig().getString("anchor.upgrades.txt"))))) {
                    p.sendMessage(Utils.Color(Objects.requireNonNull(plugin.getConfig().getString("anchor.cantaccess"))));
                    p.closeInventory();
                    return;
                }
            } catch(Exception ignored) {}
        }

        if(e.getClickedInventory() != null && e.getClickedInventory().getHolder() instanceof AnchorAdmin) {
            e.setCancelled(true);

            // Change level
            if(e.getCurrentItem().getType().equals(Material.GLOWSTONE)) {
                p.openInventory(new ChangeLevelScreen(StorageManager.getAnchorLevel(location)).getInventory());
            }

        }

    }

    private AnchorSell plugin;

}
