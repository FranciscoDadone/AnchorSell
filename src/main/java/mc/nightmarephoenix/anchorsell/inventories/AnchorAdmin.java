package mc.nightmarephoenix.anchorsell.inventories;

import mc.nightmarephoenix.anchorsell.api.Global;
import mc.nightmarephoenix.anchorsell.api.StorageManager;
import mc.nightmarephoenix.anchorsell.models.Anchor;
import mc.nightmarephoenix.anchorsell.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import java.util.Collections;
import java.util.Objects;

public class AnchorAdmin implements InventoryHolder {

    public AnchorAdmin(Location location) {
        this.anchor = StorageManager.getAnchorFromLoc(location);
        assert anchor != null;
        this.location = anchor.getLocation();

        inv = Bukkit.createInventory(
                this,
                27,
                Utils.Color("&5&lAnchor &c&lADMIN &7- " + anchor.getOwnerName())
        );

        // GUI Items
        playerHead();
        changeLevelButton();
        anchorInfo();
        removeAnchor();
        fillerPanels();
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

    private void anchorInfo() {
        // Info
        ItemStack info = Utils.createItem(
                Utils.Color(Objects.requireNonNull(Global.plugin.getConfig().getString("anchor.current-anchor-info.txt"))),
                Material.BOOK,
                Utils.getLore("anchor.current-anchor-info.lore", location, anchor.getOwner()),
                false);
        inv.setItem(13, info);
    }

    private void removeAnchor() {
        ItemStack remove = Utils.createItem(
          Utils.Color("&4&lRemove"),
          Material.BARRIER,
                Collections.singletonList(Utils.Color("&cRemoves this anchor")),
          false
        );
        inv.setItem(16, remove);
    }

    /**
     * Fills the screen with gray panels.
     * otherItemsIndexes: indexes of the other items such as This anchor, remove or player head. This indexes will be skipped.
     */
    private void fillerPanels() {
        int[] otherItemsIndexes = {4, 10, 13, 16};
        ItemStack border = Utils.createItem(" ", Material.GRAY_STAINED_GLASS_PANE, false);

        for(int i = 0; i < 27; i++) {
            for (int otherItemsIndex : otherItemsIndexes) {
                if (i == otherItemsIndex) {
                    i++;
                    break;
                }
            }
            inv.setItem(i, border);
        }
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inv;
    }

    private final Inventory inv;
    private final Location location;
    private final Anchor anchor;
}
