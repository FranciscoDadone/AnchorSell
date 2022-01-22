package mc.nightmarephoenix.anchorsell;

import com.sk89q.worldguard.WorldGuard;
import com.tchristofferson.configupdater.ConfigUpdater;
import mc.nightmarephoenix.anchorsell.commands.CommandManager;
import mc.nightmarephoenix.anchorsell.events.gui.AnchorAdminInventoryEvents;
import mc.nightmarephoenix.anchorsell.events.gui.ChangeLevelInventoryEvents;
import mc.nightmarephoenix.anchorsell.thirdparty.essentials.EssentialsManager;
import mc.nightmarephoenix.anchorsell.thirdparty.placeholderapi.PAPIExpansion;
import mc.nightmarephoenix.anchorsell.thirdparty.vault.EconomyManager;
import mc.nightmarephoenix.anchorsell.events.*;
import mc.nightmarephoenix.anchorsell.events.gui.MainAnchorInventoryEvents;
import mc.nightmarephoenix.anchorsell.api.Global;
import mc.nightmarephoenix.anchorsell.api.StorageManager;
import mc.nightmarephoenix.anchorsell.tasks.ParticleTask;
import mc.nightmarephoenix.anchorsell.tasks.PayTask;
import mc.nightmarephoenix.anchorsell.hooks.Hooks;
import mc.nightmarephoenix.anchorsell.thirdparty.bstats.Metrics;
import mc.nightmarephoenix.anchorsell.utils.UpdateChecker;
import mc.nightmarephoenix.anchorsell.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;


public final class AnchorSell extends JavaPlugin {

    @Override
    public void onEnable() {

        // // Global variables // //
        Global.plugin = this;

        // // Loading dependencies // //
        // Vault and economy (dependency)
        if (EconomyManager.setupEconomy()) {
            this.getLogger().info("Vault (econ) hooked!");
        } else {
            this.getLogger().severe("Disabled due to no Vault or Economy plugin found!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        // World guard (soft dependency)
        try {
            WorldGuard wg = WorldGuard.getInstance();
            this.getLogger().info("WorldGuard hooked!");
            if(wg != null) Hooks.setWorldGuard(true);
        } catch (NoClassDefFoundError e) {
            Hooks.setWorldGuard(false);
        }

        // Placeholder API (soft dependency)
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            this.getLogger().info("PlaceholderAPI hooked!");
            new PAPIExpansion(this).register();
        }

        // // Config // //
        this.saveDefaultConfig();
        File configFile = new File(getDataFolder(), "config.yml");

        try {
            ConfigUpdater.update(this, "config.yml", configFile, List.of());
        } catch (IOException e) {
            e.printStackTrace();
        }

        reloadConfig();

        // // Loading events // //
        getServer().getPluginManager().registerEvents(new ActionAnchor(this), this);               // enabling the listener
        getServer().getPluginManager().registerEvents(new MainAnchorInventoryEvents(this), this);  // gui click events
        getServer().getPluginManager().registerEvents(new AnchorPlace(this), this);                // detects where the anchor has been placed and stores all the data
        getServer().getPluginManager().registerEvents(new AnchorBreak(this), this);                // detects where the anchor has been removed and stores all the data
        getServer().getPluginManager().registerEvents(new AnchorBlow(this), this);                 // detects where the anchor has been blown and stores all the data
        getServer().getPluginManager().registerEvents(new UpdateMessageOnJoin(), this);                  // detects updates and shows to all op's on join.
        getServer().getPluginManager().registerEvents(new AnchorAdminInventoryEvents(this), this); // events in the anchor admin inventory
        getServer().getPluginManager().registerEvents(new ChangeLevelInventoryEvents(this), this); // events in the anchor admin inventory

        // // Loading commands // //
        Objects.requireNonNull(this.getCommand("anchor")).setExecutor(new CommandManager());

        // // Caching all anchors // //
        StorageManager.cacheAllAnchors();

        // // Particles status // //
        Global.particlesStatus = this.getConfig().getString("particles");

        /*
         * Loading the pay task
         *
         * (starts the timer to pay the player)
         */
        new PayTask(this).runTaskTimer(
                this,
                0,
                20L * this.getConfig().getInt("pay-timer-in-minutes") * 60
        );

        /*
         * Loading the particle task
         *
         * (Generates particles around the anchors)
         */
        new ParticleTask().runTaskTimer(
                this,
                0,
                10
        );

        // // Update checker // //
        new UpdateChecker(90038).getVersion(version -> {
            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                Utils.sendMessage("You are running the latest version.");
            } else {
                Utils.sendMessage("There is a new update available. ( &a" + version + "&f )");
                UpdateChecker.updateString = version;
            }
        });


        // bStats metrics
        new Metrics(this, 13580);

    }

    @Override
    public void onDisable() {}
}
