package mc.nightmarephoenix.anchorsell.inventories;

import mc.nightmarephoenix.anchorsell.AnchorSell;
import mc.nightmarephoenix.anchorsell.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.List;

public class ConfirmScreen implements InventoryHolder {
    private AnchorSell plugin;
    private Inventory inv;

    public ConfirmScreen(AnchorSell anchorSellPlugin) {
        this.plugin = anchorSellPlugin;
        inv = Bukkit.createInventory(this, 27, Utils.Color(plugin.getConfig().getString("confirmscreen.title")));

        init();
    }

    private void init() {
        ItemStack border = createItem(" ", Material.PURPLE_STAINED_GLASS_PANE, Collections.emptyList());

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

        // Yes
        ItemStack yesGlass = createItem(
                Utils.Color(""),
                Material.GREEN_STAINED_GLASS_PANE, Collections.emptyList());
        inv.setItem(10, yesGlass);
        inv.setItem(11, yesGlass);
        inv.setItem(12, yesGlass);
        System.out.println(plugin.getConfig().getString("confirmscreen.no"));


        // No
        ItemStack noGlass = createItem("",
                Material.RED_STAINED_GLASS_PANE, Collections.emptyList());
        inv.setItem(14, noGlass);
        inv.setItem(15, noGlass);
        inv.setItem(16, noGlass);
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
