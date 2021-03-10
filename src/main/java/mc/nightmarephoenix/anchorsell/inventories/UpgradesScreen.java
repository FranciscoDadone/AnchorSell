package mc.nightmarephoenix.anchorsell.inventories;

import mc.nightmarephoenix.anchorsell.AnchorSell;
import mc.nightmarephoenix.anchorsell.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import java.util.Collections;

public class UpgradesScreen implements InventoryHolder {

    public UpgradesScreen(AnchorSell plugin, int level) {
        this.plugin = plugin;
        inv = Bukkit.createInventory(this, 27, Utils.Color(plugin.getConfig().getString("anchor.upgrade-menu.title")));
        this.level = level;
        material = Utils.getAnchorOreLevel(level);
        init();
    }

    private void init() {
        ItemStack border = Utils.createItem(" ", Material.GRAY_STAINED_GLASS_PANE, Collections.emptyList());
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

        for( int i = 10; i < 17; i++ ) {
            ItemStack item;
            if (i == 13) {
                // Current state
                item = Utils.createItem(Utils.Color(plugin.getConfig().getString("anchor.upgrade-menu.current-level.txt").replaceAll("%currentLevel%", Utils.getAnchorOreLevelString(plugin, level) + "  &f(" + level + ")")), material, Collections.singletonList(Utils.Color(plugin.getConfig().getString("anchor.upgrade-menu.current-level.lore"))));
            } else {
                item = Utils.createItem(Utils.Color(plugin.getConfig().getString("anchor.upgrade-menu.upgrade-button.txt")), Material.LIME_STAINED_GLASS_PANE, Utils.getLore("anchor.upgrade-menu.upgrade-button.lore", plugin, null, null));
            }
            inv.setItem(i, item);
        }
        inv.setItem(18, Utils.createItem(Utils.Color(plugin.getConfig().getString("anchor.upgrade-menu.back")), Material.BARRIER, Collections.emptyList())); // back button
    }


    @Override
    public Inventory getInventory() {
        return inv;
    }

    private AnchorSell plugin;
    private Inventory inv;
    private Material material;
    private int level;
}
