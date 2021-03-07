package mc.nightmarephoenix.anchorsell.utils;

import org.bukkit.ChatColor;

public class Utils {
    public static String Color(String str) {
        return ChatColor.translateAlternateColorCodes('§', str.replace("&", "§"));
    }
}
