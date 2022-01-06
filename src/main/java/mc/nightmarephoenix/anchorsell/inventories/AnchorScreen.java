package mc.nightmarephoenix.anchorsell.inventories;

import mc.nightmarephoenix.anchorsell.AnchorSell;
import mc.nightmarephoenix.anchorsell.api.Global;
import mc.nightmarephoenix.anchorsell.api.StorageManager;
import mc.nightmarephoenix.anchorsell.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import java.util.Collections;
import java.util.Objects;

public class AnchorScreen implements InventoryHolder {

    public AnchorScreen(Player p, Location location) {
        this.location = location;
        this.p = p;
        this.plugin = Global.plugin;
        inv = Bukkit.createInventory(
                this,
                27,
                Utils.Color(Objects.requireNonNull(
                        Objects.requireNonNull(plugin.getConfig().getString("anchor.title"))
                                .replaceAll("%player%", p.getPlayerListName())
                                .replaceAll("%level%", String.valueOf(StorageManager.getAnchorLevel(location)))))
        );

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
        ItemStack info = Utils.createItem(Utils.Color(Objects.requireNonNull(plugin.getConfig().getString("anchor.current-anchor-info.txt"))),
                Material.BOOK, Utils.getLore("anchor.current-anchor-info.lore", location, p), false);
        inv.setItem(11, info);

        // Player head
        ItemStack skull = Utils.createItem(
                p.getName(),
                Material.PLAYER_HEAD,
                Utils.getLore("anchor.player.lore", location, p),
                false
        );
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        assert skullMeta != null;
        skullMeta.setDisplayName(p.getName());
        skullMeta.setOwningPlayer(p);
        skull.setItemMeta(skullMeta);
        inv.setItem(13, skull);

        // Upgrades
        ItemStack upgrades = Utils.createItem(Utils.Color(Objects.requireNonNull(plugin.getConfig().getString("anchor.upgrades.txt"))), Material.GLOWSTONE, Utils.getLore("anchor.upgrades.lore", location, p), false);
        inv.setItem(15, upgrades);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inv;
    }


    private final Player p;
    private final Inventory inv;
    private final AnchorSell plugin;
    private final Location location;

}
