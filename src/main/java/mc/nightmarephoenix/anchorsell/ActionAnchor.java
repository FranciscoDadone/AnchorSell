package mc.nightmarephoenix.anchorsell;
import mc.nightmarephoenix.anchorsell.inventories.AnchorScreen;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;


public class ActionAnchor implements Listener {

    @EventHandler
    public void onBlockClick(PlayerInteractEvent e) {
        if(e.getClickedBlock().getType() == Material.RESPAWN_ANCHOR) {
            System.out.println("Clicked");

            Player p = (Player) e.getPlayer();

            p.openInventory(new AnchorScreen().getInventory());


        }
    }
}