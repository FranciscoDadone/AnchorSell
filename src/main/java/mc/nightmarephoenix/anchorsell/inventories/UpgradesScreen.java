package mc.nightmarephoenix.anchorsell.inventories;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.List;

public class UpgradesScreen implements InventoryHolder {
    private Inventory inv;

    public UpgradesScreen(int level) {
        inv = Bukkit.createInventory(this, 27, "Anchor Sell - Mejoras");

        System.out.println(Math.round(level / 16));
        switch (Math.round(level / 16)) {
            case 0:
                material = Material.COAL; break;
            case 1:
                material = Material.IRON_INGOT; break;
            case 2:
                material = Material.GOLD_INGOT; break;
            case 3:
                material = Material.DIAMOND; break;
            case 4:
                material = Material.NETHERITE_INGOT; break;
        }
        init();
    }

    private void init() {
        ItemStack border = createItem(" ", Material.GRAY_STAINED_GLASS_PANE, Collections.emptyList());
        // Top
        for( int i = 0; i < 9; i++ ) {
            inv.setItem(i, border);
        }
        // Left - Right
        inv.setItem(9, border);
        inv.setItem(17, border);
        // Bottom
        for( int i = 18; i < 27; i++ ) {
            inv.setItem(i, border);
        }


        for( int i = 10; i < 17; i++ ) {
            ItemStack item;
            if (i == 13) {
                // Current state
                item = createItem("Nivel actual", material, Collections.singletonList("Mostrar el nivel actual de una mejor forma"));
            } else {
                item = createItem("Actualizar", Material.LIME_STAINED_GLASS_PANE, Collections.singletonList("Actualizar el anchor actual"));
            }
            inv.setItem(i, item);
        }
    }


    private ItemStack createItem(String name, Material material, List<String> lore) {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }


    @Override
    public Inventory getInventory() {
        return inv;
    }



    private Material material;
}
