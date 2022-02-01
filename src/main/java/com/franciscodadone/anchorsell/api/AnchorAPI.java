package com.franciscodadone.anchorsell.api;

import com.franciscodadone.anchorsell.utils.Logger;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.franciscodadone.anchorsell.models.Anchor;
import com.franciscodadone.anchorsell.utils.Utils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import java.util.*;

public class AnchorAPI {

    /**
     * Handles the save of a new anchor being placed.
     *
     * @param anchor anchor to save
     * @return if it saved
     */
    public static boolean saveAnchor(Anchor anchor) {
        userData = new PerUserStorage(anchor.getOwner());
        generalData = new GeneralStorage();

        userData.getConfig().set("playerName", anchor.getOwner().getName());

        // Updating the total amount of anchors in the user config
        int totalUserAnchors = 0;
        if(userData.getConfig().contains("total")) {
            if(userData.getConfig().getInt("total") < Global.plugin.getConfig().getInt("total-anchors-user-can-have")) {
                totalUserAnchors = userData.getConfig().getInt("total");
                userData.getConfig().set("total", (totalUserAnchors + 1));
            } else return false;
        } else {
            userData.getConfig().set("total", (totalUserAnchors + 1));
        }

        // Anchor UUID
        String anchorID = getAnchorUUID(anchor.getLocation());

        // Determines what number the anchor can have
        for(int i = 1; i <= userData.getConfig().getInt("total"); i++) {
            if(!userData.getConfig().contains("anchors." + i)) {
                // Storing the data in the user file
                userData.getConfig().set("anchors." + i + ".location.x", anchor.getLocation().getX());
                userData.getConfig().set("anchors." + i + ".location.y", anchor.getLocation().getY());
                userData.getConfig().set("anchors." + i + ".location.z", anchor.getLocation().getZ());
                userData.getConfig().set("anchors." + i + ".location.world", Objects.requireNonNull(anchor.getLocation().getWorld()).getName());
                userData.getConfig().set("anchors." + i + ".level", anchor.getLevel());
                break;
            }
        }

        //Storing in the general file
        generalData.getConfig().set("all_anchors." + anchorID + ".location.x", anchor.getLocation().getX());
        generalData.getConfig().set("all_anchors." + anchorID + ".location.y", anchor.getLocation().getY());
        generalData.getConfig().set("all_anchors." + anchorID + ".location.z", anchor.getLocation().getZ());
        generalData.getConfig().set("all_anchors." + anchorID + ".location.world", Objects.requireNonNull(anchor.getLocation().getWorld()).getName());
        generalData.getConfig().set("all_anchors." + anchorID + ".owner", anchor.getOwner().getUniqueId().toString());
        generalData.getConfig().set("all_anchors." + anchorID + ".level", anchor.getLevel());

        // Saving to configs
        userData.saveConfig();
        userData.saveConfig();
        generalData.saveConfig();

        return true;
    }

    /**
     * Determines if a user can place more anchors based on the total it has.
     *
     * @param p player
     * @return boolean
     */
    public static boolean canPlaceMoreAnchors(Player p) {
        userData = new PerUserStorage(p);
        if(userData.getConfig().contains("total")) {
            return userData.getConfig().getInt("total") < Global.plugin.getConfig().getInt("total-anchors-user-can-have");
        }
        return true;
    }

    /**
     * Saves to the config the anchor break.
     *
     * @param anchor anchor to remove
     */
    public static void removeAnchor(Anchor anchor) {
        generalData = new GeneralStorage();

        // Anchor UUID
        String anchorID = getAnchorUUID(anchor.getLocation());

        // Figuring out who is the anchor owner
        OfflinePlayer p;
        try {
            String uuid = generalData.getConfig().getString("all_anchors." + anchorID + ".owner");
            assert uuid != null;
            p = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
            userData = new PerUserStorage(p);
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
        for(int i = 1; i <= Global.plugin.getConfig().getInt("total-anchors-user-can-have"); i++) {
            if(userData.getConfig().contains("anchors." + i)) {
                if(anchor.getLocation().getX() == userData.getConfig().getInt("anchors." + i + ".location.x") &&
                        anchor.getLocation().getY() == userData.getConfig().getInt("anchors." + i + ".location.y") &&
                        anchor.getLocation().getZ() == userData.getConfig().getInt("anchors." + i + ".location.z")) {
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
     * @param location anchor location
     * @return boolean
     */
    public static boolean isValidAnchor(Location location) {
        generalData = new GeneralStorage();
        return generalData.getConfig().contains("all_anchors." + getAnchorUUID(location));
    }

    /**
     * Returns the level of an anchor.
     * @param location level from an anchor in that location
     * @return int
     */
    public static int getAnchorLevel(Location location) {
        generalData = new GeneralStorage();
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
     * @param location anchor UUID is based in location
     * @return String
     */
    public static String getAnchorUUID(Location location) {
        return (int)location.getX() + "_" + (int)location.getY() + "_" + (int)location.getZ();
    }

    /**
     * Determines if an anchor is from that user or not.
     * @param anchor anchor
     * @param p player
     * @return boolean
     */
    public static boolean belongsToPlayer(Anchor anchor, Player p) {
        userData = new PerUserStorage(p);
        generalData = new GeneralStorage();
        try {
            Player actualPlayerAnchor = Bukkit.getPlayer(UUID.fromString(
                    Objects.requireNonNull(generalData.getConfig().getString("all_anchors." + getAnchorUUID(anchor.getLocation()) + ".owner")))
            );
            assert actualPlayerAnchor != null;
            return p.getUniqueId().toString().equals(actualPlayerAnchor.getUniqueId().toString());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns the total anchors of a player.
     * @param p player
     * @return int
     */
    public static int getPlayerTotalAnchors(OfflinePlayer p) {
        return getUserData(p).getConfig().getInt("total");
    }

    /**
     * Returns the money return in minutes from a player.
     * @param p player
     * @return money per minute
     */
    public static double getPlayerMoneyPerMinute(OfflinePlayer p) {
        userData = new PerUserStorage(p);
        double res = 0;
        for(int i = 1; i <= Global.plugin.getConfig().getInt("total-anchors-user-can-have"); i++) {
            if(userData.getConfig().contains("anchors." + i)) {
                res += Utils.getMoneyPerMinute(userData.getConfig().getInt("anchors." + i + ".level"));
            }
        }
        return res;
    }

    /**
     * Changes the level of an anchor in that location.
     * @param anchor
     * @param level
     * @return
     */
    public static boolean changeLevel(Anchor anchor, int level) {
        Location location = anchor.getLocation();
        if(level < 0 || level > 64) return false;
        assert anchor != null;
        userData = new PerUserStorage(anchor.getOwner());
        generalData = new GeneralStorage();

        // Updating user data...
        for(int i = 1; i <= Global.plugin.getConfig().getInt("total-anchors-user-can-have"); i++) {
            if(userData.getConfig().contains("anchors." + i)) {
                if(userData.getConfig().getInt("anchors." + i + ".location.x") == location.getX() &&
                        userData.getConfig().getInt("anchors." + i + ".location.y") == location.getY() &&
                        userData.getConfig().getInt("anchors." + i + ".location.z") == location.getZ()) {

                    userData.getConfig().set("anchors." + i + ".level", level);
                }
            }
        }

        // Updating general data...
        if(Objects.equals(generalData.getConfig().getString("all_anchors." + AnchorAPI.getAnchorUUID(location) + ".owner"), anchor.getOwner().getUniqueId().toString())) {
            generalData.getConfig().set("all_anchors." + AnchorAPI.getAnchorUUID(location) + ".level", level);
        }

        generalData.saveConfig();
        userData.saveConfig();
        return true;
    }

    /**
     * Revalidates all the anchors on the server.
     * Revalidates means that it checks if the anchor is still there in the world.
     * If not, it removes it from the config.
     */
    public static void revalidateAll() {
        for(Player p: Bukkit.getOnlinePlayers()) {
            revalidateUser(p);
        }
    }

    /**
     * Same as revalidateAll but now from a specific user.
     * @param p player
     */
    public static void revalidateUser(OfflinePlayer p) {
        generalData = new GeneralStorage();
        userData = new PerUserStorage(p);
        int totalUserAnchors = 0;
        for(int i = 1; i <= Global.plugin.getConfig().getInt("total-anchors-user-can-have"); i++) {
            if(userData.getConfig().contains("anchors." + i)) {
                Location loc;
                if(userData.getConfig().contains("anchors." + i + ".location.world")) {
                    loc = new Location(
                            Bukkit.getWorld(Objects.requireNonNull(userData.getConfig().getString("anchors." + i + ".location.world"))),
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

                    Logger.severe("Revalidation found an error: " +
                            "Player: " + p.getName() +
                            ". Anchor location: " + "[" +
                            loc.getBlockX() + ", " +
                            loc.getBlockY() + ", " +
                            loc.getBlockZ() + "]"
                    );

                    userData.getConfig().set("anchors." + i, null);
                    userData.getConfig().set("total", userData.getConfig().getInt("total") - 1);
                    if(generalData.getConfig().contains("all_anchors." + AnchorAPI.getAnchorUUID(loc))) {
                        generalData.getConfig().set("all_anchors." + AnchorAPI.getAnchorUUID(loc), null);
                    }
                    userData.saveConfig();
                    generalData.saveConfig();
                } else {
                    totalUserAnchors++;
                }
            }
        }

        // Total anchors the user has check.
        if(userData.getConfig().getInt("total") != totalUserAnchors) {
            Logger.severe("Revalidation found an error (total of anchors): " +
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
     * @return anchor top hash map sorted
     */
    public static HashMap<String, Integer> getAnchorTop() {
        generalData = new GeneralStorage();
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

    /**
     * Returns the anchor in that location.
     */
    public static Anchor getAnchorFromLoc(Location location) {
        for(Anchor anchor : Global.getAllAnchors()) {
            if(anchor.getLocation().equals(location)) {
                return anchor;
            }
        }
        return null;
    }

    /**
     * Returns all player anchors.
     */
    public static ArrayList<Anchor> getAllPlayerAnchors(OfflinePlayer player) {
        FileConfiguration data = getUserData(player).getConfig();
        ArrayList<Anchor> anchors = new ArrayList<>();

        for(int i = 1; i <= Global.plugin.getConfig().getInt("total-anchors-user-can-have"); i++) {
            if(data.contains("anchors." + i)) {
                anchors.add(new Anchor(
                        data.getInt("anchors." + i + ".level"),
                        new Location(
                                Bukkit.getServer().getWorld(Objects.requireNonNull(data.getString("anchors." + i + ".location.world"))),
                                data.getInt("anchors." + i + ".location.x"),
                                data.getInt("anchors." + i + ".location.y"),
                                data.getInt("anchors." + i + ".location.z")
                        ),
                        null,
                        data.getString("playerName")
                ));
            }
        }
        return anchors;
    }

    /**
     * @return Total anchors placed on the server.
     */
    public static int getTotalAnchorsPlaced() {
        generalData = new GeneralStorage();
        return generalData.getConfig().getConfigurationSection("all_anchors").getKeys(false).size();
    }

    private static PerUserStorage getUserData(OfflinePlayer p) {
        return new PerUserStorage(p);
    }

    private static PerUserStorage userData;
    private static GeneralStorage generalData;
}
