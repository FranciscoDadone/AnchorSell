package mc.nightmarephoenix.anchorsell.utils;

import org.bukkit.ChatColor;

public class Utils {
    public static String Color(String str) {
        return ChatColor.translateAlternateColorCodes('§', str.replace("&", "§"));
    }

    public static int getMoneyPerSecond(int anchorLevel) {
        return (int) Math.round(0.1 * anchorLevel + Math.pow(anchorLevel, 0.8));
    }

    public static int getMoneyPerMinute(int anchorLevel) {
        return (int) getMoneyPerSecond(anchorLevel) * 60;
    }
}
