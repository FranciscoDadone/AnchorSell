package mc.nightmarephoenix.anchorsell.storage;

import mc.nightmarephoenix.anchorsell.AnchorSell;
import mc.nightmarephoenix.anchorsell.utils.Utils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.*;

public class StorageManager {

    /**
     * Handles the save of a new anchor being placed.
     *
     * @param plugin
     * @param e
     * @param p
     * @param location
     * @param currentAnchorLevel
     * @return
     */
    public static boolean anchorPlace(AnchorSell plugin, BlockPlaceEvent e, Player p, Location location, int currentAnchorLevel) {
        userData = new PerUserStorage(plugin, p);
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

    /**
     * Determines if a user can place more anchors based on the total it has.
     *
     * @param plugin
     * @param p
     * @return boolean
     */
    public static boolean userCanPlaceMoreAnchors(AnchorSell plugin, Player p) {
        userData = new PerUserStorage(plugin, p);
        if(userData.getConfig().contains("total")) {
            return userData.getConfig().getInt("total") < plugin.getConfig().getInt("total-anchors-user-can-have");
        }
        return true;
    }

    /**
     * Saves to the config the anchor break.
     *
     * @param plugin
     * @param location
     */
    public static void anchorBreak(AnchorSell plugin, Location location) {
        generalData = new GeneralStorage(plugin);

        // Anchor UUID
        String anchorID = getAnchorUUID(location);

        // Figuring out who's the anchor owner
        OfflinePlayer p;
        try {
            String uuid = generalData.getConfig().getString("all_anchors." + anchorID + ".owner");
            p = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
            userData = new PerUserStorage(plugin, p);
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

    /**
     * Returns if an anchor is registered and not from creative.
     * @param plugin
     * @param location
     * @return boolean
     */
    public static boolean anchorIsRegistered(AnchorSell plugin, Location location) {
        generalData = new GeneralStorage(plugin);
        return generalData.getConfig().contains("all_anchors." + getAnchorUUID(location));
    }

    /**
     * Returns the level of an anchor.
     * @param plugin
     * @param location
     * @return int
     */
    public static int getAnchorLevel(AnchorSell plugin, Location location) {
        generalData = new GeneralStorage(plugin);
        // Getting the current anchor level
        int currentAnchorLevel = generalData.getConfig().getInt("all_anchors." + getAnchorUUID(location) + ".level");
        if(currentAnchorLevel == 0) {
            currentAnchorLevel = 1;
        }
        return currentAnchorLevel;
    }

    /**
     * Returns the anchor UUID
     * UUID: X_Y_Z
     *
     * @param location
     * @return String
     */
    public static String getAnchorUUID(Location location) {
        return (int)location.getX() + "_" + (int)location.getY() + "_" + (int)location.getZ();
    }

    /**
     * Determines if an anchor is from that user or not.
     * @param location
     * @param p
     * @param plugin
     * @return boolean
     */
    public static boolean isMyAnchor(Location location, Player p, AnchorSell plugin) {
        userData = new PerUserStorage(plugin, p);
        generalData = new GeneralStorage(plugin);

        try {
            Player actualPlayerAnchor = Bukkit.getPlayer(UUID.fromString(
                    generalData.getConfig().getString("all_anchors." + getAnchorUUID(location) + ".owner"))
            );
            return p.getUniqueId().toString().equals(actualPlayerAnchor.getUniqueId().toString());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns the total anchors of a player.
     * @param plugin
     * @param p
     * @return int
     */
    public static int getPlayerTotalAnchors(AnchorSell plugin, Player p) {
        return getUserData(plugin, p).getConfig().getInt("total");
    }

    /**
     * Returns the money return in minutes from a player.
     * @param plugin
     * @param p
     * @return
     */
    public static double getPlayerMoneyPerMinute(AnchorSell plugin, Player p) {
        userData = new PerUserStorage(plugin, p);
        double res = 0;
        for(int i = 1; i <= plugin.getConfig().getInt("total-anchors-user-can-have"); i++) {
            if(userData.getConfig().contains("anchors." + i)) {
                res += Utils.getMoneyPerMinute(userData.getConfig().getInt("anchors." + i + ".level"));
            }
        }
        return res;
    }

    /**
     * Prints the list of anchors of a user.
     * @param plugin
     * @param p
     * @param toSendBack
     * @throws InvalidConfigurationException
     */
    public static void getAnchorUserList(AnchorSell plugin, OfflinePlayer p, CommandSender toSendBack) throws InvalidConfigurationException {
        userData = new PerUserStorage(plugin, p);

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

    /**
     * Saves the anchor upgrade to the config.
     * @param plugin
     * @param location
     * @param p
     */
    public static void upgradeAnchor(AnchorSell plugin, Location location, Player p) {
        userData = new PerUserStorage(plugin, p);
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

    /**
     * Handles the change of the upgrade multiplier.
     * @param plugin
     * @param multiplier
     */
    public static void changeUpgradeMultiplier(AnchorSell plugin, int multiplier) {
        plugin.getConfig().set("anchor.upgrade-multiplier", multiplier);
        plugin.saveConfig();
    }

    /**
     * Handles the price change.
     * @param plugin
     * @param price
     */
    public static void changePrice(AnchorSell plugin, int price) {
        plugin.getConfig().set("anchor-value", price);
        plugin.saveConfig();
    }

    /**
     * Handles the change of the area that anchors cannot be placed nearby.
     * @param plugin
     * @param zone
     */
    public static void changeSafeZone(AnchorSell plugin, int zone) {
        plugin.getConfig().set("safe-anchor-area", zone);
        plugin.saveConfig();
    }

    /**
     * Handles the change of the total of anchors a user can have.
     * @param plugin
     * @param n
     */
    public static void changeTotalAnchorsUserCanHave(AnchorSell plugin, int n) {
        plugin.getConfig().set("total-anchors-user-can-have", n);
        plugin.saveConfig();
    }

    /**
     * Revalidates all the anchors on the server.
     * Revalidates means that it checks if the anchor is still there in the world.
     * If not, it removes it from the config.
     * @param plugin
     */
    public static void revalidateAll(AnchorSell plugin) {
        for(Player p: Bukkit.getOnlinePlayers()) {
            revalidateUser(plugin, p);
        }
    }

    /**
     * Same as revalidateAll but now from a specific user.
     * @param plugin
     * @param p
     */
    public static void revalidateUser(AnchorSell plugin, OfflinePlayer p) {
        generalData = new GeneralStorage(plugin);
        userData = new PerUserStorage(plugin, p);
        int totalUserAnchors = 0;
        for(int i = 1; i <= plugin.getConfig().getInt("total-anchors-user-can-have"); i++) {
            if(userData.getConfig().contains("anchors." + i)) {
                Location loc;
                if(userData.getConfig().contains("anchors." + i + ".location.world")) {
                    loc = new Location(
                            Bukkit.getWorld(userData.getConfig().getString("anchors." + i + ".location.world")),
                            userData.getConfig().getInt("anchors." + i + ".location.x"),
                            userData.getConfig().getInt("anchors." + i + ".location.y"),
                            userData.getConfig().getInt("anchors." + i + ".location.z")
                    );
                } else {
                    loc = new Location(
                            Bukkit.getWorld("world"),
                            userData.getConfig().getInt("anchors." + i + ".location.x"),
                            userData.getConfig().getInt("anchors." + i + ".location.y"),
                            userData.getConfig().getInt("anchors." + i + ".location.z")
                    );
                }

                if(!loc.getBlock().getType().equals(Material.RESPAWN_ANCHOR)) {

                    System.out.println("[AnchorSell] Revalidation found an error: " +
                            "Player: " + p.getName() +
                            ". Anchor location: " + "[" +
                            loc.getBlockX() + ", " +
                            loc.getBlockY() + ", " +
                            loc.getBlockZ() + "]"
                    );

                    userData.getConfig().set("anchors." + i, null);
                    userData.getConfig().set("total", userData.getConfig().getInt("total") - 1);
                    if(generalData.getConfig().contains("all_anchors." + StorageManager.getAnchorUUID(loc))) {
                        generalData.getConfig().set("all_anchors." + StorageManager.getAnchorUUID(loc), null);
                    }
                    userData.saveConfig();
                    generalData.saveConfig();
                } else {
                    totalUserAnchors++;
                }
            }
        }

        /**
         * Total anchors the user has check.
         */
        if(userData.getConfig().getInt("total") != totalUserAnchors) {
            System.out.println("[AnchorSell] Revalidation found an error (total of anchors): " +
                    "Player: " + p.getName() +
                    ". Total rectified from " +
                    userData.getConfig().getInt("total") +
                    " to " +
                    totalUserAnchors
            );
            userData.getConfig().set("total", totalUserAnchors);
            userData.saveConfig();
        }
    }

    /**
     * Returns a HashMap of the anchor top.
     * Anchor TOP: Player name and the sum of all the anchors with theirs levels.
     *
     * @param plugin
     * @return
     */
    public static HashMap<String, Integer> getAnchorTop(AnchorSell plugin) {
        generalData = new GeneralStorage(plugin);
        ArrayList<String> ownersUUID = new ArrayList<>();
        ArrayList<Integer> levels = new ArrayList<>();
        ArrayList<String> ownersUUIDNotRepeated = new ArrayList<>();
        HashMap<String, Integer> top = new HashMap<>();

        // filtering from the general data
        for(String s: generalData.getConfig().getKeys(true)) {
            if(s.contains("owner")) {
                ownersUUID.add(generalData.getConfig().getString(s));
            }
            if(s.contains("level")) {
                levels.add(generalData.getConfig().getInt(s));
            }
        }

        // leaving one arraylist as the list of all owners not repeated
        for(String s: ownersUUID){
            if(!ownersUUIDNotRepeated.contains(s))
                ownersUUIDNotRepeated.add(s);
        }

        // computing the top
        for(String owner: ownersUUIDNotRepeated) {
            int sum = 0;
            for(int i = 0; i < ownersUUID.size(); i++) {
                if(owner.equals(ownersUUID.get(i))) {
                    sum += levels.get(i);
                }
            }
            top.put(owner, sum);
        }
        return Utils.sortHashMapByValue(top);
    }


    public static void cacheAllAnchors(AnchorSell plugin) {
        generalData = new GeneralStorage(plugin);
        Set<String> a = generalData.getConfig().getKeys(true);

        int x = -1, y = -1, z = -1;
        for(String str : a) {
            World world = null;

            if(StringUtils.countMatches(str, ".") == 1) {
                x = Integer.parseInt(StringUtils.split(str, "all_anchors.")[0]);
                y = Integer.parseInt(StringUtils.split(str, "all_anchors.")[1]);
                z = Integer.parseInt(StringUtils.split(str, "all_anchors.")[2]);
            }
            if(StringUtils.countMatches(str, ".world") == 1) {
                world = Bukkit.getServer().getWorld(generalData.getConfig().getString(str));
                Global.addAnchor(
                        new Location(
                                world,
                                x,
                                y,
                                z

                        ));
            }
        }
    }


    public static GeneralStorage getGeneralStorage() {
        return generalData;
    }
    public static PerUserStorage getUserData(AnchorSell plugin, Player p) {
        return new PerUserStorage(plugin, p);
    }



    public static PerUserStorage userData;
    public static GeneralStorage generalData;
}
