package com.franciscodadone.anchorsell.api;

import com.franciscodadone.anchorsell.AnchorSell;
import com.franciscodadone.anchorsell.utils.Logger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HologramsStorage {

    protected HologramsStorage() {
        this.plugin = Global.plugin;
        saveDefaultConfig();
    }

    /**
     * Reloads the general config file with the all anchors.
     */
    protected void reloadConfig() {
        if(this.configFile == null) {
            this.configFile = new File(this.plugin.getDataFolder(), "holograms.yml");
        }
        this.dataConfig = YamlConfiguration.loadConfiguration(this.configFile);
        InputStream defaultStream = this.plugin.getResource( "holograms.yml");

        if(defaultStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.dataConfig.setDefaults(defaultConfig);
        }
    }

    /**
     * Returns the config.
     * @return FileConfiguration
     */
    protected FileConfiguration getConfig() {
        if(this.dataConfig == null) {
            reloadConfig();
        }
        return this.dataConfig;
    }

    /**
     * Saves the config.
     */
    protected void saveConfig() {
        if(this.dataConfig == null || this.configFile == null) {
            return;
        }
        try {
            this.getConfig().save(this.configFile);
        } catch (IOException e) {
            Logger.severe("Could not save config to " + this.configFile);
        }
    }

    /**
     * Saves a default config.
     */
    protected void saveDefaultConfig() {

        if(this.configFile == null) {
            this.configFile = new File(this.plugin.getDataFolder(), "holograms.yml");
        }
        if(!this.configFile.exists()) {
            new File(this.plugin.getDataFolder(), "holograms.yml");
        }
    }

    private final AnchorSell plugin;
    private FileConfiguration dataConfig = null;
    private File configFile = null;

}
