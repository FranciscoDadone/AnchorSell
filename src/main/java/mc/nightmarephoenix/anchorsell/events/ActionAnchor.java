package mc.nightmarephoenix.anchorsell.events;
import mc.nightmarephoenix.anchorsell.AnchorSell;
import mc.nightmarephoenix.anchorsell.inventories.AnchorScreen;
import mc.nightmarephoenix.anchorsell.storage.StorageManager;
import mc.nightmarephoenix.anchorsell.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class ActionAnchor implements Listener {

    public ActionAnchor(AnchorSell plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        try {
            /**
             * If the player is sneaking, don't open the inventory.
             */
            if(e.getClickedBlock().getType().equals(Material.RESPAWN_ANCHOR) && (p.isSneaking() && e.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
                e.setCancelled(true);
                return;
            }

            /**
             * If the player isn't sneaking...
             */
            if((e.getClickedBlock().getType().equals(Material.RESPAWN_ANCHOR)) && (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) && (e.getAction() != null) && !p.isSneaking()) {
                /**
                 * Checks if it's my anchor and opens the inventory.
                 */
                if(StorageManager.isMyAnchor(e.getClickedBlock().getLocation(), p, plugin)) {
                    e.setCancelled(true);
                    p.openInventory(new AnchorScreen(p, plugin, e.getClickedBlock().getLocation()).getInventory());
                } else {
                    e.setCancelled(true);
                    p.sendMessage(Utils.Color(plugin.getConfig().getString("you-dont-own-this-anchor")));
                }
            }
            return;
        } catch (Exception e1) {
            return;
        }
    }

    private AnchorSell plugin;
}