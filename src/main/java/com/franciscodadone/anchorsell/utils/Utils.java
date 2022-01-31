package com.franciscodadone.anchorsell.utils;

import com.franciscodadone.anchorsell.api.Global;
import com.franciscodadone.anchorsell.api.StorageManager;
import com.franciscodadone.anchorsell.models.Anchor;
import com.franciscodadone.anchorsell.thirdparty.vault.EconomyManager;
import com.tchristofferson.configupdater.ConfigUpdater;
import com.franciscodadone.anchorsell.tasks.PayTask;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    private static String translateHexColorCodes(String message) {
        final char COLOR_CHAR = ChatColor.COLOR_CHAR;
        message = message.replace("{#", "#");
        final Pattern hexPattern = Pattern.compile("#([A-Fa-f0-9]{6})}");
        Matcher matcher = hexPattern.matcher(message);
        StringBuffer buffer = new StringBuffer(message.length() + 4 * 8);
        while (matcher.find())
        {
            String group = matcher.group(1);
            matcher.appendReplacement(buffer, COLOR_CHAR + "x"
                    + COLOR_CHAR + group.charAt(0) + COLOR_CHAR + group.charAt(1)
                    + COLOR_CHAR + group.charAt(2) + COLOR_CHAR + group.charAt(3)
                    + COLOR_CHAR + group.charAt(4) + COLOR_CHAR + group.charAt(5)
            );
        }
        return matcher.appendTail(buffer).toString();
    }

    /**
     * Translates the color codes.
     * @param str string
     * @return formatted
     */
    public static String Color(String str) {
        return translateHexColorCodes(ChatColor.translateAlternateColorCodes('§', str.replace("&", "§")));
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
//        Lore.add("");
        Lore.add(Utils.Color("&7&m----------------------------"));
        Lore.add(Utils.Color(Global.plugin.getConfig().getString("anchor-item.lore.level-line") + level));
        Global.plugin.getConfig().getStringList("anchor-item.lore.other-lines").forEach(line -> {
            Lore.add(Utils.Color(line.
                    replaceAll("%level%", String.valueOf(level)).
                    replaceAll("%moneyPer15Minutes%", String.valueOf(Utils.getMoneyPerMinute(level) * 15)).
                    replaceAll("%moneyPerMinute%", String.valueOf(Utils.getMoneyPerMinute(level))).
                    replaceAll("%oreLevel%", Utils.getAnchorOreLevelString(level)).
                    replaceAll("%maxPlayerAnchors%", String.valueOf(Global.plugin.getConfig().getInt("total-anchors-user-can-have"))).
                    replaceAll("%nextLevelOre%", Utils.getAnchorOreLevelString(level + 1))));
        });
        Lore.add(Utils.Color("&7&m----------------------------"));
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(Utils.Color(Global.plugin.getConfig().getString("anchor-item.name")));
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
    public static List<String> getLore(String path, Location location, OfflinePlayer player) {
        List<String> res = new ArrayList<>();
        for(String str: Utils.Color(Global.plugin.getConfig().getStringList(path))) {
            int level = StorageManager.getAnchorLevel(location);
            String levelToUpgrade = String.valueOf(level + 1);
            String priceOfUpgrade = String.valueOf(new DecimalFormat("0.00").format(Utils.getMoneyToUpgrade(level)));
            if((level + 1) > 64) {
                levelToUpgrade = "";
                priceOfUpgrade = "-";
            }

            long nextPaymentTimer = Global.plugin.getConfig().getInt("pay-timer-in-minutes") - ((System.currentTimeMillis() - PayTask.getLastUserPayment()) / 1000) / 60;

            res.add(str.replaceAll("%level%", String.valueOf(level)).
                    replaceAll("%moneyPer15Minutes%", String.valueOf(new DecimalFormat("0.00").format(Utils.getMoneyPerMinute(level) * 15))).
                    replaceAll("%moneyPerMinute%", String.valueOf(new DecimalFormat("0.00").format(Utils.getMoneyPerMinute(level)))).
                    replaceAll("%oreLevel%", Utils.getAnchorOreLevelString(level)).
                    replaceAll("%playerBalance%", String.valueOf(new DecimalFormat("0.00").format(EconomyManager.getEconomy().getBalance(player)))).
                    replaceAll("%playerAnchors%", String.valueOf(StorageManager.getPlayerTotalAnchors(player))).
                    replaceAll("%maxPlayerAnchors%", String.valueOf(Global.plugin.getConfig().getInt("total-anchors-user-can-have"))).
                    replaceAll("%playerMoneyPer15Minutes%", String.valueOf(new DecimalFormat("0.00").format(StorageManager.getPlayerMoneyPerMinute(player) * 15))).
                    replaceAll("%priceOfUpgrade%", priceOfUpgrade).
                    replaceAll("%nextLevel%", levelToUpgrade).
                    replaceAll("%nextLevelOre%", Utils.getAnchorOreLevelString(level + 1)).
                    replaceAll("%timer%", String.valueOf(nextPaymentTimer)));
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
            String priceOfUpgrade = String.valueOf(new DecimalFormat("0.00").format(Utils.getMoneyToUpgrade(level)));
            if((level + 1) > 64) {
                levelToUpgrade = "";
                priceOfUpgrade = "-";
            }
            res.add(str.replaceAll("%level%", String.valueOf(level)).
                    replaceAll("%moneyPer15Minutes%", String.valueOf(new DecimalFormat("0.00").format(Utils.getMoneyPerMinute(level) * 15))).
                    replaceAll("%moneyPerMinute%", String.valueOf(new DecimalFormat("0.00").format(Utils.getMoneyPerMinute(level)))).
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
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
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
        if(enchanted)
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
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
     * Sends config message and requires strings to replace
     * @param str main string
     * @param toBeReplaced string to be replaced (placeholder)
     * @param toReplace string to replace
     * @param sender command sender
     */
    public static void sendConfigMessageF(String str, String toBeReplaced, String toReplace, CommandSender sender) {
        try {
            sender.sendMessage(Color(Objects.requireNonNull(Global.plugin.getConfig().getString(str)).replaceAll(toBeReplaced, toReplace)));
        } catch (Exception e) {
            Logger.severe("Check for config updates: &ahttps://github.com/FranciscoDadone/AnchorSell/blob/main/src/main/resources/config.yml");
        }
    }

    /**
     * Sends a config message
     * @param path path in config
     * @param sender command sender
     */
    public static void sendConfigMessage(String path, CommandSender sender) {
        try {
            sender.sendMessage(Color(Objects.requireNonNull(Global.plugin.getConfig().getString(path))));
        } catch (Exception e) {
            Logger.severe("Check for config updates: &ahttps://github.com/FranciscoDadone/AnchorSell/blob/main/src/main/resources/config.yml");
        }
    }

    /**
     * Sends a config message (multiline)
     * @param path path in config
     * @param sender command sender
     */
    public static void sendConfigMultilineMessage(String path, CommandSender sender) {
        try {
            for(String line: Global.plugin.getConfig().getStringList(path)) {
                sender.sendMessage(Utils.Color(line));
            }
        } catch (Exception e) {
            Logger.severe("Check for config updates: &ahttps://github.com/FranciscoDadone/AnchorSell/blob/main/src/main/resources/config.yml");
        }
    }

    /**
     * If a string is numeric
     * @param strNum string to determine
     * @return boolean
     */
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

    /**
     * No permission error
     * @param permissionNode permission
     * @param sender command sender
     */
    public static void noPermission(String permissionNode, CommandSender sender) {
        sendConfigMessageF("no-permissions", "%permissionNode%", permissionNode, sender);
    }

    /**
     * Sets a config value
     * @param path path in config
     * @param value value to set
     */
    public static void setConfigValue(String path, String value) {
        Global.plugin.getConfig().set(path, value);
        Global.plugin.saveConfig();
        try {
            ConfigUpdater.update(Global.plugin, "config.yml", new File(Global.plugin.getDataFolder(), "config.yml"), Collections.emptyList());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets a config value
     * @param path path in config
     * @param value value to set
     */
    public static void setConfigValue(String path, int value) {
        Global.plugin.getConfig().set(path, value);
        Global.plugin.saveConfig();
        try {
            ConfigUpdater.update(Global.plugin, "config.yml", new File(Global.plugin.getDataFolder(), "config.yml"), Collections.emptyList());
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

    public static int getAnchorCharges(int level) {
        if(level < 16)
            return 0;
        else if(level < 24)
            return 1;
        else if(level < 32)
            return 2;
        else if(level < 48)
            return 3;
        else if(level <= 64)
            return 4;
        else
            return 0;
    }

    public static Set<Material> getInvisibleBlockList() {
        Set<Material> invisibleBlocksSet = new HashSet<>();
        for(Material material : Material.values()) {
            if(!material.isOccluding()) {
                invisibleBlocksSet.add(material);
            }
        }
        return invisibleBlocksSet;
    }
}
