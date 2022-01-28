package com.franciscodadone.anchorsell.inventories;

import com.franciscodadone.anchorsell.api.Global;
import com.franciscodadone.anchorsell.AnchorSell;
import com.franciscodadone.anchorsell.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Objects;

public class BuyScreen implements InventoryHolder {

    private final Inventory inv;
    private final AnchorSell plugin;

    public BuyScreen() {
        this.plugin = Global.plugin;
        inv = Bukkit.createInventory(this, 27, Utils.Color(Objects.requireNonNull(plugin.getConfig().getString("anchorbuy.title"))));
        init();
    }

    private void init() {
        ItemStack border = Utils.createItem(" ", Material.PURPLE_STAINED_GLASS_PANE, Collections.emptyList(), false);

        // Top
        for( int i = 0; i < 9; i++ ) {
            inv.setItem(i, border);
        }
        // Left - Center - Right
        inv.setItem(9, border);
        inv.setItem(13, border);
        inv.setItem(17, border);

        // Bottom
        for( int i = 18; i < 27; i++ ) {
            inv.setItem(i, border);
        }

        // Info
        ItemStack info = Utils.createItem(Utils.Color(Objects.requireNonNull(plugin.getConfig().getString("anchorbuy.anchor-info.txt"))),
                        Material.BOOK, Utils.Color(plugin.getConfig().getStringList("anchorbuy.anchor-info.lore")), true);
        inv.setItem(11, info);

        // Buy
        ItemStack buy = Utils.createItem(Utils.Color(Objects.requireNonNull(plugin.getConfig().getString("anchorbuy.buy.title"))),
                        Material.RESPAWN_ANCHOR, Collections.singletonList(Utils.Color(Objects.requireNonNull(plugin.getConfig().getString("anchorbuy.buy.lore"))).replaceAll("%price%", String.valueOf(plugin.getConfig().getInt("anchor-value")))), true);
        inv.setItem(15, buy);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inv;
    }
}
