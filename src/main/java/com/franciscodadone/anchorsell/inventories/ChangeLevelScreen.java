package com.franciscodadone.anchorsell.inventories;

import com.franciscodadone.anchorsell.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ChangeLevelScreen implements InventoryHolder {

    public ChangeLevelScreen(int level) {
        this.level = level;

        inv = Bukkit.createInventory(
                this,
                9,
                Utils.Color("&6&lChange level &7(" + level + ")")
        );

        levelItem();
        panels();

    }

    private void levelItem() {
        ItemStack levelItem = Utils.createItem(
                Utils.Color("&eLevel: &f" + level),
                Material.GLOWSTONE,
                true
        );
        inv.setItem(4, levelItem);
    }

    private void panels() {

        ItemStack limePanel = Utils.createItem(
                Utils.Color("&a&l+"),
                Material.LIME_STAINED_GLASS_PANE,
                false
        );
        ItemStack redPanel = Utils.createItem(
                Utils.Color("&c&l-"),
                Material.RED_STAINED_GLASS_PANE,
                false
        );

        for(int i = 0; i < 9; i++) {
            if(i <= 3) inv.setItem(i, limePanel);
            else if(i >= 5) inv.setItem(i, redPanel);
        }


    }


    @NotNull
    @Override
    public Inventory getInventory() {
        return inv;
    }

    private final Inventory inv;
    private final int level;
}
