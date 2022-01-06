package mc.nightmarephoenix.anchorsell.inventories;

import mc.nightmarephoenix.anchorsell.api.StorageManager;
import mc.nightmarephoenix.anchorsell.models.Anchor;
import mc.nightmarephoenix.anchorsell.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public class AnchorAdmin implements InventoryHolder {

    public AnchorAdmin(Player pAdmin, Location location) {
        this.pAdmin = pAdmin;
        this.anchor = StorageManager.getAnchorFromLoc(location);
        assert anchor != null;
        this.pOwner = anchor.getOwner();
        this.location = anchor.getLocation();

        inv = Bukkit.createInventory(
                this,
                27,
                Utils.Color("&5&lAnchor &c&lADMIN &7- " + anchor.getOwnerName())
        );

        playerHead();
        changeLevelButton();
    }


    private void playerHead() {

        // Player head
        ItemStack skull = Utils.createItem(
                anchor.getOwnerName(),
                Material.PLAYER_HEAD,
                Utils.getLore("anchor.player.lore", location, anchor.getOwner()),
                false
        );
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        assert skullMeta != null;
        skullMeta.setDisplayName(anchor.getOwnerName());
        skullMeta.setOwningPlayer(anchor.getOwner());
        skull.setItemMeta(skullMeta);
        inv.setItem(4, skull);
    }

    private void changeLevelButton() {

        ItemStack levelItem = Utils.createItem(
                Utils.Color("&6&lChange level"),
                Material.GLOWSTONE,
                Collections.singletonList(Utils.Color("&eCurrent level: &f" + anchor.getLevel())),
                false
        );

        inv.setItem(10, levelItem);

    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inv;
    }

    private final Inventory inv;
    private Location location;
    private Player pAdmin;
    private OfflinePlayer pOwner;
    private final Anchor anchor;
}
