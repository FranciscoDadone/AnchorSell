package mc.nightmarephoenix.anchorsell.inventories;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.List;

public class AnchorScreen implements InventoryHolder {
    private Inventory inv;

    public AnchorScreen(String playerName) {
        this.playerName = playerName;
        inv = Bukkit.createInventory(this, 27, "Anchor Sell");
        init();
    }

    private void init() {
        ItemStack item;
        ItemStack border = createItem(".", Material.PURPLE_STAINED_GLASS_PANE, Collections.emptyList());
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


        // Info
        ItemStack info = createItem("Info", Material.BOOK, Collections.singletonList("Mostrar información sobre el anchor actual"));
        inv.setItem(11, info);

        // Player
        ItemStack player = createItem(playerName, Material.SKELETON_SKULL, Collections.singletonList("Mostrar información sobre el jugador acá"));
        inv.setItem(13, player);

        // Upgrades
        ItemStack upgrades = createItem("Mejoras", Material.GLOWSTONE, Collections.singletonList("Haz click para ver las mejoras."));
        inv.setItem(15, upgrades);
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




    private String playerName;
}
