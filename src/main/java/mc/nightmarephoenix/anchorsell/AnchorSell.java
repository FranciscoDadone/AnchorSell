package mc.nightmarephoenix.anchorsell;

import org.bukkit.plugin.java.JavaPlugin;

public final class AnchorSell extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new ActionAnchor(), this); // enabling the listener
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
