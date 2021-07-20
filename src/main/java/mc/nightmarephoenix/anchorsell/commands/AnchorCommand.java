package mc.nightmarephoenix.anchorsell.commands;

import mc.nightmarephoenix.anchorsell.AnchorSell;
import mc.nightmarephoenix.anchorsell.inventories.BuyScreen;
import mc.nightmarephoenix.anchorsell.storage.Cache;
import mc.nightmarephoenix.anchorsell.storage.StorageManager;
import mc.nightmarephoenix.anchorsell.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.UUID;

public class AnchorCommand implements CommandExecutor {

    public AnchorCommand(AnchorSell plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(label.equalsIgnoreCase("anchor")) {
            if(sender.hasPermission("anchorsell.admin.help") && args.length == 0) {
            // anchor (admin)
                for(String msg: plugin.getConfig().getStringList("help-message-admin")) {
                    sender.sendMessage(Utils.Color(msg));
                }
            } else if(sender.hasPermission("anchorsell.player.help") && args.length == 0) {
            // anchor (player)
                for(String msg: plugin.getConfig().getStringList("help-message")) {
                    sender.sendMessage(Utils.Color(msg));
                }
            } else if(sender.hasPermission("anchorsell.admin.reload") && args[0].equalsIgnoreCase("reload")) {
            // anchor reload
                plugin.reloadConfig();
                sender.sendMessage(Utils.Color(plugin.getConfig().getString("reload-message")));
            } else if(sender.hasPermission("anchorsell.admin.give") && args.length > 0 && args[0].equalsIgnoreCase("give")) {
            // anchor give
                // Anchor give without argument
                if (args.length == 1) {
                    sender.sendMessage(Utils.Color(plugin.getConfig().getString("cant-give-anchor-message")));
                }
                // Give one Anchor
                else if (args.length == 2) {
                    Player p = sender.getServer().getPlayer(args[1]);
                    if(!p.getInventory().addItem(Utils.getAnchor(1, 1)).isEmpty()) {
                        sender.sendMessage(Utils.Color("&c" + p.getName() + " has their inventory full."));
                    } else {
                        sender.sendMessage(Utils.Color("&aGave &c1 &aAnchor to &c" + p.getName()));
                    }
                }
                // Give Anchor with quantity argument
                else if (args.length == 3) {
                    Player p = sender.getServer().getPlayer(args[1]);
                    if(!p.getInventory().addItem(Utils.getAnchor(1, Integer.parseInt(args[2]))).isEmpty()) {
                        sender.sendMessage(Utils.Color("&c" + p.getName() + " has their inventory full."));
                    } else {
                        sender.sendMessage(Utils.Color("&aGave &c" + args[2] + " &aAnchor to &c" + p.getName()));
                    }
                }
                // Give Anchor with quantity and level argument
                else if (args.length == 4) {
                    Player p = sender.getServer().getPlayer(args[1]);
                    if(!p.getInventory().addItem(Utils.getAnchor(Integer.parseInt(args[3]), Integer.parseInt(args[2]))).isEmpty()) {
                        sender.sendMessage(Utils.Color("&c" + p.getName() + " has their inventory full."));
                    } else {
                        sender.sendMessage(Utils.Color("&aGave &c" + args[2] + " &aAnchor to &c" + p.getName() + " &a(level " + Integer.parseInt(args[3]) + ")"));
                    }
                }
            } else if (sender.hasPermission("anchorsell.player.buy") && args[0].equalsIgnoreCase("buy") && args.length == 1) {
            // anchor buy
                Bukkit.getPlayer(sender.getName()).openInventory(new BuyScreen(Bukkit.getPlayer(sender.getName()), plugin).getInventory());
            } else if (sender.hasPermission("anchorsell.player.list") && args[0].equalsIgnoreCase("list") && args.length == 1) {
            // anchor list
                try {
                    StorageManager.getAnchorUserList(plugin, Bukkit.getPlayer(sender.getName()), Bukkit.getPlayer(sender.getName()));
                } catch (InvalidConfigurationException e) {
                    sender.sendMessage("An error happened. Contact an administrator.");
                }
            } else if (sender.hasPermission("anchorsell.admin.list") && args[0].equalsIgnoreCase("list") && args.length == 2) {
            // anchor list username
                try {
                    StorageManager.getAnchorUserList(plugin, Bukkit.getOfflinePlayer(args[1]), sender);
                } catch (InvalidConfigurationException e) {
                    sender.sendMessage("An error happened. Contact an administrator.");
                }
            } else if (sender.hasPermission("anchorsell.admin.changeUpgradeMultiplier") && args[0].equalsIgnoreCase("changeupgrademultiplier")) {
            // anchor changeupgrademultiplier
                StorageManager.changeUpgradeMultiplier(plugin, Integer.parseInt(args[1]));
                sender.sendMessage(Utils.Color("&aMultiplier changed to &c" + Integer.parseInt(args[1])));
            } else if (sender.hasPermission("anchorsell.admin.changePrice") && args[0].equalsIgnoreCase("changeprice")) {
            // anchor changeprice
                StorageManager.changePrice(plugin, Integer.parseInt(args[1]));
                sender.sendMessage(Utils.Color("&aPrice for anchors changed to &c$" + Integer.parseInt(args[1])));
            } else if (sender.hasPermission("anchorsell.admin.changeSafeZone") && args[0].equalsIgnoreCase("changeSafeZone")) {
            // anchor changesafezone
                StorageManager.changeSafeZone(plugin, Integer.parseInt(args[1]));
                sender.sendMessage(Utils.Color("&aSafe zone changed to &c" + Integer.parseInt(args[1])));
            } else if (sender.hasPermission("anchorsell.admin.upgrades") && args[0].equalsIgnoreCase("upgrades")) {
            // anchor upgrades
                sender.sendMessage(Utils.Color("&7&m----------&r &5&lAnchor &7&m----------"));
                for (int i = 1; i <= 64; i++) {
                    sender.sendMessage(Utils.Color(
                            "Level: " + i
                                    + " | MToUpgrade: " + Utils.getMoneyToUpgrade(i, plugin)
                                    + " | MPerMinute: " + Utils.getMoneyPerMinute(i)
                    ));
                }
                sender.sendMessage(Utils.Color("&7&m----------------------------"));
            } else if(sender.hasPermission("anchorsell.admin.getUserFileName") && args[0].equalsIgnoreCase("getuserfilename")) {
            // anchor getUserFileName
                if(args.length == 2) {
                    sender.sendMessage(Utils.Color("&aUser file name: &c" + Bukkit.getPlayer(args[1]).getUniqueId() + ".yml"));
                } else {
                    sender.sendMessage(Utils.Color("Usage: &e/anchor getuserfilename [username]"));
                }
            } else if(sender.hasPermission("anchorsell.admin.changeTotalAnchorsUserCanHave") && args[0].equalsIgnoreCase("changeTotalAnchorsUserCanHave")) {
            // anchor changetotalanchorsusercanhave
                if(args.length == 2) {
                    StorageManager.changeTotalAnchorsUserCanHave(plugin, Integer.parseInt(args[1]));
                    sender.sendMessage(Utils.Color("&aTotal anchors per user changed to &c" + args[1]));
                } else {
                    sender.sendMessage(Utils.Color("Usage: &e/anchor changeTotalAnchorsUserCanHave [number]"));
                }
            } else if(sender.hasPermission("anchorsell.admin.revalidate") && args[0].equalsIgnoreCase("revalidate")) {
            // anchor revalidate
                if(args.length == 2) {
                    StorageManager.revalidateUser(plugin, Bukkit.getOfflinePlayer(args[1]));
                    sender.sendMessage(Utils.Color("&aRevalidated &c" + args[1] + " &afiles."));
                } else {
                    sender.sendMessage(Utils.Color("Usage: &e/anchor revalidate [username]"));
                }
            } else if(sender.hasPermission("anchorsell.admin.particles") && args[0].equalsIgnoreCase("particles")) {
                if(args.length == 2 && (args[1].equalsIgnoreCase("all") || args[1].equalsIgnoreCase("low") || args[1].equalsIgnoreCase("off"))) {
                    plugin.getConfig().set("particles", args[1]);
                    plugin.reloadConfig();
                    Cache.particlesStatus = args[1];

                    sender.sendMessage(Utils.Color("&aParticles changed to " + args[1] + "."));
                } else {
                    sender.sendMessage(Utils.Color("Usage: &e/anchor particles [all/low/off]"));
                }
            } else if(args[0].equalsIgnoreCase("top")) {
            // anchor top
                HashMap<String, Integer> top = StorageManager.getAnchorTop(plugin);
                int page = 1;
                double a = top.size();
                double maxPages = Math.ceil(a / 10);
                try {
                    if(Integer.parseInt(args[1]) > 0 && Integer.parseInt(args[1]) <= maxPages) {
                        page = Integer.parseInt(args[1]);
                    } else if(Integer.parseInt(args[1]) > maxPages) {
                        page = (int) maxPages;
                    }
                } catch (Exception e1) {}

                int n = 1 + (1 * (10 * (page - 1)));
                boolean skipFirst = page != 1;
                sender.sendMessage(Utils.Color("&7&m----------&r &5&lAnchor TOP &r&5&o(" + page + "/" + (int) maxPages + ") &7&m----------"));
                for(int i = (top.size() - (10 * (page - 1))); i > (top.size() - (10 * page) - 1); i--) {
                    if(skipFirst) {
                        if((top.size() - (10 * (page - 1))) != i){
                            try {
                                sender.sendMessage(Utils.Color("&7#" + n + " &b" + Bukkit.getOfflinePlayer(UUID.fromString(top.keySet().toArray()[i].toString())).getName() + "&7 - &f" + top.get(top.keySet().toArray()[i])));
                                n++;
                            } catch (Exception e) {}
                        }
                    } else {
                        try {
                            sender.sendMessage(Utils.Color("&7#" + n + " &b" + Bukkit.getOfflinePlayer(UUID.fromString(top.keySet().toArray()[i].toString())).getName() + "&7 - &f" + top.get(top.keySet().toArray()[i])));
                            n++;
                        } catch (Exception e) {}
                    }
                }
                sender.sendMessage(Utils.Color("&7&m-------------------------------------"));
            } else {
                if (args[0].equalsIgnoreCase("authors")) {
                    sender.sendMessage(Utils.Color("&7&m----------&r &5&lAnchor &7&m----------"));
                    sender.sendMessage(Utils.Color("&ePlugin made by: &fMatiasME and DadoGamer13"));
                    sender.sendMessage(Utils.Color("&eGithub:&f https://github.com/FranciscoDadone/AnchorSell.git"));
                    sender.sendMessage(Utils.Color("&7&m----------------------------"));
                } else {
                    sender.sendMessage(Utils.Color(plugin.getConfig().getString("unknown-command")));
                }
            }
        }
        return true;
    }

    private AnchorSell plugin;
}