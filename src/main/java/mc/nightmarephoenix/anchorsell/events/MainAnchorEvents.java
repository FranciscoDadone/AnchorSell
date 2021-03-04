package mc.nightmarephoenix.anchorsell.events;

import mc.nightmarephoenix.anchorsell.inventories.AnchorScreen;
import mc.nightmarephoenix.anchorsell.inventories.UpgradesScreen;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MainAnchorEvents implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        // Main anchor screen
        if( (e.getClickedInventory().getHolder() instanceof AnchorScreen) && (e.getClickedInventory() != null) ) {
            e.setCancelled(true);

            Player p = (Player) e.getWhoClicked();
            if( (e.getCurrentItem() != null) && (e.getSlot() == 15) )  { // checks if the slot is the upgrade slot
                p.openInventory(new UpgradesScreen(1).getInventory());
            }
        }

        // Upgrades screen
        if( (e.getClickedInventory().getHolder() instanceof UpgradesScreen) && (e.getClickedInventory() != null) ) {
            e.setCancelled(true);

            Player p = (Player) e.getWhoClicked();
            if( (e.getCurrentItem() != null) && (e.getCurrentItem().getType() == Material.LIME_STAINED_GLASS_PANE) )  { // checks if the slot is the upgrade slot
                p.sendMessage("Upgrade");
            }
        }
    }
}
