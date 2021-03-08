package mc.nightmarephoenix.anchorsell.commands;

import mc.nightmarephoenix.anchorsell.AnchorSell;
import mc.nightmarephoenix.anchorsell.inventories.AnchorScreen;
import mc.nightmarephoenix.anchorsell.inventories.BuyScreen;
import mc.nightmarephoenix.anchorsell.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

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
            } else if (sender.hasPermission("anchorsell.give")
                    && args.length > 0
                    && args[0].equalsIgnoreCase("give")) {

                ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();

                // Anchor give without argument
                if (args.length == 1) {
                    sender.sendMessage(Utils.Color(plugin.getConfig().getString("cant-give-anchor-message")));
                }
                // Give one Anchor
                else if (args.length == 2) {
                    Player p = sender.getServer().getPlayer(args[1]);
                    p.getInventory().addItem(Utils.getAnchor(1, 1));
                }
                // Give Anchor with quantity argument
                else if (args.length == 3) {
                    Player p = sender.getServer().getPlayer(args[1]);
                    p.getInventory().addItem(Utils.getAnchor(1, Integer.parseInt(args[2])));
                }
                // Give Anchor with quantity and level argument
                else if (args.length == 4) {
                    Player p = sender.getServer().getPlayer(args[1]);
                    p.getInventory().addItem(Utils.getAnchor(Integer.parseInt(args[3]), Integer.parseInt(args[2])));
                }
            } else if (sender.hasPermission("anchorsell.give")
                    && args.length == 1
                    && args[0].equalsIgnoreCase("buy")) {

                Player player = sender.getServer().getPlayerExact(sender.getName());
                player.openInventory(new BuyScreen(player, plugin).getInventory());
            }
        }
        return true;
    }

    private AnchorSell plugin;
}