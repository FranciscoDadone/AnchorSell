package mc.nightmarephoenix.anchorsell.utils;

import mc.nightmarephoenix.anchorsell.AnchorSell;
import mc.nightmarephoenix.anchorsell.economy.EconomyManager;
import mc.nightmarephoenix.anchorsell.storage.StorageManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static String Color(String str) {
        return ChatColor.translateAlternateColorCodes('§', str.replace("&", "§"));
    }

    public static List<String> Color(List<String> strList) {
        for(String string: strList) {
            strList.set(strList.indexOf(string), Color(string));
        }
        return strList;
    }

    public static ItemStack getAnchor(int level, int quantity) {
        ItemStack item = new ItemStack(Material.RESPAWN_ANCHOR, quantity);
        ArrayList<String> Lore = new ArrayList<String>();
        Lore.add("");
        Lore.add(Utils.Color("&7&m----------------------------"));
        Lore.add(Utils.Color("&fAnchor level: &e" + level));
        Lore.add(Utils.Color("&fMoney per minute: &e" + Utils.getMoneyPerMinute(level)));
        Lore.add(Utils.Color("&7&m----------------------------"));
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Utils.Color("&5&lAnchor"));
        meta.setLore(Lore);
        item.setItemMeta(meta);
        return item;
    }

    public static String getAnchorOreLevelString(AnchorSell plugin, int level) {
        if(level > 64)
            return Color(plugin.getConfig().getString("levels.maxed-out-level"));
        if(level > 0 && level < 16)
            return Color(plugin.getConfig().getString("levels.1"));
        else if(level >= 16 && level < 24)
            return Color(plugin.getConfig().getString("levels.2"));
        else if(level >= 24 && level < 32)
            return Color(plugin.getConfig().getString("levels.3"));
        else if(level >= 32 && level < 48)
            return Color(plugin.getConfig().getString("levels.4"));
        else if(level >= 32 && level <= 64)
            return Color(plugin.getConfig().getString("levels.5"));
        else
            return null;
    }

    public static Material getAnchorOreLevel(int level) {
        if(level > 0 && level < 16)
            return Material.COAL;
        else if(level >= 16 && level < 24)
            return Material.IRON_INGOT;
        else if(level >= 24 && level < 32)
            return Material.GOLD_INGOT;
        else if(level >= 32 && level < 48)
            return Material.DIAMOND;
        else if(level >= 32 && level <= 64)
            return Material.NETHERITE_INGOT;
        else
            return null;
    }

    public static double getMoneyPerSecond(int anchorLevel) {
        return Math.round(0.1 * anchorLevel + Math.pow(anchorLevel, 0.8));
    }

    public static double getMoneyPerMinute(int anchorLevel) {
        return getMoneyPerSecond(anchorLevel) * 60;
    }

    public static double getMoneyToUpgrade(int anchorLevel) {
        return getMoneyPerMinute(anchorLevel + 1) * 60 * 16; // the money that gives the next level multiplied by 16hs
    }

    public static  List<String> getLore(String path, AnchorSell plugin, Location location, Player player, boolean displayID) {
        List<String> res = new ArrayList<>();
        for(String str: Utils.Color(plugin.getConfig().getStringList(path))) {
            int level = StorageManager.getAnchorLevel(plugin, location);
            String levelToUpgrade = String.valueOf(level + 1);
            String priceOfUpgrade = String.valueOf(Utils.getMoneyToUpgrade(level));
            String formatted = str;
            if((level + 1) > 64) {
                levelToUpgrade = "";
                priceOfUpgrade = "-";
            }

            if(displayID) {
                res.add(str.replaceAll("%level%", String.valueOf(level)).
                        replaceAll("%moneyPer15Minutes%", String.valueOf(Utils.getMoneyPerMinute(level) * 15)).
                        replaceAll("%moneyPerMinute%", String.valueOf(Utils.getMoneyPerMinute(level))).
                        replaceAll("%oreLevel%", Utils.getAnchorOreLevelString(plugin, level)).
                        replaceAll("%playerBalance%", String.valueOf(EconomyManager.getEconomy().getBalance(player))).
                        replaceAll("%playerAnchors%", String.valueOf(StorageManager.getPlayerTotalAnchors(plugin, player))).
                        replaceAll("%maxPlayerAnchors%", String.valueOf(plugin.getConfig().getInt("total-anchors-user-can-have"))).
                        replaceAll("%playerMoneyPer15Minutes%", String.valueOf(StorageManager.getPlayerMoneyPerMinute(plugin, player) * 15)).
                        replaceAll("%priceOfUpgrade%", priceOfUpgrade).
                        replaceAll("%nextLevel%", levelToUpgrade).
                        replaceAll("%nextLevelOre%", Utils.getAnchorOreLevelString(plugin, level + 1)).
                        replaceAll("%id%", StorageManager.getAnchorUUID(location)));
            } else {
                res.add(str.replaceAll("%level%", String.valueOf(level)).
                        replaceAll("%moneyPer15Minutes%", String.valueOf(Utils.getMoneyPerMinute(level) * 15)).
                        replaceAll("%moneyPerMinute%", String.valueOf(Utils.getMoneyPerMinute(level))).
                        replaceAll("%oreLevel%", Utils.getAnchorOreLevelString(plugin, level)).
                        replaceAll("%playerBalance%", String.valueOf(EconomyManager.getEconomy().getBalance(player))).
                        replaceAll("%playerAnchors%", String.valueOf(StorageManager.getPlayerTotalAnchors(plugin, player))).
                        replaceAll("%maxPlayerAnchors%", String.valueOf(plugin.getConfig().getInt("total-anchors-user-can-have"))).
                        replaceAll("%playerMoneyPer15Minutes%", String.valueOf(StorageManager.getPlayerMoneyPerMinute(plugin, player) * 15)).
                        replaceAll("%priceOfUpgrade%", priceOfUpgrade).
                        replaceAll("%nextLevel%", levelToUpgrade).
                        replaceAll("%nextLevelOre%", Utils.getAnchorOreLevelString(plugin, level + 1)));
            }
        }
        return res;
    }

    public static  List<String> getLore(String path, AnchorSell plugin, Location location) {
        List<String> res = new ArrayList<>();
        for(String str: Utils.Color(plugin.getConfig().getStringList(path))) {
            int level = StorageManager.getAnchorLevel(plugin, location);
            String levelToUpgrade = String.valueOf(level + 1);
            String priceOfUpgrade = String.valueOf(Utils.getMoneyToUpgrade(level));
            if((level + 1) > 64) {
                levelToUpgrade = "";
                priceOfUpgrade = "-";
            }
            res.add(str.replaceAll("%level%", String.valueOf(level)).
                    replaceAll("%moneyPer15Minutes%", String.valueOf(Utils.getMoneyPerMinute(level) * 15)).
                    replaceAll("%moneyPerMinute%", String.valueOf(Utils.getMoneyPerMinute(level))).
                    replaceAll("%oreLevel%", Utils.getAnchorOreLevelString(plugin, level)).
                    replaceAll("%maxPlayerAnchors%", String.valueOf(plugin.getConfig().getInt("total-anchors-user-can-have"))).
                    replaceAll("%priceOfUpgrade%", priceOfUpgrade).
                    replaceAll("%nextLevel%", levelToUpgrade).
                    replaceAll("%nextLevelOre%", Utils.getAnchorOreLevelString(plugin, level + 1)));
        }
        return res;
    }

    public static ItemStack createItem(String name, Material material, boolean enchanted) {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        if(enchanted)
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createItem(String name, Material material, List<String> lore, boolean enchanted) {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        if(enchanted)
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
        item.setItemMeta(meta);
        return item;
    }

}
