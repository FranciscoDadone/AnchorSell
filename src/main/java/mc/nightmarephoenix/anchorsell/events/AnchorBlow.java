package mc.nightmarephoenix.anchorsell.events;

import mc.nightmarephoenix.anchorsell.AnchorSell;
import mc.nightmarephoenix.anchorsell.storage.GeneralStorage;
import mc.nightmarephoenix.anchorsell.storage.PerUSerStorage;
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
    PerUSerStorage storage;
    GeneralStorage data;

    public AnchorBlow(AnchorSell plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onExplosion(ExplosionPrimeEvent e) {
        Entity entity = e.getEntity();
        int radius = Math.round(e.getRadius());
        ArrayList<Block> blocks = getNearbyBlocks(entity.getLocation(), radius);
        for(Block b : blocks) {
            if(b.getType().equals(Material.RESPAWN_ANCHOR)) {
                data = new GeneralStorage(plugin);
                storage = new PerUSerStorage(plugin, (Player) data.getConfig().get("all_anchors." +(b.getX()+b.getY()+b.getZ()) + ".owner"));
                world = entity.getLocation().getWorld();
                b.breakNaturally();

                if(        data.getConfig().get("all_anchors." + (b.getX()+b.getY()+b.getZ()) + ".location.x").equals(b.getX())
                        && data.getConfig().get("all_anchors." + (b.getX()+b.getY()+b.getZ()) + ".location.y").equals(b.getY())
                        && data.getConfig().get("all_anchors." + (b.getX()+b.getY()+b.getZ()) + ".location.z").equals(b.getZ())) {
                    data.getConfig().set("all_anchors." + (b.getX()+b.getY()+b.getZ()), null);
                    //storage.getConfig().set("anchors." + (b.getX()+b.getY()+b.getZ()) + ".level", data.getConfig().get("all_anchors." + (b.getX()+b.getY()+b.getZ()) + ".level"));
                }

                if(storage.getConfig().get("anchors." + (b.getX()+b.getY()+b.getZ())) != null || storage.getConfig().get("anchors." + (b.getX()+b.getY()+b.getZ())) != "") {
                    storage.getConfig().set("anchors." + (b.getX()+b.getY()+b.getZ()), null);
                }

                world.dropItem(b.getLocation(), Utils.getAnchor(18)).setInvulnerable(true);
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
    private AnchorSell plugin;

}
