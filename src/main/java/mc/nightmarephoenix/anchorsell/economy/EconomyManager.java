package mc.nightmarephoenix.anchorsell.economy;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class EconomyManager {

    public static boolean setupEconomy() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static boolean withdrawFromUser(Player p, double balance) {
        EconomyResponse res = EconomyManager.getEconomy().withdrawPlayer(p, balance);

        if (!res.equals(EconomyResponse.ResponseType.SUCCESS)) {
            return true;
        } else {
            return false;
        }
    }



    public static Economy getEconomy() {
        return econ;
    }


    private static Economy econ;

}
