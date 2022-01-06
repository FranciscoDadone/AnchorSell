package mc.nightmarephoenix.anchorsell.inventories;

import mc.nightmarephoenix.anchorsell.api.StorageManager;
import mc.nightmarephoenix.anchorsell.models.Anchor;
import mc.nightmarephoenix.anchorsell.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class AnchorAdmin implements InventoryHolder {

    public AnchorAdmin(Player pAdmin, Location location) {
        this.pAdmin = pAdmin;
        this.anchor = StorageManager.getAnchorFromLoc(location);
        assert anchor != null;
        this.pOwner = anchor.getOwner();

        inv = Bukkit.createInventory(
                this,
                27,
                Utils.Color("&5&lAnchor &c&lADMIN &7- " + anchor.getOwnerName())
        );
    }


    private void changeLevelButton() {



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
