package mc.nightmarephoenix.anchorsell.inventories;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class AnchorScreen implements InventoryHolder {

    private Inventory inv;

    public AnchorScreen() {

        inv = Bukkit.createInventory(this, 54, "Título");

    }

    private void init() {

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
}
