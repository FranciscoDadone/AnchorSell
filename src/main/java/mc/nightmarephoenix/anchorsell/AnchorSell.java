package mc.nightmarephoenix.anchorsell;

import mc.nightmarephoenix.anchorsell.commands.AnchorCommand;
import mc.nightmarephoenix.anchorsell.events.ActionAnchor;
import mc.nightmarephoenix.anchorsell.events.AnchorBreak;
import mc.nightmarephoenix.anchorsell.events.MainAnchorEvents;
import mc.nightmarephoenix.anchorsell.events.AnchorPlace;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class AnchorSell extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

        // File
        this.file = new File("anchors.yml");

        // Listeners
        getServer().getPluginManager().registerEvents(new ActionAnchor(), this);     // enabling the listener
        getServer().getPluginManager().registerEvents(new MainAnchorEvents(), this); // gui click events
        getServer().getPluginManager().registerEvents(new AnchorPlace(), this);      // detects where the anchor has been placed and stores all the data
        getServer().getPluginManager().registerEvents(new AnchorBreak(), this);     // detects where the anchor has been remove and stores all the data


        // Commands
        this.getCommand("anchor").setExecutor(new AnchorCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }



    public File file;
}
