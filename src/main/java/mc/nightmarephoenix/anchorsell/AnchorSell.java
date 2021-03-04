package mc.nightmarephoenix.anchorsell;

import mc.nightmarephoenix.anchorsell.commands.Anchor;
import mc.nightmarephoenix.anchorsell.events.ActionAnchor;
import mc.nightmarephoenix.anchorsell.events.MainAnchorEvents;
import org.bukkit.plugin.java.JavaPlugin;

public final class AnchorSell extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new ActionAnchor(), this);     // enabling the listener
        getServer().getPluginManager().registerEvents(new MainAnchorEvents(), this); // gui click events

        getCommand("anchor").setExecutor(new Anchor(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
