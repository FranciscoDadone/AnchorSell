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
import java.util.Collections;

public class AnchorScreen implements InventoryHolder {
    private Location location;

    public AnchorScreen(Player p, AnchorSell plugin, Location location) {
        this.location = location;
        this.p = p;
        this.plugin = plugin;
        inv = Bukkit.createInventory(this, 27, Utils.Color(plugin.getConfig().getString("anchor.title")));

        init();
    }

    private void init() {
        ItemStack border = Utils.createItem(" ", Material.PURPLE_STAINED_GLASS_PANE,
                Collections.emptyList(), false);
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
        ItemStack info = Utils.createItem(Utils.Color(plugin.getConfig().getString("anchor.current-anchor-info.txt")),
                Material.BOOK, Utils.getLore("anchor.current-anchor-info.lore", plugin, location, p), false);
        inv.setItem(11, info);

        // Player
        ItemStack player = Utils.createItem(p.getName(), Material.SKELETON_SKULL, Utils.getLore("anchor.player.lore", plugin, location, p), false);
        inv.setItem(13, player);

        // Upgrades
        ItemStack upgrades = Utils.createItem(Utils.Color(plugin.getConfig().getString("anchor.upgrades.txt")), Material.GLOWSTONE, Utils.getLore("anchor.upgrades.lore", plugin, location, p), false);
        inv.setItem(15, upgrades);
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }

    private Player p;
    private Inventory inv;
    private AnchorSell plugin;

}
