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

    public static ItemStack getAnchor(String name, ArrayList<String> lore) {
        ItemStack item = new ItemStack(Material.RESPAWN_ANCHOR, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Utils.Color(name));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
}
