package mc.nightmarephoenix.anchorsell;

import mc.nightmarephoenix.anchorsell.commands.AnchorCommand;
import mc.nightmarephoenix.anchorsell.events.ActionAnchor;
import mc.nightmarephoenix.anchorsell.events.AnchorBreak;
import mc.nightmarephoenix.anchorsell.events.MainAnchorEvents;
import mc.nightmarephoenix.anchorsell.events.AnchorPlace;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;


public final class AnchorSell extends JavaPlugin {

//    public static File file;

    @Override
    public void onEnable() {
        // Vault check
        if (!setupEconomy()) {
            this.getLogger().severe("Disabled due to no Vault dependency found!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        // Config
        this.saveDefaultConfig();
        System.out.println();



        // Listeners
        getServer().getPluginManager().registerEvents(new ActionAnchor(this), this);     // enabling the listener
        getServer().getPluginManager().registerEvents(new MainAnchorEvents(this), this); // gui click events
        getServer().getPluginManager().registerEvents(new AnchorPlace(this), this);      // detects where the anchor has been placed and stores all the data
        getServer().getPluginManager().registerEvents(new AnchorBreak(this), this);     // detects where the anchor has been remove and stores all the data


        // Commands
        this.getCommand("anchor").setExecutor(new AnchorCommand(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    private boolean setupEconomy() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public Economy getEconomy() {
        return econ;
    }


    private Economy econ;
}
