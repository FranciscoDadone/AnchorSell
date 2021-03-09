package mc.nightmarephoenix.anchorsell.events.gui;

import mc.nightmarephoenix.anchorsell.AnchorSell;
import mc.nightmarephoenix.anchorsell.economy.EconomyManager;
import mc.nightmarephoenix.anchorsell.inventories.AnchorScreen;
import mc.nightmarephoenix.anchorsell.inventories.BuyScreen;
import mc.nightmarephoenix.anchorsell.inventories.ConfirmScreen;
import mc.nightmarephoenix.anchorsell.inventories.UpgradesScreen;
import mc.nightmarephoenix.anchorsell.storage.StorageManager;
import mc.nightmarephoenix.anchorsell.utils.Utils;
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
        if (e == null) {
            return;
        }

        // Main anchor screen
        if (e.getClickedInventory() != null && e.getClickedInventory().getHolder() instanceof AnchorScreen) {
            e.setCancelled(true);
            Player p = (Player) e.getWhoClicked();
            if ((e.getCurrentItem() != null) && (e.getSlot() == 15)) { // checks if the slot is the upgrade slot
                int level = StorageManager.getAnchorLevel(plugin, new Location(AnchorScreen.location.getWorld(), AnchorScreen.location.getBlockX(), AnchorScreen.location.getBlockY(), AnchorScreen.location.getBlockZ()));
                if(level >= 64)
                    return;
                p.openInventory(new UpgradesScreen(plugin, level).getInventory());
            }
        }

        // Upgrades screen
        if (e.getClickedInventory() != null && e.getClickedInventory().getHolder() instanceof UpgradesScreen) {
            e.setCancelled(true);
            Player p = (Player) e.getWhoClicked();
            Location location = new Location(AnchorScreen.location.getWorld(), AnchorScreen.location.getBlockX(), AnchorScreen.location.getBlockY(), AnchorScreen.location.getBlockZ());
            if ((e.getCurrentItem() != null) && (e.getCurrentItem().getType() == Material.LIME_STAINED_GLASS_PANE)) { // checks if the slot is the upgrade slot
                int level = StorageManager.getAnchorLevel(plugin, location);
                if(EconomyManager.withdrawFromUser(plugin, p, Utils.getMoneyToUpgrade(level))) {

                    StorageManager.upgradeAnchor(plugin, location, p);

                    plugin.getConfig().getStringList("anchor.upgrade-menu.upgrade-success").forEach((str) -> {
                        p.sendMessage(Utils.Color(str.replaceAll("%previusLevel%", "&r(" + Utils.getAnchorOreLevelString(plugin, level) + "&r) " + level).
                                replaceAll("%currentLevel%", "&r(" + Utils.getAnchorOreLevelString(plugin, level + 1) + "&r) " + (level + 1))));
                    });
                } else {
                    plugin.getConfig().getStringList("anchor.upgrade-menu.upgrade-fail").forEach((str) -> {
                        p.sendMessage(Utils.Color(str));
                    });
                }
            } else if ((e.getCurrentItem() != null) && (e.getCurrentItem().getType() == Material.BARRIER)) {
                p.openInventory(new AnchorScreen(p, plugin, AnchorScreen.location).getInventory());
            }
        }

        // Buy screen
        if (e.getClickedInventory() != null && e.getClickedInventory().getHolder() instanceof BuyScreen) {
            e.setCancelled(true);
            Player p = (Player) e.getWhoClicked();
            if ((e.getCurrentItem() != null) && (e.getCurrentItem().getType() == Material.RESPAWN_ANCHOR))
                p.openInventory(new ConfirmScreen(plugin).getInventory());
        }

        // Confirm Screen
        if (e.getClickedInventory() != null && e.getClickedInventory().getHolder() instanceof ConfirmScreen) {
            e.setCancelled(true);

            Player p = (Player) e.getWhoClicked();
            if ((e.getCurrentItem() != null) && (e.getCurrentItem().getType() == Material.GREEN_STAINED_GLASS_PANE)) {
                int anchorValue = plugin.getConfig().getInt("anchor-value");
                if(EconomyManager.withdrawFromUser(plugin, p, anchorValue)) {
                    p.getInventory().addItem(Utils.getAnchor(1, 1));
                    p.sendMessage(Utils.Color(plugin.getConfig().getString("confirmscreen.you-have-an-anchor")));
                } else {
                    p.sendMessage(Utils.Color(plugin.getConfig().getString("confirmscreen.you-cant-afford")));
                }
                p.closeInventory();
            } else if ((e.getCurrentItem() != null) && (e.getCurrentItem().getType() == Material.RED_STAINED_GLASS_PANE)) {
                p.closeInventory();
            }
        }
    }

    private AnchorSell plugin;
}