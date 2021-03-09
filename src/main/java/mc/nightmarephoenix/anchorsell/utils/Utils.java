package mc.nightmarephoenix.anchorsell.utils;

import mc.nightmarephoenix.anchorsell.AnchorSell;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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
}
