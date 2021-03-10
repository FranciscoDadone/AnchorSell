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

public class BuyScreen implements InventoryHolder {

    private Player p;
    private Inventory inv;
    private AnchorSell plugin;

    public BuyScreen(Player p, AnchorSell plugin) {
        this.p = p;
        this.plugin = plugin;
        inv = Bukkit.createInventory(this, 27, Utils.Color(plugin.getConfig().getString("anchorbuy.title")));
        init();
    }

    private void init() {
        ItemStack item;
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
        ItemStack info = Utils.createItem(Utils.Color(plugin.getConfig().getString("anchorbuy.anchor-info.txt")),
                        Material.BOOK, Utils.Color(plugin.getConfig().getStringList("anchorbuy.anchor-info.lore")), true);
        inv.setItem(11, info);

        // Buy
        ItemStack buy = Utils.createItem(Utils.Color(plugin.getConfig().getString("anchorbuy.buy.title")),
                        Material.RESPAWN_ANCHOR, Collections.singletonList(""), true);
        inv.setItem(15, buy);
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }
}
