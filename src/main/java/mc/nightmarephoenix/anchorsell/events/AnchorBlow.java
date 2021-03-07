package mc.nightmarephoenix.anchorsell.events;

import mc.nightmarephoenix.anchorsell.utils.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ExplosionPrimeEvent;

import java.util.ArrayList;

public class AnchorBlow implements Listener {
    @EventHandler
    public void onExplosion(ExplosionPrimeEvent e) {
        Entity entity = e.getEntity();
        int radius = Math.round(e.getRadius());
        ArrayList<Block> blocks = getNearbyBlocks(entity.getLocation(), radius);
        for(Block b : blocks) {
            if(b.getType().equals(Material.RESPAWN_ANCHOR)) {
                world = entity.getLocation().getWorld();
                b.breakNaturally();

                ArrayList<String> Lore = new ArrayList<String>();
                Lore.add("");
                Lore.add(Utils.Color("&7&m----------------------------"));
                Lore.add(Utils.Color("&fAnchor level: &e1"));
                Lore.add(Utils.Color("&fMoney per minute: &e" + Utils.getMoneyPerMinute(1)));
                Lore.add(Utils.Color("&7&m----------------------------"));

                world.dropItem(b.getLocation(), Utils.getAnchor("&5Anchor", Lore)).setInvulnerable(true);
            }
        }
    }

    public ArrayList<Block> getNearbyBlocks(Location location, int radius) {
        ArrayList<Block> blocks = new ArrayList<Block>();
        for(int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for(int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for(int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    blocks.add(location.getWorld().getBlockAt(x, y, z));
                }
            }
        }
        return blocks;
    }
    private World world;


}
