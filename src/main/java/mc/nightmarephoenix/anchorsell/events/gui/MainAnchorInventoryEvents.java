package mc.nightmarephoenix.anchorsell.events.gui;

import mc.nightmarephoenix.anchorsell.AnchorSell;
import mc.nightmarephoenix.anchorsell.thirdparty.vault.EconomyManager;
import mc.nightmarephoenix.anchorsell.inventories.AnchorScreen;
import mc.nightmarephoenix.anchorsell.inventories.BuyScreen;
import mc.nightmarephoenix.anchorsell.inventories.ConfirmScreen;
import mc.nightmarephoenix.anchorsell.inventories.UpgradesScreen;
import mc.nightmarephoenix.anchorsell.api.StorageManager;
import mc.nightmarephoenix.anchorsell.utils.Logger;
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

import java.util.*;

public class MainAnchorInventoryEvents implements Listener {

    public MainAnchorInventoryEvents(AnchorSell plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e == null) return;

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


        //
        //Opens the anchor main screen (inventory)
        //
        if (e.getClickedInventory() != null && e.getClickedInventory().getHolder() instanceof AnchorScreen) {
            e.setCancelled(true);
            if ((e.getCurrentItem() != null) && (e.getSlot() == 15)) { // checks if the slot is the upgrade slot
                int level = StorageManager.getAnchorLevel(new Location(p.getWorld(), location.getX(), location.getY(), location.getZ()));
                if(level >= 64)
                    return;
                p.openInventory(new UpgradesScreen(plugin, level, location).getInventory());
            }
        }

        //
        // Opens the anchor upgrades screen (inventory)
        //
        if (e.getClickedInventory() != null && e.getClickedInventory().getHolder() instanceof UpgradesScreen) {
            e.setCancelled(true);
            // If it hits the upgrade button.
            if ((e.getCurrentItem() != null) && (e.getCurrentItem().getType().equals(Material.LIME_STAINED_GLASS_PANE))) { // checks if the slot is the upgrade slot
                int level = StorageManager.getAnchorLevel(location);
                if(level >= 64) {
                    p.closeInventory();
                    return;
                }

                if(EconomyManager.withdrawFromUser(p, Utils.getMoneyToUpgrade(level))) {
                    // Log to console the anchor upgrade event
                    Logger.info(p.getName() + " upgraded an anchor. (" +
                            location.getX() + ", " +
                            location.getY() + ", " +
                            location.getZ() + ") (" +
                                    (level) + " -> " + (level + 1) + ")"
                            );

                    // Saves the upgrade to config.
                    StorageManager.upgradeAnchor(location, p);

                    // Upgrades the anchor aesthetics if it needs.
                    if(Utils.getAnchorOreLevel(level) != Utils.getAnchorOreLevel(level + 1)) {
                        Block b = location.getBlock();
                        RespawnAnchor anchor = (RespawnAnchor) b.getBlockData();
                        anchor.setCharges(anchor.getCharges() + 1);
                        b.setBlockData(anchor);
                    }

                    // Prints to user the upgrade message.
                    plugin.getConfig().getStringList("anchor.upgrade-menu.upgrade-success").forEach((str) -> p.sendMessage(Utils.Color(str.replaceAll("%previusLevel%", "&r(" + Utils.getAnchorOreLevelString(level) + "&r) " + level).
                            replaceAll("%currentLevel%", "&r(" + Utils.getAnchorOreLevelString(level + 1) + "&r) " + (level + 1)))));

                    // Updates the menu.
                    p.openInventory(new UpgradesScreen(plugin, level + 1, location).getInventory());
                } else {
                    // Prints to the user the fail message.
                    plugin.getConfig().getStringList("anchor.upgrade-menu.upgrade-fail").forEach((str) -> p.sendMessage(Utils.Color(str)));
                }

               // "Go back" button.
            } else if ((e.getCurrentItem() != null) && (e.getCurrentItem().getType() == Material.BARRIER)) {
                p.openInventory(new AnchorScreen(p, location).getInventory());
            }
        }

        // Opens the anchor buy screen (inventory)
        if (e.getClickedInventory() != null && e.getClickedInventory().getHolder() instanceof BuyScreen) {
            e.setCancelled(true);
            if ((e.getCurrentItem() != null) && (e.getCurrentItem().getType() == Material.RESPAWN_ANCHOR))
                p.openInventory(
                        new ConfirmScreen(
                                Objects.requireNonNull(plugin.getConfig().getString("confirmscreen.title")),
                                Utils.createItem(Utils.Color("&f$&e" + plugin.getConfig().getInt("anchor-value")), Material.RESPAWN_ANCHOR,true)).getInventory()
                );
        }

        // Opens the anchor confirm screen (inventory)
        if (e.getClickedInventory() != null && e.getClickedInventory().getHolder() instanceof ConfirmScreen && Objects.requireNonNull(e.getClickedInventory().getItem(13)).getType().equals(Material.RESPAWN_ANCHOR)) {
            e.setCancelled(true);

            if((e.getCurrentItem() != null) && (e.getCurrentItem().getType().equals(Material.GREEN_STAINED_GLASS_PANE))) {
                int anchorValue = plugin.getConfig().getInt("anchor-value");
                // Takes the money from the user.
                if(EconomyManager.withdrawFromUser(p, anchorValue)) {
                    // Log to console the anchor buy event
                    Logger.info("[AnchorSell] " +
                            p.getName() + " bought an Anchor."
                    );

                    p.getInventory().addItem(Utils.getAnchor(1, 1));
                    p.sendMessage(Utils.Color(Objects.requireNonNull(plugin.getConfig().getString("confirmscreen.you-have-an-anchor"))));
                } else {
                    p.sendMessage(Utils.Color(Objects.requireNonNull(plugin.getConfig().getString("confirmscreen.you-cant-afford"))));
                }
                p.closeInventory();
            } else if ((e.getCurrentItem() != null) && (e.getCurrentItem().getType().equals(Material.RED_STAINED_GLASS_PANE))) {
                p.closeInventory();
            }
        }
    }

    private final AnchorSell plugin;
}