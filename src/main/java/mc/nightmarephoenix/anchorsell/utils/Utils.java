package mc.nightmarephoenix.anchorsell.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class Utils {
    public static String Color(String str) {
        return ChatColor.translateAlternateColorCodes('§', str.replace("&", "§"));
    }

    public static ItemStack getAnchor(int level) {
        ItemStack item = new ItemStack(Material.RESPAWN_ANCHOR, 1);
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

    public static int getMoneyPerSecond(int anchorLevel) {
        return (int) Math.round(0.1 * anchorLevel + Math.pow(anchorLevel, 0.8));
    }

    public static int getMoneyPerMinute(int anchorLevel) {
        return getMoneyPerSecond(anchorLevel) * 60;
    }
}
