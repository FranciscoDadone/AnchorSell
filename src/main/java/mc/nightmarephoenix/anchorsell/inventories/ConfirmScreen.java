package mc.nightmarephoenix.anchorsell.inventories;

import mc.nightmarephoenix.anchorsell.AnchorSell;
import mc.nightmarephoenix.anchorsell.api.Global;
import mc.nightmarephoenix.anchorsell.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ConfirmScreen implements InventoryHolder {
    private final AnchorSell plugin;
    private final Inventory inv;

    public ConfirmScreen(String title, ItemStack middleItem) {
        this.plugin = Global.plugin;
        this.middleItem = middleItem;

        inv = Bukkit.createInventory(this, 27, Utils.Color(title));
        init();
    }

    private void init() {
        ItemStack border = Utils.createItem(" ", Material.GRAY_STAINED_GLASS_PANE, false);

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
        ItemStack yesGlass = Utils.createItem(Utils.Color(Objects.requireNonNull(plugin.getConfig().getString("confirmscreen.confirm"))), Material.GREEN_STAINED_GLASS_PANE, true);
        inv.setItem(10, yesGlass);
        inv.setItem(11, yesGlass);
        inv.setItem(12, yesGlass);

        // No
        ItemStack noGlass = Utils.createItem(Utils.Color(Objects.requireNonNull(plugin.getConfig().getString("confirmscreen.cancel"))), Material.RED_STAINED_GLASS_PANE, true);
        inv.setItem(14, noGlass);
        inv.setItem(15, noGlass);
        inv.setItem(16, noGlass);

        // Middle Item
        inv.setItem(13, middleItem);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inv;
    }

    private final ItemStack middleItem;
}
