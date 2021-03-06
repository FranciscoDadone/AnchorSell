package mc.nightmarephoenix.anchorsell.vault;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

public class Vault {

    public Vault() {
        console = Bukkit.getServer().getConsoleSender();
    }

    public static void ecoGive(String playername, double amount) {
        Bukkit.dispatchCommand(console, "eco give " + playername + " " + amount);
    }

    public static void ecoTake(String playername, double amount) {
        Bukkit.dispatchCommand(console, "eco give " + playername + " " + amount);
    }


    private static ConsoleCommandSender console;
}
