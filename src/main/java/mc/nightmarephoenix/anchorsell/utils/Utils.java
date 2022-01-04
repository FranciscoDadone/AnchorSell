package mc.nightmarephoenix.anchorsell.utils;

import com.tchristofferson.configupdater.ConfigUpdater;
import mc.nightmarephoenix.anchorsell.models.Anchor;
import mc.nightmarephoenix.anchorsell.thirdparty.vault.EconomyManager;
import mc.nightmarephoenix.anchorsell.api.Global;
import mc.nightmarephoenix.anchorsell.api.StorageManager;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Utils {

    /**
     * Translates the color codes.
     * @param str string
     * @return formatted
     */
    public static String Color(String str) {
        return ChatColor.translateAlternateColorCodes('§', str.replace("&", "§"));
    }

    /**
     * Same as above but with a list.
     * @param strList string list
     * @return formatted
     */
    public static List<String> Color(List<String> strList) {
        for(String string: strList) {
            strList.set(strList.indexOf(string), Color(string));
        }
        return strList;
    }

    /**
     * Returns a new anchor with a given level and the quantity.
     * @param level anchor level
     * @param quantity quantity
     * @return ItemStack
     */
    public static ItemStack getAnchor(int level, int quantity) {
        ItemStack item = new ItemStack(Material.RESPAWN_ANCHOR, quantity);
        ArrayList<String> Lore = new ArrayList<>();
        Lore.add("");
        Lore.add(Utils.Color("&7&m----------------------------"));
        Lore.add(Utils.Color("&fAnchor level: &e" + level));
        Lore.add(Utils.Color("&fMoney per minute: &e" + Utils.getMoneyPerMinute(level)));
        Lore.add(Utils.Color("&7&m----------------------------"));
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(Utils.Color("&5&lAnchor"));
        meta.setLore(Lore);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Returns the anchor level but with the ore.
     * @param level anchor level
     * @return ore level string
     */
    public static String getAnchorOreLevelString(int level) {
        if(level > 64)
            return Color(Objects.requireNonNull(Global.plugin.getConfig().getString("levels.maxed-out-level")));
        else if(level < 16)
            return Color(Objects.requireNonNull(Global.plugin.getConfig().getString("levels.1")));
        else if(level < 24)
            return Color(Objects.requireNonNull(Global.plugin.getConfig().getString("levels.2")));
        else if(level < 32)
            return Color(Objects.requireNonNull(Global.plugin.getConfig().getString("levels.3")));
        else if(level < 48)
            return Color(Objects.requireNonNull(Global.plugin.getConfig().getString("levels.4")));
        else return Color(Objects.requireNonNull(Global.plugin.getConfig().getString("levels.5")));
    }

    /**
     * Anchor level translated to ores.
     * @param level anchor level
     * @return level material
     */
    public static Material getAnchorOreLevel(int level) {
        if(level < 16)
            return Material.COAL;
        else if(level < 24)
            return Material.IRON_INGOT;
        else if(level < 32)
            return Material.GOLD_INGOT;
        else if(level < 48)
            return Material.DIAMOND;
        else if(level <= 64)
            return Material.NETHERITE_INGOT;
        else
            return null;
    }

    /**
     * Money per minute with a given anchor level.
     * @param anchorLevel level
     * @return double
     */
    public static double getMoneyPerMinute(int anchorLevel) {
        return Math.round((0.1 * anchorLevel + Math.pow(anchorLevel, 0.8)) * 60) * Global.plugin.getConfig().getDouble("anchor.pay-modifier");
    }

    /**
     * Money to the next upgrade.
     * @param anchorLevel level
     * @return double
     */
    public static double getMoneyToUpgrade(int anchorLevel) {
        return getMoneyPerMinute(anchorLevel + 1) * 60 * Global.plugin.getConfig().getInt("anchor.upgrade-multiplier"); // the money that gives the next level multiplied by 16hs
    }

    /**
     * Gets the lore of a given anchor.
     * @param location anchor location
     * @param player player
     * @return lore
     */
    public static List<String> getLore(String path, Location location, Player player) {
        List<String> res = new ArrayList<>();
        for(String str: Utils.Color(Global.plugin.getConfig().getStringList(path))) {
            int level = StorageManager.getAnchorLevel(location);
            String levelToUpgrade = String.valueOf(level + 1);
            String priceOfUpgrade = String.valueOf(Utils.getMoneyToUpgrade(level));
            if((level + 1) > 64) {
                levelToUpgrade = "";
                priceOfUpgrade = "-";
            }

            res.add(str.replaceAll("%level%", String.valueOf(level)).
                    replaceAll("%moneyPer15Minutes%", String.valueOf(Utils.getMoneyPerMinute(level) * 15)).
                    replaceAll("%moneyPerMinute%", String.valueOf(Utils.getMoneyPerMinute(level))).
                    replaceAll("%oreLevel%", Utils.getAnchorOreLevelString(level)).
                    replaceAll("%playerBalance%", String.valueOf(EconomyManager.getEconomy().getBalance(player))).
                    replaceAll("%playerAnchors%", String.valueOf(StorageManager.getPlayerTotalAnchors(player))).
                    replaceAll("%maxPlayerAnchors%", String.valueOf(Global.plugin.getConfig().getInt("total-anchors-user-can-have"))).
                    replaceAll("%playerMoneyPer15Minutes%", String.valueOf(StorageManager.getPlayerMoneyPerMinute(player) * 15)).
                    replaceAll("%priceOfUpgrade%", priceOfUpgrade).
                    replaceAll("%nextLevel%", levelToUpgrade).
                    replaceAll("%nextLevelOre%", Utils.getAnchorOreLevelString(level + 1)));
        }
        return res;
    }

    /**
     * Gets the lore of an anchor but without the player.
     * @param location anchor location
     * @return lore
     */
    public static List<String> getLore(String path, Location location) {
        List<String> res = new ArrayList<>();
        for(String str: Utils.Color(Global.plugin.getConfig().getStringList(path))) {
            int level = StorageManager.getAnchorLevel(location);
            String levelToUpgrade = String.valueOf(level + 1);
            String priceOfUpgrade = String.valueOf(Utils.getMoneyToUpgrade(level));
            if((level + 1) > 64) {
                levelToUpgrade = "";
                priceOfUpgrade = "-";
            }
            res.add(str.replaceAll("%level%", String.valueOf(level)).
                    replaceAll("%moneyPer15Minutes%", String.valueOf(Utils.getMoneyPerMinute(level) * 15)).
                    replaceAll("%moneyPerMinute%", String.valueOf(Utils.getMoneyPerMinute(level))).
                    replaceAll("%oreLevel%", Utils.getAnchorOreLevelString(level)).
                    replaceAll("%maxPlayerAnchors%", String.valueOf(Global.plugin.getConfig().getInt("total-anchors-user-can-have"))).
                    replaceAll("%priceOfUpgrade%", priceOfUpgrade).
                    replaceAll("%nextLevel%", levelToUpgrade).
                    replaceAll("%nextLevelOre%", Utils.getAnchorOreLevelString(level + 1)));
        }
        return res;
    }

    /**
     * Creates a determined item.
     * @param name item name
     * @param material item material
     * @param enchanted enchanted?
     * @return ItemStack
     */
    public static ItemStack createItem(String name, Material material, boolean enchanted) {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(name);
        if(enchanted)
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Creates a item with lore.
     * @param name item name
     * @param material item material
     * @param lore item lore
     * @param enchanted enchanted?
     * @return ItemStack
     */
    public static ItemStack createItem(String name, Material material, List<String> lore, boolean enchanted) {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(name);
        meta.setLore(lore);
        if(enchanted)
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Sort hashmap by values (TOP)
     * @param hm hash map
     * @return hash map sorted
     */
    public static HashMap<String, Integer> sortHashMapByValue(HashMap<String, Integer> hm) {
        // Create a list from elements of HashMap
        List<Map.Entry<String, Integer> > list =
                new LinkedList<>(hm.entrySet());

        // Sort the list
        list.sort(Map.Entry.comparingByValue());

        // put data from sorted list to hashmap
        HashMap<String, Integer> temp = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    /**
     * Sends console message.
     * @param msg message
     */
    public static void sendMessage(String msg) {
        Bukkit.getConsoleSender().sendMessage(Utils.Color("&5&lAnchorSell &f - " + msg));
    }

    public static void sendConfigMessageF(String str, String toBeReplaced, String toReplace, CommandSender sender) {
        try {
            sender.sendMessage(Color(Objects.requireNonNull(Global.plugin.getConfig().getString(str)).replaceAll(toBeReplaced, toReplace)));
        } catch (Exception e) {
            sendMessage("Check for config updates: &ahttps://github.com/FranciscoDadone/AnchorSell/blob/main/src/main/resources/config.yml");
        }
    }

    public static void sendConfigMessage(String str, CommandSender sender) {
        try {
            sender.sendMessage(Color(Objects.requireNonNull(Global.plugin.getConfig().getString(str))));
        } catch (Exception e) {
            sendMessage("Check for config updates: &ahttps://github.com/FranciscoDadone/AnchorSell/blob/main/src/main/resources/config.yml");
        }
    }

    public static void sendConfigMultilineMessage(String message, CommandSender sender) {
        try {
            for(String line: Global.plugin.getConfig().getStringList(message)) {
                sender.sendMessage(Utils.Color(line));
            }
        } catch (Exception e) {
            sendMessage("Check for config updates: &ahttps://github.com/FranciscoDadone/AnchorSell/blob/main/src/main/resources/config.yml");
        }
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static void noPermission(String permissionNode, CommandSender sender) {
        sendConfigMessageF("no-permissions", "%permissionNode%", permissionNode, sender);
    }


    public static void setConfigValue(String path, String value) {
        Global.plugin.getConfig().set(path, value);
        Global.plugin.saveConfig();
        try {
            ConfigUpdater.update(Global.plugin, "config.yml", new File(Global.plugin.getDataFolder(), "config.yml"), List.of());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setConfigValue(String path, int value) {
        Global.plugin.getConfig().set(path, value);
        Global.plugin.saveConfig();
        try {
            ConfigUpdater.update(Global.plugin, "config.yml", new File(Global.plugin.getDataFolder(), "config.yml"), List.of());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prints the list of anchors of a user.
     * @param p player
     * @param toSendBack sender
     * @throws InvalidConfigurationException ex
     */
    public static boolean printAnchorUserList(OfflinePlayer p, CommandSender toSendBack) throws InvalidConfigurationException {
        boolean userFound = false;

        toSendBack.sendMessage(Utils.Color(Objects.requireNonNull(Global.plugin.getConfig().getString("anchor.list.first-message"))));

        for(Anchor playerAnchor : StorageManager.getAllPlayerAnchors(p)) {
            userFound = true;
            Utils.Color(Global.plugin.getConfig().getStringList("anchor.list.message")).forEach((str) -> toSendBack.sendMessage(
                    str.replaceAll("%location%", "X: " + playerAnchor.getLocation().getX()
                                    + " Y: " + playerAnchor.getLocation().getY()
                                    + " Z: " + playerAnchor.getLocation().getZ())
                            .replaceAll("%level%", String.valueOf(playerAnchor.getLevel()))
            ));
        }
        toSendBack.sendMessage(Utils.Color(Objects.requireNonNull(Global.plugin.getConfig().getString("anchor.list.last-message"))));
        return userFound;
    }

}
