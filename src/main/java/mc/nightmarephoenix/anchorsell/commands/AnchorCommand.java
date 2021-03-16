package mc.nightmarephoenix.anchorsell.commands;

import mc.nightmarephoenix.anchorsell.AnchorSell;
import mc.nightmarephoenix.anchorsell.inventories.BuyScreen;
import mc.nightmarephoenix.anchorsell.storage.StorageManager;
import mc.nightmarephoenix.anchorsell.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;

public class AnchorCommand implements CommandExecutor {

    public AnchorCommand(AnchorSell plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        boolean isAnchorCommand = label.equalsIgnoreCase("anchor");
        Player player = sender.getServer().getPlayerExact(sender.getName());

        if (isAnchorCommand) { // sender instanceof Player
            // /anchor
            if(args.length == 0) {
                if(sender.hasPermission("anchorsell.admin.help")) {
                    for(String msg: plugin.getConfig().getStringList("help-message-admin")) {
                        sender.sendMessage(Utils.Color(msg));
                    }
                } else if(sender.hasPermission("anchorsell.player.help")) {
                    for(String msg: plugin.getConfig().getStringList("help-message")) {
                        sender.sendMessage(Utils.Color(msg));
                    }
                }
            } else if(sender.hasPermission("anchorsell.admin.reload") && (args.length > 0) && (args[0].equalsIgnoreCase("reload"))) {
                // /anchor reload
                plugin.reloadConfig();
                sender.sendMessage(Utils.Color(plugin.getConfig().getString("reload-message")));
            } else if (sender.hasPermission("anchorsell.admin.give")
                    && args.length > 0
                    && args[0].equalsIgnoreCase("give")) {

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
            } else if (sender.hasPermission("anchorsell.player.buy")
                    && args.length == 1
                    && args[0].equalsIgnoreCase("buy")) {
                player.openInventory(new BuyScreen(player, plugin).getInventory());
            } else if (sender.hasPermission("anchorsell.player.list")
                    && args[0].equalsIgnoreCase("list")) {
                if(args[1].length() > 0 && sender.hasPermission("anchorsell.admin.list")) {
                    try {
                        StorageManager.getAnchorUserList(plugin, Bukkit.getPlayer(args[1]));
                    } catch (InvalidConfigurationException e) {
                        player.sendMessage("An error happened. Contact an administrator.");
                    }
                } else {
                    try {
                        StorageManager.getAnchorUserList(plugin, player);
                    } catch (InvalidConfigurationException e) {
                        player.sendMessage("An error happened. Contact an administrator.");
                    }
                }
            } else if (args[0].equalsIgnoreCase("authors")) {
                player.sendMessage(Utils.Color("&7&m----------&r &5&lAnchor &7&m----------"));
                player.sendMessage(Utils.Color("&ePlugin made by: &fMatiasME and DadoGamer13"));
                player.sendMessage(Utils.Color("&eGithub:&f https://github.com/FranciscoDadone/AnchorSell.git"));
                player.sendMessage(Utils.Color("&7&m----------------------------"));
            }  else if (sender.hasPermission("anchorsell.admin.upgrades") && args[0].equalsIgnoreCase("upgrades")) {
                player.sendMessage(Utils.Color("&7&m----------&r &5&lAnchor &7&m----------"));
                for (int i = 1; i <= 64; i++) {
                    player.sendMessage(Utils.Color(
                            "Level: " + i
                            + " | MToUpgrade: " + Utils.getMoneyToUpgrade(i, plugin)
                            + " | MPerMinute: " + Utils.getMoneyPerMinute(i)
                    ));
                }
                player.sendMessage(Utils.Color("&7&m----------------------------"));
            } else if(sender.hasPermission("anchorsell.admin.changeUpgradeMultiplier") && args.length == 1 && args[0].equalsIgnoreCase("changeupgrademultiplier")) {
                StorageManager.changeUpgradeMultiplier(plugin, Integer.parseInt(args[1]));
                player.sendMessage(Utils.Color("&aMultiplier changed to &c" + Integer.parseInt(args[1])));
            } else if(sender.hasPermission("anchorsell.admin.changePrice") && args.length == 1 && args[0].equalsIgnoreCase("changeprice")) {
                StorageManager.changePrice(plugin, Integer.parseInt(args[1]));
                player.sendMessage(Utils.Color("&aPrice for anchors changed to &c$" + Integer.parseInt(args[1])));
            } else if(sender.hasPermission("anchorsell.admin.changeSafeZone") && args.length == 1 && args[0].equalsIgnoreCase("changeSafeZone")) {
                StorageManager.changeSafeZone(plugin, Integer.parseInt(args[1]));
                player.sendMessage(Utils.Color("&aSafe zone changed to &c$" + Integer.parseInt(args[1])));
            } else {
                player.sendMessage(Utils.Color(plugin.getConfig().getString("unknown-command")));
            }
        }
        return true;
    }

    private AnchorSell plugin;
}