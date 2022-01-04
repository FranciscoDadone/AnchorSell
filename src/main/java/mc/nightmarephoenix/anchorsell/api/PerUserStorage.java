package mc.nightmarephoenix.anchorsell.api;

import mc.nightmarephoenix.anchorsell.AnchorSell;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

class PerUserStorage {

    protected PerUserStorage(OfflinePlayer p) {
        this.plugin = Global.plugin;
        this.p = p;
        saveDefaultConfig(p);
    }

    /**
     * Reloads a user config.
     */
    protected void reloadConfig() {
        if(this.configFile == null) {
            this.configFile = new File(this.plugin.getDataFolder() + File.separator + "playerdata", p.getUniqueId() + ".yml");
        }
        this.dataConfig = YamlConfiguration.loadConfiguration(this.configFile);
        InputStream defaultStream = this.plugin.getResource(p.getUniqueId() + ".yml");

        if(defaultStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.dataConfig.setDefaults(defaultConfig);
        }
    }

    /**
     * Gets a user config.
     * @return FileConfiguration
     */
    protected FileConfiguration getConfig() {
        if(this.dataConfig == null) {
            reloadConfig();
        }
        return this.dataConfig;
    }

    /**
     * Saves a user config.
     */
    protected void saveConfig() {
        if(this.dataConfig == null || this.configFile == null) {
            return;
        }
        try {
            this.getConfig().save(this.configFile);
        } catch (IOException e) {
            this.plugin.getLogger().log(Level.SEVERE, "Could not save config to " + this.configFile, e);
        }
    }

    /**
     * Saves a new user config.
     * @param p offPLayer
     */
    protected void saveDefaultConfig(OfflinePlayer p) {

        if(this.configFile == null) {
            this.configFile = new File(
                    this.plugin.getDataFolder() + File.separator + "playerdata",
                    p.getUniqueId() + ".yml"
            );
        }
        if(!this.configFile.exists()) {
            new File(this.plugin.getDataFolder() + File.separator + "playerdata",
                    p.getUniqueId() + ".yml"
            );
        }
    }

    private final AnchorSell plugin;
    private FileConfiguration dataConfig = null;
    private File configFile = null;
    private final OfflinePlayer p;
}
