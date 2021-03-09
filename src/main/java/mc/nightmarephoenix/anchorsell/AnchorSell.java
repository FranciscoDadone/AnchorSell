package mc.nightmarephoenix.anchorsell;

import mc.nightmarephoenix.anchorsell.commands.AnchorCommand;
import mc.nightmarephoenix.anchorsell.economy.EconomyManager;
import mc.nightmarephoenix.anchorsell.events.*;
import mc.nightmarephoenix.anchorsell.events.gui.GuiAnchorEvents;
import mc.nightmarephoenix.anchorsell.tasks.PayTask;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;


public final class AnchorSell extends JavaPlugin {
    @Override
    public void onEnable() {
        // Vault check
        if (!EconomyManager.setupEconomy()) {
            this.getLogger().severe("Disabled due to no Vault dependency found!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        // Config
        this.saveDefaultConfig();

        // Listeners
        getServer().getPluginManager().registerEvents(new ActionAnchor(this), this);     // enabling the listener
        getServer().getPluginManager().registerEvents(new GuiAnchorEvents(this), this); // gui click events
        getServer().getPluginManager().registerEvents(new AnchorPlace(this), this);      // detects where the anchor has been placed and stores all the data
        getServer().getPluginManager().registerEvents(new AnchorBreak(this), this);     // detects where the anchor has been removed and stores all the data
        getServer().getPluginManager().registerEvents(new AnchorBlow(this), this);     // detects where the anchor has been blown and stores all the data

        // Commands
        this.getCommand("anchor").setExecutor(new AnchorCommand(this));

        // Tasks
        new PayTask(this).runTaskTimer(this, 0, 20 * this.getConfig().getInt("pay-timer-in-minutes") * 60); // starts the timer to pay the player
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
