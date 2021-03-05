package mc.nightmarephoenix.anchorsell.events;
import mc.nightmarephoenix.anchorsell.AnchorSell;
import mc.nightmarephoenix.anchorsell.inventories.AnchorScreen;
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
        try {
            if( (e.getClickedBlock().getType() == Material.RESPAWN_ANCHOR) && (e.getAction() == Action.RIGHT_CLICK_BLOCK) && (e.getAction() != null) ) {
                Player p = (Player) e.getPlayer();
                p.openInventory(new AnchorScreen(p, plugin).getInventory());
            } else {
                return;
            }
        } catch (Exception e1) {
            System.out.println(e1);
        }
    }

    private AnchorSell plugin;
}