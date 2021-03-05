package mc.nightmarephoenix.anchorsell.events;
import mc.nightmarephoenix.anchorsell.inventories.AnchorScreen;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;


public class ActionAnchor implements Listener {

    @EventHandler
    public void onBlockClick(PlayerInteractEvent e) {
        if( (e.getClickedBlock().getType() == Material.RESPAWN_ANCHOR) && (e.getAction() == Action.RIGHT_CLICK_BLOCK) ) {
            Player p = (Player) e.getPlayer();
            p.openInventory(new AnchorScreen(p.getName()).getInventory());
        } else {
            return;
        }
    }
}