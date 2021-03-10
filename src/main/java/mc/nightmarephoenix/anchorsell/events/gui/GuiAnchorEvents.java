package mc.nightmarephoenix.anchorsell.events.gui;

import mc.nightmarephoenix.anchorsell.AnchorSell;
import mc.nightmarephoenix.anchorsell.economy.EconomyManager;
import mc.nightmarephoenix.anchorsell.inventories.AnchorScreen;
import mc.nightmarephoenix.anchorsell.inventories.BuyScreen;
import mc.nightmarephoenix.anchorsell.inventories.ConfirmScreen;
import mc.nightmarephoenix.anchorsell.inventories.UpgradesScreen;
import mc.nightmarephoenix.anchorsell.storage.StorageManager;
import mc.nightmarephoenix.anchorsell.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.RespawnAnchor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

public class GuiAnchorEvents implements Listener {

    public GuiAnchorEvents(AnchorSell plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e == null) {
            return;
        }

        // ACA ESTÁ EL PROBLEMA, HAY QUE CONSEGUIR LAS COORDS EXACTAS SIN USAR ESTE MÉTODO QUE NO SIRVE
        Location location = new Location(e.getWhoClicked().getLocation().getWorld(), Math.round(e.getWhoClicked().getLocation().getX()), e.getWhoClicked().getLocation().getY() - 1, Math.round(e.getWhoClicked().getLocation().getZ()));
        Player p = (Player) e.getWhoClicked();

        p.sendMessage(location.toString());
        /////////////////////////////////////////////////////////////////////////////////////////////////

        // Main anchor screen
        if (e.getClickedInventory() != null && e.getClickedInventory().getHolder() instanceof AnchorScreen) {
            e.setCancelled(true);
            if ((e.getCurrentItem() != null) && (e.getSlot() == 15)) { // checks if the slot is the upgrade slot
                int level = StorageManager.getAnchorLevel(plugin, new Location(p.getWorld(), location.getX(), location.getY(), location.getZ()));
                if(level >= 64)
                    return;
                p.openInventory(new UpgradesScreen(plugin, level, location).getInventory());
            }
        }

        // Upgrades screen
        if (e.getClickedInventory() != null && e.getClickedInventory().getHolder() instanceof UpgradesScreen) {
            e.setCancelled(true);
            if ((e.getCurrentItem() != null) && (e.getCurrentItem().getType() == Material.LIME_STAINED_GLASS_PANE)) { // checks if the slot is the upgrade slot
                int level = StorageManager.getAnchorLevel(plugin, location);
                if(EconomyManager.withdrawFromUser(plugin, p, Utils.getMoneyToUpgrade(level))) {

                    StorageManager.upgradeAnchor(plugin, location, p);  // saves the upgrade to the configs

                    if(Utils.getAnchorOreLevel(level) != Utils.getAnchorOreLevel(level + 1)) {
                        Block b = location.getBlock();
                        RespawnAnchor anchor = (RespawnAnchor) b.getBlockData();
                        anchor.setCharges(anchor.getCharges() + 1);
                        b.setBlockData(anchor);
                    }

                    plugin.getConfig().getStringList("anchor.upgrade-menu.upgrade-success").forEach((str) -> {
                        p.sendMessage(Utils.Color(str.replaceAll("%previusLevel%", "&r(" + Utils.getAnchorOreLevelString(plugin, level) + "&r) " + level).
                                replaceAll("%currentLevel%", "&r(" + Utils.getAnchorOreLevelString(plugin, level + 1) + "&r) " + (level + 1))));
                    });

                    p.openInventory(new UpgradesScreen(plugin, level + 1, location).getInventory());
                } else {
                    plugin.getConfig().getStringList("anchor.upgrade-menu.upgrade-fail").forEach((str) -> {
                        p.sendMessage(Utils.Color(str));
                    });
                }
            } else if ((e.getCurrentItem() != null) && (e.getCurrentItem().getType() == Material.BARRIER)) {
                p.openInventory(new AnchorScreen(p, plugin).getInventory());
            }
        }

        // Buy screen
        if (e.getClickedInventory() != null && e.getClickedInventory().getHolder() instanceof BuyScreen) {
            e.setCancelled(true);
            if ((e.getCurrentItem() != null) && (e.getCurrentItem().getType() == Material.RESPAWN_ANCHOR))
                p.openInventory(new ConfirmScreen(plugin).getInventory());
        }

        // Confirm Screen
        if (e.getClickedInventory() != null && e.getClickedInventory().getHolder() instanceof ConfirmScreen) {
            e.setCancelled(true);

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