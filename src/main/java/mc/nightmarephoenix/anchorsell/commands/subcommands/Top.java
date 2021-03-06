package mc.nightmarephoenix.anchorsell.commands.subcommands;

import mc.nightmarephoenix.anchorsell.storage.Global;
import mc.nightmarephoenix.anchorsell.storage.StorageManager;
import mc.nightmarephoenix.anchorsell.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Top extends SubCommands {
    @Override
    public String getDescription() {
        return "Shows the top of the player anchors in the server.";
    }

    @Override
    public String getName() {
        return "top";
    }

    @Override
    public String syntax() {
        return "/anchor top";
    }

    @Override
    public List<String> getSubCommandsArgs(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public String getPermission() {
        return "anchorsell.player.top";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        HashMap<String, Integer> top = StorageManager.getAnchorTop(Global.plugin);
        int page = 1;
        double a = top.size();
        double maxPages = Math.ceil(a / 10);
        try {
            if (Integer.parseInt(args[1]) > 0 && Integer.parseInt(args[1]) <= maxPages) {
                page = Integer.parseInt(args[1]);
            } else if (Integer.parseInt(args[1]) > maxPages) {
                page = (int) maxPages;
            }
        } catch (Exception e1) {
        }

        int n = 1 + (1 * (10 * (page - 1)));
        boolean skipFirst = page != 1;
        sender.sendMessage(Utils.Color("&7&m----------&r &5&lAnchor TOP &r&5&o(" + page + "/" + (int) maxPages + ") &7&m----------"));
        for (int i = (top.size() - (10 * (page - 1))); i > (top.size() - (10 * page) - 1); i--) {
            if (skipFirst) {
                if ((top.size() - (10 * (page - 1))) != i) {
                    try {
                        sender.sendMessage(Utils.Color("&7#" + n + " &b" + Bukkit.getOfflinePlayer(UUID.fromString(top.keySet().toArray()[i].toString())).getName() + "&7 - &f" + top.get(top.keySet().toArray()[i])));
                        n++;
                    } catch (Exception e) {
                    }
                }
            } else {
                try {
                    sender.sendMessage(Utils.Color("&7#" + n + " &b" + Bukkit.getOfflinePlayer(UUID.fromString(top.keySet().toArray()[i].toString())).getName() + "&7 - &f" + top.get(top.keySet().toArray()[i])));
                    n++;
                } catch (Exception e) {
                }
            }
        }
        sender.sendMessage(Utils.Color("&7&m-------------------------------------"));
    }
}
