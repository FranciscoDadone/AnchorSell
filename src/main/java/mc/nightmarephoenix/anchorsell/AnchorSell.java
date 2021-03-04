package mc.nightmarephoenix.anchorsell;

import mc.nightmarephoenix.anchorsell.commands.AnchorCommand;
import mc.nightmarephoenix.anchorsell.events.ActionAnchor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public final class AnchorSell extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new ActionAnchor(), this);

        //this.getCommand("anchor").setExecutor(new AnchorCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return super.onCommand(sender, command, label, args);

    }
}
