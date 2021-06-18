package mc.nightmarephoenix.anchorsell;

import com.sk89q.worldguard.WorldGuard;
import mc.nightmarephoenix.anchorsell.commands.AnchorCommand;
import mc.nightmarephoenix.anchorsell.economy.EconomyManager;
import mc.nightmarephoenix.anchorsell.events.*;
import mc.nightmarephoenix.anchorsell.events.gui.GuiAnchorEvents;
import mc.nightmarephoenix.anchorsell.tasks.PayTask;
import mc.nightmarephoenix.anchorsell.hooks.Global;
import net.prosavage.factionsx.manager.FactionManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;


public final class AnchorSell extends JavaPlugin {

    @Override
    public void onEnable() {
        /**
         * Loading dependencies
         */
        // Vault check
        if (!EconomyManager.setupEconomy()) {
            this.getLogger().severe("Disabled due to no Vault dependency found!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        // World guard check - soft depend
        try {
            WorldGuard.getInstance();
            Global.setWorldGuard(true);
        } catch (NoClassDefFoundError e) {
            this.getLogger().fine("No WorldGuard detected.");
            Global.setWorldGuard(false);
        }
         // FactionsX check - soft depend
        try {
            FactionManager.INSTANCE.getFactions();
            Global.setFactionsX(true);
        } catch (NoClassDefFoundError e) {
            this.getLogger().fine("No FactionsX detected.");
            Global.setFactionsX(false);
        }


        /**
         * Saving config
         */
        this.saveDefaultConfig();

        /**
         * Loading listeners
         */
        getServer().getPluginManager().registerEvents(new ActionAnchor(this), this);     // enabling the listener
        getServer().getPluginManager().registerEvents(new GuiAnchorEvents(this), this); // gui click events
        getServer().getPluginManager().registerEvents(new AnchorPlace(this), this);      // detects where the anchor has been placed and stores all the data
        getServer().getPluginManager().registerEvents(new AnchorBreak(this), this);     // detects where the anchor has been removed and stores all the data
        getServer().getPluginManager().registerEvents(new AnchorBlow(this), this);     // detects where the anchor has been blown and stores all the data

        /**
         * Loading commands
         */
        this.getCommand("anchor").setExecutor(new AnchorCommand(this));

        /**
         * Loading the pay task
         *
         * (starts the timer to pay the player)
         */
        new PayTask(this).runTaskTimer(
                this,
                0,
                20 * this.getConfig().getInt("pay-timer-in-minutes") * 60
        );
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
