package mc.nightmarephoenix.anchorsell.commands;

import mc.nightmarephoenix.anchorsell.AnchorSell;
import mc.nightmarephoenix.anchorsell.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AnchorCommand implements CommandExecutor {

    public AnchorCommand(AnchorSell plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        boolean isAnchorCommand = label.equalsIgnoreCase("anchor");

        if (isAnchorCommand) { // sender instanceof Player
            if (sender.hasPermission("anchorsell.help") && (args.length == 0)) {
                // /anchor
                for(String msg: plugin.getConfig().getStringList("help-message")) {
                    sender.sendMessage(Utils.Color(msg));
                }
            } else if(sender.hasPermission("anchorsell.reload") && (args.length > 0) && (args[0].equalsIgnoreCase("reload"))) {
                // /anchor reload
                plugin.reloadConfig();
                sender.sendMessage(Utils.Color(plugin.getConfig().getString("reload-message")));
            } else if(sender.hasPermission("anchorsell.give") && (args.length > 0) && (args[0].equalsIgnoreCase("give"))) {
                // /anchor give

                //
                //
                //
            }
        }

        return true;
    }

    private AnchorSell plugin;
}