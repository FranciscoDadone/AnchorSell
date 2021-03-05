package mc.nightmarephoenix.anchorsell.events;

import mc.nightmarephoenix.anchorsell.utils.Utils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class AnchorPlace implements Listener {
    @EventHandler
    public void place(BlockPlaceEvent e) {
        Block block = e.getBlock();
        Player p = e.getPlayer();

        if(block.getType() == Material.RESPAWN_ANCHOR) {
            p.sendMessage(Utils.Color("&aNuevo anchor puesto en (" + block.getX() + ", " + block.getY() + ", " + block.getZ() + ")"));
        }
    }
}
