package mc.nightmarephoenix.anchorsell.storage;

import mc.nightmarephoenix.anchorsell.AnchorSell;
import mc.nightmarephoenix.anchorsell.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.UUID;

public class StorageManager {
    public static void anchorPlace(AnchorSell plugin, BlockPlaceEvent e, Player p, Location location) {
        userData = new PerUSerStorage(plugin, p);
        generalData = new GeneralStorage(plugin);

        int currentAnchorLevel = -1;

        // Getting the current anchor level before placing the block
        try {
            currentAnchorLevel = Integer.parseInt(String.valueOf(e.getItemInHand().getItemMeta().getLore().get(2).substring(18)));
        } catch (NullPointerException nullPointerException) {
            // Creative respawn anchor not work
            return;
        }

        if (currentAnchorLevel == 0) {
            currentAnchorLevel = 1;
        }

        // Updating the total amount of anchors in the user config
        int totalUserAnchors = 0;
        if(userData.getConfig().contains("total")) {
            if(userData.getConfig().getInt("total") < plugin.getConfig().getInt("total-anchors-user-can-have")) {
                totalUserAnchors = userData.getConfig().getInt("total");
                userData.getConfig().set("total", (totalUserAnchors + 1));
            } else {
                p.sendMessage(Utils.Color(plugin.getConfig().getString("cannot-place-more-anchors").replaceAll("%quantity%", String.valueOf(plugin.getConfig().getInt("total-anchors-user-can-have")))));
                e.setCancelled(true);
                return;
            }
        } else {
            userData.getConfig().set("total", (totalUserAnchors + 1));
        }

        // Anchor UUID
        String anchorID = getAnchorUUID(location);

        // Storing the data in the user file
        userData.getConfig().set("anchors." + anchorID + ".location.x", location.getX());
        userData.getConfig().set("anchors." + anchorID + ".location.y", location.getY());
        userData.getConfig().set("anchors." + anchorID + ".location.z", location.getZ());
        userData.getConfig().set("anchors." + anchorID + ".level", currentAnchorLevel);

        //Storing in the general file
        generalData.getConfig().set("all_anchors." + anchorID + ".location.x", location.getX());
        generalData.getConfig().set("all_anchors." + anchorID + ".location.y", location.getY());
        generalData.getConfig().set("all_anchors." + anchorID + ".location.z", location.getZ());
        generalData.getConfig().set("all_anchors." + anchorID + ".owner", p.getUniqueId().toString());
        generalData.getConfig().set("all_anchors." + anchorID + ".level", currentAnchorLevel);


        // Saving to configs
        userData.saveConfig();
        generalData.saveConfig();
        p.sendMessage(Utils.Color(plugin.getConfig().getString("anchor-place").replaceAll("%coordsX%", String.valueOf(location.getX())).replaceAll("%coordsY%", String.valueOf(location.getY())).replaceAll("%coordsZ%", String.valueOf(location.getZ()))));
    }

    public static void anchorBreak(AnchorSell plugin, Location location) {
        generalData = new GeneralStorage(plugin);

        // Anchor UUID
        String anchorID = getAnchorUUID(location);

        // Figuring out who's the anchor owner
        Player p = Bukkit.getPlayer(UUID.fromString(generalData.getConfig().getString("all_anchors." + anchorID + ".owner")));
        userData = new PerUSerStorage(plugin, p);


        // Updating the total amount of anchors in the user config
        int totalUserAnchors = 0;
        if(userData.getConfig().contains("total") && userData.getConfig().getInt("total") > 0) {
            totalUserAnchors = userData.getConfig().getInt("total");
            totalUserAnchors--;
        }
        userData.getConfig().set("total", (totalUserAnchors));


        // Storing the data in the user file
        userData.getConfig().set("anchors." + anchorID, null);

        //Storing in the general file
        generalData.getConfig().set("all_anchors." + anchorID, null);


        // Saving to configs
        userData.saveConfig();
        generalData.saveConfig();

    }

    public static int getAnchorLevel(Location location) {
        // Getting the current anchor level
        int currentAnchorLevel = generalData.getConfig().getInt("all_anchors." + getAnchorUUID(location) + ".level");
        if(currentAnchorLevel == 0) {
            currentAnchorLevel = 1;
        }
        return currentAnchorLevel;
    }

    public static String getAnchorUUID(Location location) {
        return (int)location.getX() + "_" + (int)location.getY() + "_" + (int)location.getZ();
    }

    public static boolean isMyAnchor(Location location, Player p, AnchorSell plugin) {
        userData = new PerUSerStorage(plugin, p);
        generalData = new GeneralStorage(plugin);

        try {
            Player actualPlayerAnchor = Bukkit.getPlayer(UUID.fromString(generalData.getConfig().getString("all_anchors." + getAnchorUUID(location) + ".owner")));

            if(p.getUniqueId().toString().equals(actualPlayerAnchor.getUniqueId().toString())) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static PerUSerStorage userData;
    public static GeneralStorage generalData;
}
