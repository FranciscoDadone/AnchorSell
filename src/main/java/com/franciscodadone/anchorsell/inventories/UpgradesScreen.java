package com.franciscodadone.anchorsell.inventories;

import com.franciscodadone.anchorsell.AnchorSell;
import com.franciscodadone.anchorsell.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Objects;

public class UpgradesScreen implements InventoryHolder {
    public UpgradesScreen(AnchorSell plugin, int level, Location location) {
        this.plugin = plugin;
        this.location = location;
        this.level = level;

        inv = Bukkit.createInventory(this, 27, Utils.Color(Objects.requireNonNull(plugin.getConfig().getString("anchor.upgrade-menu.title"))));
        material = Utils.getAnchorOreLevel(level);
        init();
    }

    private void init() {
        ItemStack border = Utils.createItem(" ", Material.GRAY_STAINED_GLASS_PANE, Collections.emptyList(), false);
        // Top
        for( int i = 0; i < 9; i++ ) {
            inv.setItem(i, border);
        }
        // Left - Right
        inv.setItem(9, border);
        inv.setItem(17, border);
        // Bottom
        for( int i = 19; i < 27; i++ ) {
            inv.setItem(i, border);
        }

        for(int i = 10; i < 17; i++) {
            ItemStack item;
            if (i == 13) {
                // Current state
                item = Utils.createItem(
                        Utils.Color(Objects.requireNonNull(plugin.getConfig().getString("anchor.upgrade-menu.current-level.txt")).replaceAll("%currentLevel%", Utils.getAnchorOreLevelString(level) + " &f(" + level + ")")),
                        material,
                        Utils.getLore("anchor.upgrade-menu.current-level.lore", location),
                        true
                );
            } else {
                item = Utils.createItem(
                        Utils.Color(Objects.requireNonNull(plugin.getConfig().getString("anchor.upgrade-menu.upgrade-button.txt"))),
                        Material.LIME_STAINED_GLASS_PANE,
                        Utils.getLore("anchor.upgrade-menu.upgrade-button.lore",
                                null,
                                null
                        ),
                        true
                );
            }
            inv.setItem(i, item);
        }
        // Back button
        inv.setItem(18, Utils.createItem(
                Utils.Color(Objects.requireNonNull(plugin.getConfig().getString("anchor.upgrade-menu.back"))),
                Material.BARRIER,
                Collections.emptyList(),
                false
                )
        );
    }


    @Override
    public @NotNull Inventory getInventory() {
        return inv;
    }

    private final AnchorSell plugin;
    private final Inventory inv;
    private final Material material;
    private final int level;
    private final Location location;
}
