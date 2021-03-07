package mc.nightmarephoenix.anchorsell.inventories;

import mc.nightmarephoenix.anchorsell.AnchorSell;
import mc.nightmarephoenix.anchorsell.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.List;

public class AnchorScreen implements InventoryHolder {

    public AnchorScreen(Player p, AnchorSell plugin, Location location) {
        this.location = location;
        this.p = p;
        this.plugin = plugin;
        inv = Bukkit.createInventory(this, 27, Utils.Color(plugin.getConfig().getString("anchor.title")));
        init();
    }

    private void init() {
        ItemStack item;
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


        // Info
        ItemStack info = createItem(Utils.Color(plugin.getConfig().getString("anchor.current-anchor-info.txt")), Material.BOOK, Collections.singletonList(Utils.Color(plugin.getConfig().getString("anchor.current-anchor-info.lore"))));
        inv.setItem(11, info);

        // Player
        ItemStack player = createItem(p.getName(), Material.SKELETON_SKULL, Collections.singletonList(Utils.Color(plugin.getConfig().getString("anchor.player.lore"))));
        inv.setItem(13, player);

        // Upgrades
        ItemStack upgrades = createItem(Utils.Color(plugin.getConfig().getString("anchor.upgrades.txt")), Material.GLOWSTONE, Collections.singletonList(Utils.Color(plugin.getConfig().getString("anchor.upgrades.lore"))));
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




    private Player p;
    private Inventory inv;
    private AnchorSell plugin;
    public static Location location;
}
