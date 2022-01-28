package com.franciscodadone.anchorsell;

import com.franciscodadone.anchorsell.api.Global;
import com.franciscodadone.anchorsell.api.StorageManager;
import com.franciscodadone.anchorsell.commands.CommandManager;
import com.franciscodadone.anchorsell.events.*;
import com.franciscodadone.anchorsell.events.gui.MainAnchorInventoryEvents;
import com.franciscodadone.anchorsell.hooks.Hooks;
import com.franciscodadone.anchorsell.tasks.ParticleTask;
import com.franciscodadone.anchorsell.thirdparty.bstats.Metrics;
import com.franciscodadone.anchorsell.thirdparty.placeholderapi.PAPIExpansion;
import com.franciscodadone.anchorsell.thirdparty.vault.EconomyManager;
import com.franciscodadone.anchorsell.utils.Logger;
import com.franciscodadone.anchorsell.utils.UpdateChecker;
import com.sk89q.worldguard.WorldGuard;
import com.tchristofferson.configupdater.ConfigUpdater;
import com.franciscodadone.anchorsell.events.gui.AnchorAdminInventoryEvents;
import com.franciscodadone.anchorsell.events.gui.ChangeLevelInventoryEvents;
import com.franciscodadone.anchorsell.thirdparty.holographicdisplays.HologramMaker;
import com.franciscodadone.anchorsell.tasks.PayTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;


public final class AnchorSell extends JavaPlugin {

    @Override
    public void onEnable() {

        // // Global variables // //
        Global.plugin = this;

        // // Loading dependencies // //
        // Vault and economy (dependency)
        if (EconomyManager.setupEconomy()) {
            Logger.info("Vault (econ) hooked!");
        } else {
            Logger.severe("Disabled due to no Vault or Economy plugin found!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        // World guard (soft dependency)
        try {
            WorldGuard wg = WorldGuard.getInstance();
            Logger.info("WorldGuard hooked!");
            if(wg != null) Hooks.isWorldGuardActive = true;
        } catch (NoClassDefFoundError e) {
            Hooks.isWorldGuardActive = false;
        }

        // Placeholder API (soft dependency)
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            Logger.info("PlaceholderAPI hooked!");
            new PAPIExpansion(this).register();
        }

        // HolographicDisplays (soft dependency)
        if(Bukkit.getPluginManager().getPlugin("HolographicDisplays") != null) {
            Logger.info("HolographicDisplays hooked!");
            Hooks.isHolographicDisplaysActive = true;
            Location loc = StorageManager.retrieveHologramLocation();
            if(loc != null) HologramMaker.createHoloTop(loc);
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
                Logger.info("You are running the latest version.");
            } else {
                Logger.warning("There is a new update available. ( &a" + version + "&e )");
                UpdateChecker.updateString = version;
            }
        });

        // bStats metrics
        new Metrics(this, 13580);
    }

    @Override
    public void onDisable() {}
}
