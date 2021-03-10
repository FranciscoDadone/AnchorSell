package mc.nightmarephoenix.anchorsell.events;
import mc.nightmarephoenix.anchorsell.AnchorSell;
import mc.nightmarephoenix.anchorsell.inventories.AnchorScreen;
import mc.nightmarephoenix.anchorsell.storage.StorageManager;
import mc.nightmarephoenix.anchorsell.utils.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class ActionAnchor implements Listener {

    public ActionAnchor(AnchorSell plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        try {
            if(e.getClickedBlock().getType() == Material.RESPAWN_ANCHOR && p.isSneaking()) {
                e.setCancelled(true);
            }
            if((e.getClickedBlock().getType() == Material.RESPAWN_ANCHOR) && (e.getAction() == Action.RIGHT_CLICK_BLOCK) && (e.getAction() != null) && !p.isSneaking()) {
                if(StorageManager.isMyAnchor(e.getClickedBlock().getLocation(), p, plugin)) {
                    e.setCancelled(true);
                    Block block = e.getPlayer().getTargetBlock(null,100);
                    p.openInventory(new AnchorScreen(p, plugin, e.getClickedBlock().getLocation()).getInventory()); //e.getClickedBlock().getLocation()


//                    if(block.getState() instanceof InventoryHolder){
//                        InventoryHolder ih = (InventoryHolder)new AnchorScreen(p, plugin);
//                        Inventory i = ih.getInventory();
//                        e.getPlayer().openInventory(i);
//                    }

//                    AnchorScreen anchor = new AnchorScreen(p, plugin);
//                    p.openInventory(anchor.getInventory()); //e.getClickedBlock().getLocation()

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