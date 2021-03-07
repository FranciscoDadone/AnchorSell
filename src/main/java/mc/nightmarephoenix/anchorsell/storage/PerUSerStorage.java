package mc.nightmarephoenix.anchorsell.storage;

import mc.nightmarephoenix.anchorsell.AnchorSell;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class PerUSerStorage {

    public PerUSerStorage(AnchorSell plugin, Player p) {
        this.plugin = plugin;
        this.p = p;
        saveDefaultConfig(p);
    }

    public void reloadConfig() {
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

    public FileConfiguration getConfig() {
        if(this.dataConfig == null) {
            reloadConfig();
        }
        return this.dataConfig;
    }

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

    public void saveDefaultConfig(Player p) {

        if(this.configFile == null) {
            this.configFile = new File(this.plugin.getDataFolder() + File.separator + "playerdata", p.getUniqueId() + ".yml");
        }
        if(!this.configFile.exists()) {
            new File(this.plugin.getDataFolder() + File.separator + "playerdata", p.getUniqueId() + ".yml");
        }
    }

    private AnchorSell plugin;
    private FileConfiguration dataConfig = null;
    private File configFile = null;
    private Player p;
}
