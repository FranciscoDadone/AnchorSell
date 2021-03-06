package mc.nightmarephoenix.anchorsell.storage;

import mc.nightmarephoenix.anchorsell.AnchorSell;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class GeneralStorage {

    public GeneralStorage(AnchorSell plugin) {
        this.plugin = plugin;
        saveDefaultConfig();
    }

    /**
     * Reloads the general config file with the all anchors.
     */
    public void reloadConfig() {
        if(this.configFile == null) {
            this.configFile = new File(this.plugin.getDataFolder(), "all_anchors.yml");
        }
        this.dataConfig = YamlConfiguration.loadConfiguration(this.configFile);
        InputStream defaultStream = this.plugin.getResource( "all_anchors.yml");

        if(defaultStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.dataConfig.setDefaults(defaultConfig);
        }
    }

    /**
     * Returns the config.
     * @return
     */
    public FileConfiguration getConfig() {
        if(this.dataConfig == null) {
            reloadConfig();
        }
        return this.dataConfig;
    }

    /**
     * Saves the config.
     */
    public void saveConfig() {
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
     * Saves a default config.
     */
    public void saveDefaultConfig() {

        if(this.configFile == null) {
            this.configFile = new File(this.plugin.getDataFolder(), "all_anchors.yml");
        }
        if(!this.configFile.exists()) {
            new File(this.plugin.getDataFolder(), "all_anchors.yml");
        }
    }

    private AnchorSell plugin;
    private FileConfiguration dataConfig = null;
    private File configFile = null;
}
