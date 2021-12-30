package mc.nightmarephoenix.anchorsell;

import com.earth2me.essentials.Essentials;
import com.sk89q.worldguard.WorldGuard;
import com.tchristofferson.configupdater.ConfigUpdater;
import mc.nightmarephoenix.anchorsell.commands.CommandManager;
import mc.nightmarephoenix.anchorsell.thirdparty.essentials.EssentialsManager;
import mc.nightmarephoenix.anchorsell.thirdparty.vault.EconomyManager;
import mc.nightmarephoenix.anchorsell.events.*;
import mc.nightmarephoenix.anchorsell.events.gui.GuiAnchorEvents;
import mc.nightmarephoenix.anchorsell.storage.Global;
import mc.nightmarephoenix.anchorsell.storage.StorageManager;
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
import java.util.Arrays;
import java.util.logging.Level;


public final class AnchorSell extends JavaPlugin {

    @Override
    public void onEnable() {

        // // Global variables // //
        Global.plugin = this;

        // // Loading dependencies // //
        // Vault check
        if (!EconomyManager.setupEconomy()) {
            this.getLogger().severe("Disabled due to no Vault or EssentialsX found!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        try {
            if(getServer().getPluginManager().getPlugin("Essentials") != null) {
                EssentialsManager.setupEssentials();
                this.getLogger().log(Level.FINE, "EssentialsX hooked!");
            } else {
                this.getLogger().log(Level.SEVERE, "No EssentialsX plugin found :( Disabling AnchorSell...");
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        // World guard check - soft depend
        try {
            WorldGuard.getInstance();
            Hooks.setWorldGuard(true);
        } catch (NoClassDefFoundError e) {
            this.getLogger().fine("No WorldGuard detected.");
            Hooks.setWorldGuard(false);
        }

        // // Saving config // //
        this.saveDefaultConfig();
        File configFile = new File(getDataFolder(), "config.yml");

        try {
            ConfigUpdater.update(this, "config.yml", configFile, Arrays.asList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        reloadConfig();

        // // Loading events // //
        getServer().getPluginManager().registerEvents(new ActionAnchor(this), this);    // enabling the listener
        getServer().getPluginManager().registerEvents(new GuiAnchorEvents(this), this); // gui click events
        getServer().getPluginManager().registerEvents(new AnchorPlace(this), this);     // detects where the anchor has been placed and stores all the data
        getServer().getPluginManager().registerEvents(new AnchorBreak(this), this);     // detects where the anchor has been removed and stores all the data
        getServer().getPluginManager().registerEvents(new AnchorBlow(this), this);      // detects where the anchor has been blown and stores all the data
        getServer().getPluginManager().registerEvents(new UpdateMessageOnJoin(), this);       // detects updates and shows to all op's on join.

        // // Loading commands // //
        this.getCommand("anchor").setExecutor(new CommandManager());

        // // Caching all anchors // //
        StorageManager.cacheAllAnchors(this);

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
                20 * this.getConfig().getInt("pay-timer-in-minutes") * 60
        );

        /*
         * Loading the particle task
         *
         * (Generates particles around the anchors)
         */
        new ParticleTask(this).runTaskTimer(
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


        /**
         * bStats metrics
         */
        Metrics metrics = new Metrics(this, 13580);

    }

    @Override
    public void onDisable() {}
}
