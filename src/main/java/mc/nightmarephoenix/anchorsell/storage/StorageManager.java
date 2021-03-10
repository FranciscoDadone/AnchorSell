package mc.nightmarephoenix.anchorsell.storage;

import mc.nightmarephoenix.anchorsell.AnchorSell;
import mc.nightmarephoenix.anchorsell.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
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

        // Determines what number the anchor can have
        for(int i = 1; i <= userData.getConfig().getInt("total"); i++) {
            if(!userData.getConfig().contains("anchors." + i)) {
                // Storing the data in the user file
                userData.getConfig().set("anchors." + i + ".location.x", location.getX());
                userData.getConfig().set("anchors." + i + ".location.y", location.getY());
                userData.getConfig().set("anchors." + i + ".location.z", location.getZ());
                userData.getConfig().set("anchors." + i + ".level", currentAnchorLevel);
                break;
            }
        }

        //Storing in the general file
        generalData.getConfig().set("all_anchors." + anchorID + ".location.x", location.getX());
        generalData.getConfig().set("all_anchors." + anchorID + ".location.y", location.getY());
        generalData.getConfig().set("all_anchors." + anchorID + ".location.z", location.getZ());
        generalData.getConfig().set("all_anchors." + anchorID + ".owner", p.getUniqueId().toString());
        generalData.getConfig().set("all_anchors." + anchorID + ".level", currentAnchorLevel);


        // Saving to configs
        userData.saveConfig();
        generalData.saveConfig();

        // Announcing to the user that the anchor has been placed
        Utils.Color(plugin.getConfig().getStringList("anchor-place")).forEach((str) -> {
            p.sendMessage(str.replaceAll("%coordsX%", String.valueOf(location.getX())).
                    replaceAll("%coordsY%", String.valueOf(location.getY())).
                    replaceAll("%coordsZ%", String.valueOf(location.getZ())).
                    replaceAll("%level%", String.valueOf(StorageManager.getAnchorLevel(plugin, location))));
        });
    }

    public static void anchorBreak(AnchorSell plugin, Location location) {
        generalData = new GeneralStorage(plugin);

        // Anchor UUID
        String anchorID = getAnchorUUID(location);


        // Figuring out who's the anchor owner
        Player p;
        try {
            p = Bukkit.getPlayer(UUID.fromString(generalData.getConfig().getString("all_anchors." + anchorID + ".owner")));
            userData = new PerUSerStorage(plugin, p);
        } catch (Exception exception) {
            // Creative anchor break not work
            return;
        }

        // Updating the total amount of anchors in the user config
        int totalUserAnchors = 0;
        if(userData.getConfig().contains("total") && userData.getConfig().getInt("total") > 0) {
            totalUserAnchors = userData.getConfig().getInt("total");
            totalUserAnchors--;
        }
        userData.getConfig().set("total", (totalUserAnchors));

        // Storing the data in the user file
        for(int i = 1; i <= plugin.getConfig().getInt("total-anchors-user-can-have"); i++) {
            if(userData.getConfig().contains("anchors." + i)) {
                if(location.getX() == userData.getConfig().getInt("anchors." + i + ".location.x") &&
                        location.getY() == userData.getConfig().getInt("anchors." + i + ".location.y") &&
                        location.getZ() == userData.getConfig().getInt("anchors." + i + ".location.z")) {
                    userData.getConfig().set("anchors." + i, null);
                }
            }
        }

        //Storing in the general file
        generalData.getConfig().set("all_anchors." + anchorID, null);

        // Saving to configs
        userData.saveConfig();
        generalData.saveConfig();

    }

    public static boolean isARegisterAnchor(AnchorSell plugin, Location location) {
        generalData = new GeneralStorage(plugin);
        return generalData.getConfig().contains("all_anchors." + getAnchorUUID(location));
    }

    public static int getAnchorLevel(AnchorSell plugin, Location location) {
        generalData = new GeneralStorage(plugin);
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
            Player actualPlayerAnchor = Bukkit.getPlayer(UUID.fromString(
                    generalData.getConfig().getString("all_anchors." + getAnchorUUID(location) + ".owner")));

            if(p.getUniqueId().toString().equals(actualPlayerAnchor.getUniqueId().toString())) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static int getPlayerTotalAnchors(AnchorSell plugin, Player p) {
        return getUserData(plugin, p).getConfig().getInt("total");
    }

    public static double getPlayerMoneyPerMinute(AnchorSell plugin, Player p) {
        userData = new PerUSerStorage(plugin, p);
        double res = 0;
        for(int i = 1; i <= plugin.getConfig().getInt("total-anchors-user-can-have"); i++) {
            if(userData.getConfig().contains("anchors." + i)) {
                res += Utils.getMoneyPerMinute(userData.getConfig().getInt("anchors." + i + ".level"));
            }
        }
        return res;
    }

    public static void getAnchorUserList(AnchorSell plugin, Player p) throws InvalidConfigurationException {
        userData = new PerUSerStorage(plugin, p);

        for(int i = 1; i <= plugin.getConfig().getInt("total-anchors-user-can-have"); i++) {
            if(userData.getConfig().contains("anchors." + i)) {
                p.sendMessage("[" + i + "] "
                        + "Level: " + userData.getConfig().getInt("anchors."+ i + ".level")
                        + " Location: x " + userData.getConfig().getInt("anchors."+ i + ".location.x")
                        + " z " + userData.getConfig().getInt("anchors."+ i + ".location.z")
                        + " y " + userData.getConfig().getInt("anchors."+ i + ".location.y")
                );
            }
        }
    }

    public static void upgradeAnchor(AnchorSell plugin, Location location, Player p) {
        userData = new PerUSerStorage(plugin, p);
        generalData = new GeneralStorage(plugin);

        // Updating user data...
        for(int i = 1; i <= plugin.getConfig().getInt("total-anchors-user-can-have"); i++) {
            if(userData.getConfig().contains("anchors." + i)) {
                if(userData.getConfig().getInt("anchors." + i + ".location.x") == location.getX() &&
                        userData.getConfig().getInt("anchors." + i + ".location.y") == location.getY() &&
                        userData.getConfig().getInt("anchors." + i + ".location.z") == location.getZ()) {
                    int currentLevel = userData.getConfig().getInt("anchors." + i + ".level");
                    if(currentLevel >= 64)
                        return;
                    userData.getConfig().set("anchors." + i + ".level", (currentLevel + 1));
                }
            }
        }

        // Updating general data...
        generalData.getConfig().set("all_anchors." + StorageManager.getAnchorUUID(location) + ".level",
                generalData.getConfig().getInt("all_anchors." + StorageManager.getAnchorUUID(location) + ".level") + 1);

        generalData.saveConfig();
        userData.saveConfig();
    }

    public static GeneralStorage getGeneralStorage() {
        return generalData;
    }

    public static PerUSerStorage getUserData(AnchorSell plugin, Player p) {
        return new PerUSerStorage(plugin, p);
    }

    public static PerUSerStorage userData;
    public static GeneralStorage generalData;
}
