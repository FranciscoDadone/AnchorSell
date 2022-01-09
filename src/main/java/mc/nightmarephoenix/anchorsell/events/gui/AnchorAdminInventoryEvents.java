package mc.nightmarephoenix.anchorsell.events.gui;

import mc.nightmarephoenix.anchorsell.AnchorSell;
import mc.nightmarephoenix.anchorsell.api.Global;
import mc.nightmarephoenix.anchorsell.api.StorageManager;
import mc.nightmarephoenix.anchorsell.inventories.AnchorAdmin;
import mc.nightmarephoenix.anchorsell.inventories.ChangeLevelScreen;
import mc.nightmarephoenix.anchorsell.inventories.ConfirmScreen;
import mc.nightmarephoenix.anchorsell.models.Anchor;
import mc.nightmarephoenix.anchorsell.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import java.util.Objects;
import java.util.logging.Level;

public class AnchorAdminInventoryEvents implements Listener {

    public AnchorAdminInventoryEvents(AnchorSell plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    @SuppressWarnings("all")
    public void onClick(InventoryClickEvent e) {
        if(e == null) return;

        Player p = (Player) e.getWhoClicked();
        Block block = p.getTargetBlock(Utils.getInvisibleBlockList(), 5);
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
            if(Objects.requireNonNull(e.getCurrentItem()).getType().equals(Material.GLOWSTONE)) {
                p.openInventory(new ChangeLevelScreen(StorageManager.getAnchorLevel(location)).getInventory());
            }

            // Remove anchor
            if(e.getCurrentItem().getType().equals(Material.BARRIER)) {
                p.openInventory(new ConfirmScreen(
                        "Remove this anchor?",
                        Utils.createItem(Utils.Color("&c&lRemove?"), Material.BARRIER,true)
                        ).getInventory()
                );
            }
        } else if (e.getClickedInventory() != null && e.getClickedInventory().getHolder() instanceof ConfirmScreen && Objects.requireNonNull(e.getClickedInventory().getItem(13)).getType().equals(Material.BARRIER)) {
            e.setCancelled(true);

            if((e.getCurrentItem() != null) && (e.getCurrentItem().getType().equals(Material.GREEN_STAINED_GLASS_PANE))) {
                Anchor anchor = StorageManager.getAnchorFromLoc(location);
                assert anchor != null;
                StorageManager.removeAnchor(anchor);
                Global.removeAnchor(anchor);
                block.setType(Material.AIR);
                Bukkit.getLogger().log(Level.INFO, "[AnchorSell] " + p.getPlayerListName() + " (Admin) removed an anchor " + anchor.getLocation());
                p.closeInventory();
            } else if((e.getCurrentItem() != null) && (e.getCurrentItem().getType().equals(Material.RED_STAINED_GLASS_PANE))) {
                p.closeInventory();
            }
        }
    }

    private final AnchorSell plugin;

}
