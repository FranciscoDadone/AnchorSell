package mc.nightmarephoenix.anchorsell.utils;

import org.bukkit.Bukkit;

public class Logger {

    public static void info(String str) {
        Bukkit.getConsoleSender().sendMessage(prefix + Utils.Color(str));
    }

    public static void severe(String str) {
        Bukkit.getConsoleSender().sendMessage(prefix + Utils.Color("&4" + str));
    }

    public static void warning(String str) {
        Bukkit.getConsoleSender().sendMessage(prefix + Utils.Color("&e" + str));
    }

    private static String prefix = Utils.Color("&7[&5AnchorSell&7]&f ");

}
