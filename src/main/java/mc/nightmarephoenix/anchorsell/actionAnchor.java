package mc.nightmarephoenix.anchorsell;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;


public class actionAnchor implements Listener {

    @EventHandler
    public void onBlockClick(PlayerInteractEvent e) {
        if(e.getClickedBlock().getType() == Material.RESPAWN_ANCHOR) {
            System.out.println("Clicked");
        }
    }
}
