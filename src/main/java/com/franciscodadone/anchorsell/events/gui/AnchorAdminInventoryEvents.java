package com.franciscodadone.anchorsell.events.gui;

import com.franciscodadone.anchorsell.AnchorSell;
import com.franciscodadone.anchorsell.api.Global;
import com.franciscodadone.anchorsell.api.AnchorAPI;
import com.franciscodadone.anchorsell.inventories.AnchorAdmin;
import com.franciscodadone.anchorsell.inventories.ChangeLevelScreen;
import com.franciscodadone.anchorsell.inventories.ConfirmScreen;
import com.franciscodadone.anchorsell.models.Anchor;
import com.franciscodadone.anchorsell.utils.Logger;
import com.franciscodadone.anchorsell.utils.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import java.util.Objects;

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
                p.openInventory(new ChangeLevelScreen(AnchorAPI.getAnchorLevel(location)).getInventory());
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
                Anchor anchor = AnchorAPI.getAnchorFromLoc(location);
                assert anchor != null;
                AnchorAPI.removeAnchor(anchor);
                Global.removeAnchor(anchor);
                block.setType(Material.AIR);
                Logger.info(p.getPlayerListName() + " (Admin) removed an anchor " + anchor.getLocation());
                p.closeInventory();
            } else if((e.getCurrentItem() != null) && (e.getCurrentItem().getType().equals(Material.RED_STAINED_GLASS_PANE))) {
                p.closeInventory();
            }
        }
    }

    private final AnchorSell plugin;

}
