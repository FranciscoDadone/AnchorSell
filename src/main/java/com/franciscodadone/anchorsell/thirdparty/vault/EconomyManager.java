package com.franciscodadone.anchorsell.thirdparty.vault;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class EconomyManager {

    /**
     * Sets up Vault hook.
     * @return if active
     */
    public static boolean setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) return false;
        econ = rsp.getProvider();
        return true;
    }

    /**
     * Withdraw money from user.
     * @param p player to withdraw money
     * @param toWithdraw quantity
     * @return if the operation succeeded.
     */
    public static boolean withdrawFromUser(Player p, double toWithdraw) {
        if (EconomyManager.getEconomy().getBalance(p) >= toWithdraw) {
            if(econ.getBalance(p) >= toWithdraw) {
                EconomyManager.getEconomy().withdrawPlayer(p, toWithdraw);
                return true;
            }
        }
        return false;
    }

    public static Economy getEconomy() {
        return econ;
    }

    private static Economy econ;
}
