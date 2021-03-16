package mc.nightmarephoenix.anchorsell.storage;

import mc.nightmarephoenix.anchorsell.AnchorSell;
import mc.nightmarephoenix.anchorsell.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.UUID;

public class StorageManager {
    public static boolean anchorPlace(AnchorSell plugin, BlockPlaceEvent e, Player p, Location location, int currentAnchorLevel) {
        userData = new PerUSerStorage(plugin, p);
        generalData = new GeneralStorage(plugin);

        // Updating the total amount of anchors in the user config
        int totalUserAnchors = 0;
        if(userData.getConfig().contains("total")) {
            if(userData.getConfig().getInt("total") < plugin.getConfig().getInt("total-anchors-user-can-have")) {
                totalUserAnchors = userData.getConfig().getInt("total");
                userData.getConfig().set("total", (totalUserAnchors + 1));
            } else {
                p.sendMessage(Utils.Color(plugin.getConfig().getString("cannot-place-more-anchors").replaceAll("%quantity%", String.valueOf(plugin.getConfig().getInt("total-anchors-user-can-have")))));
                e.setCancelled(true);
                return false;
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
                userData.getConfig().set("anchors." + i + ".location.world", location.getWorld().getName());
                userData.getConfig().set("anchors." + i + ".level", currentAnchorLevel);
                break;
            }
        }

        //Storing in the general file
        generalData.getConfig().set("all_anchors." + anchorID + ".location.x", location.getX());
        generalData.getConfig().set("all_anchors." + anchorID + ".location.y", location.getY());
        generalData.getConfig().set("all_anchors." + anchorID + ".location.z", location.getZ());
        generalData.getConfig().set("all_anchors." + anchorID + ".location.world", location.getWorld().getName());
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
        return true;
    }

    public static void anchorBreak(AnchorSell plugin, Location location) {
        generalData = new GeneralStorage(plugin);

        // Anchor UUID
        String anchorID = getAnchorUUID(location);

        // Figuring out who's the anchor owner
        Player p;
        try {
            String uuid = generalData.getConfig().getString("all_anchors." + anchorID + ".owner");
            p = Bukkit.getPlayer(UUID.fromString(uuid));
            userData = new PerUSerStorage(plugin, p);
        } catch (Exception exception) {
            exception.printStackTrace();
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

    public static void getAnchorUserList(AnchorSell plugin, Player p, Player toSendBack) throws InvalidConfigurationException {
        userData = new PerUSerStorage(plugin, p);

        toSendBack.sendMessage(Utils.Color(plugin.getConfig().getString("anchor.list.first-message")));

        for(int i = 1; i <= plugin.getConfig().getInt("total-anchors-user-can-have"); i++) {
            if(userData.getConfig().contains("anchors." + i)) {

                int finalI = i;
                Utils.Color(plugin.getConfig().getStringList("anchor.list.message")).forEach((str) -> {
                    toSendBack.sendMessage(
                            str.replaceAll("%location%", "X: " + userData.getConfig().getInt("anchors."+ finalI + ".location.x")
                                            + " Y: " + userData.getConfig().getInt("anchors."+ finalI + ".location.y")
                                            + " Z: " + userData.getConfig().getInt("anchors."+ finalI + ".location.z"))
                                    .replaceAll("%level%", String.valueOf(userData.getConfig().getInt("anchors."+ finalI + ".level")))
                    );
                });
            }
        }
        toSendBack.sendMessage(Utils.Color(plugin.getConfig().getString("anchor.list.last-message")));
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
        if(generalData.getConfig().getString("all_anchors." + StorageManager.getAnchorUUID(location) + ".owner").equals(p.getUniqueId().toString())) {
            generalData.getConfig().set("all_anchors." + StorageManager.getAnchorUUID(location) + ".level",
                    generalData.getConfig().getInt("all_anchors." + StorageManager.getAnchorUUID(location) + ".level") + 1);
        }

        generalData.saveConfig();
        userData.saveConfig();
    }

    public static void changeUpgradeMultiplier(AnchorSell plugin, int multiplier) {
        plugin.getConfig().set("anchor.upgrade-multiplier", multiplier);
        plugin.saveConfig();
    }

    public static void changePrice(AnchorSell plugin, int price) {
        plugin.getConfig().set("anchor-value", price);
        plugin.saveConfig();
    }

    public static void changeSafeZone(AnchorSell plugin, int zone) {
        plugin.getConfig().set("safe-anchor-area", zone);
        plugin.saveConfig();
    }

    public static void changeTotalAnchorsUserCanHave(AnchorSell plugin, int n) {
        plugin.getConfig().set("total-anchors-user-can-have", n);
        plugin.saveConfig();
    }

    public static void revalidateAll(AnchorSell plugin) {
        generalData = new GeneralStorage(plugin);
        for(Player p: Bukkit.getOnlinePlayers()) {
            userData = new PerUSerStorage(plugin, p);
            for(int i = 1; i <= plugin.getConfig().getInt("total-anchors-user-can-have"); i++) {
                if(userData.getConfig().contains("anchors." + i)) {
                    Location loc;
                    if(userData.getConfig().contains("anchors." + i + ".location.world")) {
                        loc = new Location(Bukkit.getWorld(userData.getConfig().getString("anchors." + i + ".location.world")),
                                userData.getConfig().getInt("anchors." + i + ".location.x"),
                                userData.getConfig().getInt("anchors." + i + ".location.y"),
                                userData.getConfig().getInt("anchors." + i + ".location.z"));
                    } else {
                        loc = new Location(Bukkit.getWorld("world"),
                                userData.getConfig().getInt("anchors." + i + ".location.x"),
                                userData.getConfig().getInt("anchors." + i + ".location.y"),
                                userData.getConfig().getInt("anchors." + i + ".location.z"));
                    }

                    if(loc.getBlock().getType() != Material.RESPAWN_ANCHOR) {
                        userData.getConfig().set("anchors." + i, null);
                        userData.getConfig().set("total", userData.getConfig().getInt("total") - 1);
                        if(generalData.getConfig().contains("all_anchors." + StorageManager.getAnchorUUID(loc))) {
                            generalData.getConfig().set("all_anchors." + StorageManager.getAnchorUUID(loc), null);
                        }
                        userData.saveConfig();
                        generalData.saveConfig();
                    }
                }
            }
        }
    }

    public static void revalidateUser(AnchorSell plugin, Player p) {
        generalData = new GeneralStorage(plugin);
        userData = new PerUSerStorage(plugin, p);
        for(int i = 1; i <= plugin.getConfig().getInt("total-anchors-user-can-have"); i++) {
            if(userData.getConfig().contains("anchors." + i)) {
                Location loc;
                if(userData.getConfig().contains("anchors." + i + ".location.world")) {
                    loc = new Location(Bukkit.getWorld(userData.getConfig().getString("anchors." + i + ".location.world")),
                            userData.getConfig().getInt("anchors." + i + ".location.x"),
                            userData.getConfig().getInt("anchors." + i + ".location.y"),
                            userData.getConfig().getInt("anchors." + i + ".location.z"));
                } else {
                    loc = new Location(Bukkit.getWorld("world"),
                            userData.getConfig().getInt("anchors." + i + ".location.x"),
                            userData.getConfig().getInt("anchors." + i + ".location.y"),
                            userData.getConfig().getInt("anchors." + i + ".location.z"));
                }

                if(loc.getBlock().getType() != Material.RESPAWN_ANCHOR) {
                    userData.getConfig().set("anchors." + i, null);
                    userData.getConfig().set("total", userData.getConfig().getInt("total") - 1);
                    if(generalData.getConfig().contains("all_anchors." + StorageManager.getAnchorUUID(loc))) {
                        generalData.getConfig().set("all_anchors." + StorageManager.getAnchorUUID(loc), null);
                    }
                    userData.saveConfig();
                    generalData.saveConfig();
                }
            }
        }
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
