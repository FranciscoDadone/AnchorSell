package mc.nightmarephoenix.anchorsell.events.gui;

import mc.nightmarephoenix.anchorsell.AnchorSell;
import mc.nightmarephoenix.anchorsell.inventories.AnchorScreen;
import mc.nightmarephoenix.anchorsell.inventories.UpgradesScreen;
import mc.nightmarephoenix.anchorsell.storage.StorageManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GuiAnchorEvents implements Listener {

    public GuiAnchorEvents(AnchorSell plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        // Main anchor screen
        if ((e.getClickedInventory().getHolder() instanceof AnchorScreen) && (e.getClickedInventory() != null)) {
            e.setCancelled(true);

            Player p = (Player) e.getWhoClicked();
            if ((e.getCurrentItem() != null) && (e.getSlot() == 15)) { // checks if the slot is the upgrade slot
                p.openInventory(new UpgradesScreen(plugin, StorageManager.getAnchorLevel(new Location(AnchorScreen.location.getWorld(), AnchorScreen.location.getBlockX(), AnchorScreen.location.getBlockY(), AnchorScreen.location.getBlockZ()))).getInventory());
            }
        }

        // Upgrades screen
        if ((e.getClickedInventory().getHolder() instanceof UpgradesScreen) && (e.getClickedInventory() != null)) {
            e.setCancelled(true);

            Player p = (Player) e.getWhoClicked();
            if ((e.getCurrentItem() != null) && (e.getCurrentItem().getType() == Material.LIME_STAINED_GLASS_PANE)) { // checks if the slot is the upgrade slot
                p.sendMessage("Upgrade");
            } else if ((e.getCurrentItem() != null) && (e.getCurrentItem().getType() == Material.BARRIER)) {
                p.openInventory(new AnchorScreen(p, plugin, AnchorScreen.location).getInventory());
            }
        }

        // Buy screen
    }

    private AnchorSell plugin;
}
