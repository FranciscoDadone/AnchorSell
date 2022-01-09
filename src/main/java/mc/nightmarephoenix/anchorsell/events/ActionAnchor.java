package mc.nightmarephoenix.anchorsell.events;
import mc.nightmarephoenix.anchorsell.AnchorSell;
import mc.nightmarephoenix.anchorsell.inventories.AnchorAdmin;
import mc.nightmarephoenix.anchorsell.inventories.AnchorScreen;
import mc.nightmarephoenix.anchorsell.api.StorageManager;
import mc.nightmarephoenix.anchorsell.models.Anchor;
import mc.nightmarephoenix.anchorsell.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Objects;

public class ActionAnchor implements Listener {

    public ActionAnchor(AnchorSell plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        try {
            // If the player is sneaking, don't open the inventory.
            if(Objects.requireNonNull(e.getClickedBlock()).getType().equals(Material.RESPAWN_ANCHOR) && (p.isSneaking() && e.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
                e.setCancelled(true);
                return;
            }

            // If the player isn't sneaking...
            if((e.getClickedBlock().getType().equals(Material.RESPAWN_ANCHOR)) && (e.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
                if (!p.isSneaking()) {
                    // Checks if it's my anchor and opens the inventory.
                    Anchor anchor = StorageManager.getAnchorFromLoc(e.getClickedBlock().getLocation());

                    // Checks creative anchor
                    if (anchor == null) return;

                    e.setCancelled(true);
                    if (StorageManager.belongsToPlayer(anchor, p)) {
                        p.openInventory(new AnchorScreen(p, e.getClickedBlock().getLocation()).getInventory());
                    } else if(p.hasPermission("anchorsell.admin.anchoradmin")) {
                        p.openInventory(new AnchorAdmin(e.getClickedBlock().getLocation()).getInventory());
                    } else {
                        p.sendMessage(Utils.Color(Objects.requireNonNull(plugin.getConfig().getString("you-dont-own-this-anchor"))));
                    }
                }
            }
        } catch (Exception ignored) {}
    }

    private final AnchorSell plugin;
}